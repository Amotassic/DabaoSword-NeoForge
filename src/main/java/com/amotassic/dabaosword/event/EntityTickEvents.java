package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.Skill;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Gamerule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
            int skill = world.getGameRules().getInt(Gamerule.CHANGE_SKILL_INTERVAL) * 20;
            boolean limit = world.getGameRules().getBoolean(Gamerule.ENABLE_CARDS_LIMIT);

            if (time % giveCard == 0) { // 每分钟摸两张牌
                if (!player.isCreative() && !player.isSpectator() && player.isAlive()) {
                    player.displayClientMessage(Component.translatable("dabaosword.draw"),true);
                    if (player.hasEffect(ModItems.BINGLIANG)) player.removeEffect(ModItems.BINGLIANG);
                    else if (countCards(player) < player.getMaxHealth() || !limit) {
                        int draw = 0;
                        for (var stack : allTrinkets(player)) {
                            if (stack.getItem() instanceof Skill s) {
                                int i = s.onDrawPhase(player, stack);
                                if (i <= -114) {draw = 0; break;}
                                draw += i;
                            }
                        }
                        if (draw > 0) draw(player, draw);
                    }
                }
            }

            if (skill >= 0) {
                if (skill == 0) player.addTag("change_skill");
                else if (time % skill == 0) { //每5分钟可以切换技能
                    player.addTag("change_skill");
                    if (skill >= 600 && hasTrinket(ModItems.CARD_PILE, player)) {
                        player.displayClientMessage(Component.translatable("dabaosword.change_skill").withStyle(ChatFormatting.BOLD), false);
                        player.displayClientMessage(Component.translatable("dabaosword.change_skill2"), false);
                    }
                }
            }

            if (time % 2 == 0) {
                player.getTags().remove("benxi");
                player.getTags().remove("xingshang");

                //牌堆恢复饱食度
                boolean food = world.getGameRules().getBoolean(Gamerule.CARD_PILE_HUNGERLESS);
                if (hasTrinket(ModItems.CARD_PILE, player) && food) player.getFoodData().setFoodLevel(20);
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
            int level1 = 0; int longHand = 0; int level2 = 0;
            ItemStack mainHand = player.getMainHandItem();
            if (hasTrinket(SkillCards.BENXI, player)) longHand += getTag(trinketItem(SkillCards.BENXI, player));
            if (mainHand.is(ModItems.DISCARD) || mainHand.is(ModItems.JUEDOU)) longHand += 114;
            if (noTieji(player)) {
                if (hasTrinket(SkillCards.LIEGONG, player) && !player.hasEffect(ModItems.COOLDOWN)) longHand += 13;
                if (hasTrinket(SkillCards.WUSHENG, player) && isSha.test(mainHand)) longHand += 13;
            }
            if (hasTrinket(ModItems.CHITU, player)) level1++;
            if (hasTrinket(SkillCards.MASHU, player)) level1++;
            //如果有马术或赤兔，则等级加上额外加成数，否则为额外加成数-1
            level1 = level1 > 0 ? level1 + longHand : longHand - 1;
            if (level1 >= 0) player.addEffect(new MobEffectInstance(ModItems.REACH, 2,level1,false,false,false));

            if (hasTrinket(ModItems.DILU, player)) level2++;
            if (hasTrinket(SkillCards.FEIYING, player)) level2++;
            if (level2 > 0) player.addEffect(new MobEffectInstance(ModItems.DEFEND, 2,level2,false,false,false));

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
