package com.amotassic.dabaosword.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

import static com.amotassic.dabaosword.api.event.CardEvents.cardUsePre;

public class NanmanItem extends CardItem {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (cardUsePre(user, user.getMainHandItem(), null)) return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {
        Level world = user.level();
        world.players().forEach(player -> {
            if (player != user) summonRavager(user, player);
        });
        AABB box = new AABB(user.getOnPos()).inflate(10);
        Predicate<LivingEntity> p = e -> !(e instanceof Player) && e != user && e.isAlive() && !e.getTags().contains("b");
        for (LivingEntity near : world.getEntitiesOfClass(LivingEntity.class, box, p)) {
            summonRavager(user, near);
        }
    }

    private void summonRavager(LivingEntity user, LivingEntity entity) {
        Level world = entity.level();
        Ravager ravager = new Ravager(EntityType.RAVAGER, world);
        ravager.setCustomName(Component.nullToEmpty(String.valueOf(user.getId())));
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
}
