package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Gamerule;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
            long time = world.getGameTime();
            if (time % 2 == 0) {
                String[] tags = {"sha", "juedou", "nanman", "wanjian", "benxi"};
                for (var tag : tags) entity.getTags().remove(tag);
            }
            if (time % 20 == 0) {
                //阳光开朗的笑容的效果：让与其对视的实体冻伤（阴风阵阵）
                if (entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.SUNSHINE_SMILE)) for (var e : world.getEntitiesOfClass(LivingEntity.class, new AABB(entity.getOnPos()).inflate(20), e -> e != entity && isEyeContact(e, entity, 25))) e.setTicksFrozen(240);
            }
            if (time % 200 == 0) {
                entity.getTags().remove("seen_skill_tip");
            }

            //处理所有加触及距离和近战防御距离的效果
            int level1 = 0; int level2 = 0;
            ItemStack mainHand = entity.getMainHandItem();
            if (mainHand.is(ModItems.DISCARD) || mainHand.is(ModItems.JUEDOU)) level1 += 114;
            for (var skill : getSkillsMayUse(entity)) {
                level1 += skill.item.getExtraReach(entity, skill);
                level2 += skill.item.getDefend(entity, skill);
            }
            if (level1 > 0) entity.addEffect(new MobEffectInstance(ModItems.REACH, 2,level1 - 1,false,false,false));
            if (level2 > 0) entity.addEffect(new MobEffectInstance(ModItems.DEFEND, 2,level2 - 1,false,false,false));

            Player closest = world.getNearestPlayer(entity, 5);
            ItemStack stack;
            if (closest != null && !(stack = trinketItem(ModItems.FANGTIAN, closest)).isEmpty() && entity.isAlive()) {
                if (s(stack).getCD() > 15 && closest.swingTime == 1) {
                    //给玩家本人一个极短的无敌效果，以防止被误伤
                    closest.addEffect(new MobEffectInstance(ModItems.INVULNERABLE,2,0,false,false,false));
                    float i = (float) closest.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    entity.hurt(closest.damageSources().playerAttack(closest), i);
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
                        for (var skill : getSkillsMayUse(player)) {
                            int i = skill.item.onDrawPhase(player, skill);
                            if (i <= -114) {draw = 0; break;}
                            draw += i;
                        }
                        if (draw > 0) draw(player, draw);
                    }
                }
            }

            if (time % 2 == 0) decreaseAttackRange(player);

        }
    }

    private static void decreaseAttackRange(LivingEntity entity) {
        AABB box = new AABB(entity.getOnPos()).inflate(20);
        for (LivingEntity target : entity.level().getEntitiesOfClass(LivingEntity.class, box, living -> living != entity && living.hasEffect(ModItems.DEFEND) && isLooking(entity, living))) {
            //实现沈佳宜的效果：若玩家看到的玩家有近战防御效果，则给当前玩家攻击范围缩短效果
            int amplifier = Objects.requireNonNull(target.getEffect(ModItems.DEFEND)).getAmplifier();
            entity.addEffect(new MobEffectInstance(ModItems.DEFENDED, 2, amplifier,false,false,true));
        }
    }

    public static boolean isLooking(LivingEntity entity, Entity target) {
        Vec3 playerPos = entity.getEyePosition();
        Vec3 lookVec = entity.getViewVector(1.0F);
        AABB targetBox = target.getBoundingBox();
        // 进行射线与碰撞箱的相交检测
        return targetBox.clip(playerPos, playerPos.add(lookVec.scale(100.0))).isPresent();
    }

    public static boolean isEyeContact(Entity entity1, Entity entity2, float angle) {
        double MAX_ANGLE = Math.toRadians(angle);
        Vec3 pos1 = entity1.position(); Vec3 pos2 = entity2.position();
        // 计算从生物 1 到生物 2 的向量
        Vec3 d = pos2.subtract(pos1);
        // 获取生物的视线方向
        Vec3 v1 = entity1.getViewVector(1.0F); Vec3 v2 = entity2.getViewVector(1.0F);
        // 计算夹角
        double theta1 = Math.acos(v1.dot(d.normalize()));
        double theta2 = Math.acos(v2.dot(d.normalize().reverse()));
        // 判断夹角是否在允许范围内
        return theta1 <= MAX_ANGLE && theta2 <= MAX_ANGLE;
    }
}
