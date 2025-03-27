package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static com.amotassic.dabaosword.api.CardEvents.hurtByCard;

public class NanmanItem extends CardItem.Armoury {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world instanceof ServerLevel sw && hand == InteractionHand.MAIN_HAND) {

            Set<LivingEntity> targets = new HashSet<>(sw.players());
            AABB box = new AABB(user.getOnPos()).inflate(10);
            Predicate<LivingEntity> p = e -> e.isAlive() && !e.getTags().contains("b");
            targets.addAll(world.getEntitiesOfClass(LivingEntity.class, box, p));
            targets.remove(user);

            user.addTag("nanman"); //防止触发杀
            onUse(user, user.getMainHandItem(), targets.toArray(new LivingEntity[0]));
            return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity entity) {
        DamageSource source = user.damageSources().mobAttack(user);
        //防止触发闪
        entity.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 2, 0, false, false));
        if (entity.hurt(source, 6)) hurtByCard(entity, card);
        summonRavager(entity);
    }

    private void summonRavager(LivingEntity entity) {
        Level world = entity.level();
        Ravager ravager = new Ravager(EntityType.RAVAGER, world);
        ravager.setCustomName(Component.nullToEmpty(String.valueOf(entity.getId())));
        world.addFreshEntity(ravager);
        ravager.setInvulnerable(true);
        ravager.addTag("a"); ravager.addTag("b");
        ravager.moveTo(getBlockInFront(entity, 3));
    }

    public Vec3 getBlockInFront(LivingEntity entity, int distance) {
        Vec3 pos = entity.position();
        Vec3 playerDirection = entity.getViewVector(1.0F);
        double x = pos.x + playerDirection.x * distance;
        double z = pos.z + playerDirection.z * distance;
        return new Vec3(x, pos.y, z);
    }

    @Override public boolean askForWuxie() {return true;}
}
