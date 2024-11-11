package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.Skill;
import com.amotassic.dabaosword.api.event.CardCBs;
import com.amotassic.dabaosword.effect.ShandianEffect;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EntityHurtHandler {

    private static void trySave(LivingEntity entity, float amount) {
        for (int i = 0; i < 114; i++) {
            if (entity.isAlive()) return;
            if (hasCard(entity, canSaveDying)) {
                ItemStack stack = getCard(entity, canSaveDying).getB();
                cardUsePost(entity, stack, entity);
                entity.setHealth(entity.getHealth() - amount + 5); amount -= 5;
            }
        }
    }

    private static void tiesuoTrigger(LivingEntity entity, DamageSource source, float amount) {
        Level world = entity.level();
        if (entity.isCurrentlyGlowing() && source.is(Tags.TRIGGER_TIESUO)) {
            entity.removeEffect(MobEffects.GLOWING);
            AABB box = new AABB(entity.getOnPos()).inflate(20);
            for (LivingEntity near : world.getEntitiesOfClass(LivingEntity.class, box, e -> e != entity && e.isCurrentlyGlowing())) {
                near.removeEffect(MobEffects.GLOWING);
                near.hurt(source, amount);
                if (source.is(DamageTypeTags.IS_FREEZING)) near.setTicksFrozen(entity.getTicksFrozen());
                if (source.is(DamageTypeTags.IS_FIRE)) {
                    int fireTicks = entity.getRemainingFireTicks() / 20;
                    int fireTime = fireTicks == 0 ? 6 : fireTicks;
                    near.setRemainingFireTicks(fireTime * 20);
                }
                if (source.is(DamageTypeTags.IS_LIGHTNING)) ShandianEffect.summonLightning(near, true, false);
            }
        }
    }

    @SubscribeEvent
    public static void EntityHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity(); DamageSource source = event.getSource();
        float amount = event.getNewDamage();

        if (entity.level() instanceof ServerLevel) {

            tiesuoTrigger(entity, source, amount);

            for (var stack : allTrinkets(entity)) { //受伤害后触发，优先级高
                if (stack.getItem() instanceof Skill skill && canTrigger(stack, entity)) skill.onHurt(stack, entity, source, amount);
            }

            trySave(entity, amount);

            if (isNanman(source)) hurtBy(entity, source, ModItems.NANMAN);
            if (isWanjian(source)) hurtBy(entity, source, ModItems.WANJIAN);
            if (isHuogong(source)) hurtBy(entity, source, ModItems.FIRE_ATTACK);
            if (isShandian(source)) hurtBy(entity, source, ModItems.SHANDIAN_ITEM);

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

    @SubscribeEvent
    public static void canHurtByCard(CardCBs.CanHurtByCard event) {
        LivingEntity entity = event.getEntity(); ItemStack card = event.card;

        if (isSha.test(card) && isBlackCard.test(card) && hasTrinket(ModItems.RENWANG, entity)) {
            voice(entity, Sounds.RENWANG); event.setCanceled(true); return;
        }
        if (card.is(ModItems.NANMAN)) {
            if (hasTrinket(SkillCards.WEIMU, entity)) {voice(entity, Sounds.WEIMU); event.setCanceled(true); return;}
        }
        event.setCanceled(canTriggerTengjia(entity, card));
    }

    private static boolean canTriggerTengjia(LivingEntity entity, ItemStack card) {
        if (card.is(ModItems.WANJIAN) || card.is(ModItems.NANMAN) || card.is(ModItems.SHA)) {
            if (hasTrinket(ModItems.RATTAN_ARMOR, entity)) {
                voice(entity, Sounds.TENGJIA1);
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void hurtByCard(CardCBs.HurtByCard event) {
        LivingEntity entity = event.getEntity(); ItemStack card = event.card;

        ItemStack jianxiong = trinketItem(SkillCards.JIANXIONG, entity);
        if (!jianxiong.isEmpty() && getCD(jianxiong) == 0) {
            voice(entity, jianxiong);
            setCD(jianxiong, 15);
            if (entity instanceof Player player) give(player, card.copyWithCount(1));
        }
    }
}
