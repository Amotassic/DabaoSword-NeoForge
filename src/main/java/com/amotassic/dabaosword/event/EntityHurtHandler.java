package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.Skill;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModifyDamage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EntityHurtHandler {

    private static void trySave(LivingEntity entity, float amount) {
        for (int i = 0; i < 1145; i++) {
            if (entity.isAlive()) return;
            if (hasCard(entity, canSaveDying)) {
                ItemStack stack = getCard(entity, canSaveDying).getB();
                cardUsePost(entity, stack, entity);
                entity.setHealth(entity.getHealth() - amount + 5); amount -= 5;
            }
        }
    }

    @SubscribeEvent
    public static void EntityHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity(); DamageSource source = event.getSource();
        float amount = event.getNewDamage();

        if (entity.level() instanceof ServerLevel world) {

            for (var stack : allTrinkets(entity)) { //受伤害后触发，优先级高
                if (stack.getItem() instanceof Skill skill && canTrigger(stack, entity)) skill.onHurt(stack, entity, source, amount);
            }

            trySave(entity, amount);

            if (source.getEntity() instanceof LivingEntity living) {
                if (living.getTags().contains("px")) entity.invulnerableTime = 0;
            }

            //监听事件：若玩家杀死敌对生物，有概率摸牌，若杀死玩家，摸两张牌
            if (source.getEntity() instanceof Player player && entity.getHealth() <= 0) {
                if (entity instanceof Monster) {
                    if (new Random().nextFloat() < 0.1) {
                        draw(player);
                        player.displayClientMessage(Component.translatable("dabaosword.draw.monster"),true);
                    }
                }
                if (entity instanceof Player) {
                    draw(player, 2);
                    player.displayClientMessage(Component.translatable("dabaosword.draw.player"),true);
                }
            }

            if (source.getDirectEntity() instanceof LivingEntity SE) {
                //杀的相关结算
                if (shouldSha(SE) && entity.isAlive()) {
                    ItemStack stack = isSha.test(SE.getMainHandItem()) ? SE.getMainHandItem() : getItem(SE, isSha);
                    ItemStack sha = stack.copy();
                    //处理铁索连环的效果 铁索传导过去的伤害会触发2次加伤，这符合三国杀的逻辑，所以不改了
                    if (cardUsePre(SE, stack, entity) && entity.isCurrentlyGlowing()) {
                        if (!sha.is(ModItems.SHA)) entity.removeEffect(MobEffects.GLOWING);
                        AABB box = new AABB(SE.getOnPos()).inflate(20); // 检测范围，根据需要修改
                        for (LivingEntity near : world.getEntitiesOfClass(LivingEntity.class, box, e -> e.isCurrentlyGlowing() && e != entity)) {
                            if (sha.is(ModItems.FIRE_SHA)) {
                                near.removeEffect(MobEffects.GLOWING); near.hurt(source, amount);
                                near.invulnerableTime = 0; near.setRemainingFireTicks(100);
                            }
                            if (sha.is(ModItems.THUNDER_SHA)) {
                                near.removeEffect(MobEffects.GLOWING); near.hurt(source, amount);
                                near.invulnerableTime = 0; near.hurt(SE.damageSources().magic(), 5);
                                LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                                if (lightningEntity != null) {
                                    lightningEntity.moveTo(near.getX(), near.getY(), near.getZ());
                                    lightningEntity.setVisualOnly(true);
                                    world.addFreshEntity(lightningEntity);
                                }
                            }
                        }
                    }
                }
            }

            if (source.getDirectEntity() instanceof LivingEntity living) { //在近战攻击造成伤害后触发
                for (var stack : allTrinkets(living)) {
                    if (stack.getItem() instanceof Skill skill && canTrigger(stack, living)) skill.postAttack(stack, entity, living, amount);
                }
            }

            if (source.getEntity() instanceof LivingEntity living) { //只要攻击造成伤害即可触发，包括远程
                for (var stack : allTrinkets(living)) {
                    if (stack.getItem() instanceof Skill skill && canTrigger(stack, living)) skill.postDamage(stack, entity, living, amount);
                }
            }

        }
    }

    static boolean shouldSha(LivingEntity entity) {
        return hasItem(entity, isSha) && !entity.getTags().contains("sha") && !entity.getTags().contains("juedou");
    }

    @SubscribeEvent
    public static void cancel(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getAmount();
        if (ModifyDamage.shouldCancel(entity, source, amount)) event.setCanceled(true);
    }
}
