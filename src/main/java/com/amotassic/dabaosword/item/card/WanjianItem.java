package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Set;

import static com.amotassic.dabaosword.api.CardEvents.hurtByCard;

public class WanjianItem extends CardItem.Armoury {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world instanceof ServerLevel sw && hand == InteractionHand.MAIN_HAND) {

            Set<LivingEntity> targets = new HashSet<>(sw.players());
            AABB box = new AABB(user.getOnPos()).inflate(10);
            targets.addAll(world.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive));
            var e = ModTools.getClosestEntity(user, LivingEntity.class, 10, l -> !(l instanceof Player));
            if (e != null) e.addTag("wanjian");
            targets.remove(user);

            user.addTag("sha"); //防止触发杀
            onUse(user, user.getMainHandItem(), targets.toArray(new LivingEntity[0]));
            return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity entity) {
        DamageSource source = user.damageSources().mobAttack(user);
        if (entity.hurt(source, 6)) hurtByCard(entity, card);
        if (entity instanceof Player || entity.getTags().contains("wanjian")) entity.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 20, 1, false, false));
    }

    @Override public boolean askForWuxie() {return true;}
}
