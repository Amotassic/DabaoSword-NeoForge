package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.ReachDefend;
import com.amotassic.dabaosword.api.Skill;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Gamerule;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Objects;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EntityTickEvents {

    @SubscribeEvent
    public static void endLivingTick(EntityTickEvent.Post event) {
        Entity en = event.getEntity();
        if (en.level() instanceof ServerLevel world && en instanceof LivingEntity entity) {
            if (world.getGameTime() % 2 == 0) {
                entity.getTags().remove("sha");
                en.getTags().remove("juedou");
                entity.getTags().remove("nanman");
                entity.getTags().remove("benxi");
                entity.getTags().remove("xingshang");
            }

            Player closestPlayer = world.getNearestPlayer(entity, 5);
            if (closestPlayer != null && hasTrinket(ModItems.FANGTIAN, closestPlayer) && entity.isAlive()) {
                ItemStack stack = trinketItem(ModItems.FANGTIAN, closestPlayer);
                int time = 0;
                if (stack != null) time = getCD(stack);
                if (time > 15 && closestPlayer.swingTime == 1) {
                    //给玩家本人一个极短的无敌效果，以防止被误伤
                    closestPlayer.addEffect(new MobEffectInstance(ModItems.INVULNERABLE,2,0,false,false,false));
                    float i = (float) closestPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    entity.hurt(closestPlayer.damageSources().playerAttack(closestPlayer), i);
                }
            }
        }
    }

    @SubscribeEvent
    public static void endPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel world) {
            var time = world.getGameTime();
            int giveCard = Math.max(20, world.getGameRules().getInt(Gamerule.GIVE_CARD_INTERVAL) * 20);
            boolean limit = world.getGameRules().getBoolean(Gamerule.ENABLE_CARDS_LIMIT);

            if (time % 100 == 0) {
                //若玩家不在任何一场对战中，且拥有身份标签，则移除。如果意外卡在旁观者模式，变回生存模式并杀死
                if (!PVPGameEvents.getGameManager().isPlayerInGame(player)) {
                    var tags = player.getTags();
                    if (tags.contains("dabaosword.zhong") || tags.contains("dabaosword.fan") || tags.contains("dabaosword.nei")) {
                        player.getTags().remove("dabaosword.zhong");
                        player.getTags().remove("dabaosword.fan");
                        player.getTags().remove("dabaosword.nei");
                        if (player.isSpectator()) {
                            ((ServerPlayer) player).setGameMode(GameType.SURVIVAL);
                            player.kill();
                        }
                    }
                }
            }

            if (time % giveCard == 0) { // 每分钟摸两张牌
                if (!player.isCreative() && !player.isSpectator() && player.isAlive()) {
                    player.displayClientMessage(Component.translatable("dabaosword.draw"),true);
                    if (player.hasEffect(ModItems.BINGLIANG)) player.removeEffect(ModItems.BINGLIANG);
                    else if (countCards(player) < player.getMaxHealth() || !limit) {
                        int draw = hasTrinket(ModItems.CARD_PILE, player) ? 2 : 0;
                        for (var stack : allTrinkets(player)) {
                            if (stack.getItem() instanceof Skill s && canTrigger(stack, player)) {
                                int i = s.onDrawPhase(player, stack);
                                if (i <= -114) {draw = 0; break;}
                                draw += i;
                            }
                        }
                        if (draw > 0) draw(player, draw);
                    }
                }
            }

            AABB box = new AABB(player.getOnPos()).inflate(20); // 检测范围，根据需要修改
            for (LivingEntity nearbyPlayer : world.getEntitiesOfClass(Player.class, box, playerEntity -> playerEntity.hasEffect(ModItems.DEFEND))) {
                //实现沈佳宜的效果：若玩家看到的玩家有近战防御效果，则给当前玩家攻击范围缩短效果
                int amplifier = Objects.requireNonNull(nearbyPlayer.getEffect(ModItems.DEFEND)).getAmplifier();
                int attack = (int) (player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) + 3);
                int defended = Math.min(amplifier, attack);
                if (player != nearbyPlayer && isLooking(player, nearbyPlayer)) {
                    player.addEffect(new MobEffectInstance(ModItems.DEFENDED, 1, defended,false,false,true));
                }
            }

            //处理所有加触及距离和近战防御距离的效果
            int level1 = 0; int level2 = 0;
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.is(ModItems.DISCARD) || mainHand.is(ModItems.JUEDOU)) level1 += 114;
            for (var stack : allTrinkets(player)) {
                if (stack.getItem() instanceof ReachDefend rd && canTrigger(stack, player)) {
                    level1 += rd.getExtraReach(player, stack);
                    level2 += rd.getDefend(player, stack);
                }
            }
            if (level1 > 0) player.addEffect(new MobEffectInstance(ModItems.REACH, 2,level1 - 1,false,false,false));
            if (level2 > 0) player.addEffect(new MobEffectInstance(ModItems.DEFEND, 2,level2 - 1,false,false,false));

        }
    }

    static boolean isLooking(Player player, Entity entity) {
        Vec3 vec3d = player.getViewVector(1.0f).normalize();
        Vec3 vec3d2 = new Vec3(entity.getX() - player.getX(), entity.getEyeY() - player.getEyeY(), entity.getZ() - player.getZ());
        double d = vec3d2.length();
        double e = vec3d.dot(vec3d2.normalize());
        if (e > 1.0 - 0.25 / d) {
            return player.hasLineOfSight(entity);
        }
        return false;
    }
}
