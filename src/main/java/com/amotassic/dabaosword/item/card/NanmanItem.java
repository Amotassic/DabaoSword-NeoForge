package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import static com.amotassic.dabaosword.util.ModTools.cardUsePre;

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
        Component[] names = {
                Component.translatable("nanman.dog1"),
                Component.translatable("nanman.dog2"),
                Component.translatable("nanman.dog3")
        };
        if (user instanceof Player player) for (Component name : names) {summonDog(user.level(), player, name);}
    }

    private void summonDog(Level level, Player player, Component name) {
        BlockPos blockPos = player.getOnPos();
        Wolf wolf1 = new Wolf(EntityType.WOLF, level);
        wolf1.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null);
        wolf1.tame(player);
        wolf1.setTame(true, true);
        level.addFreshEntity(wolf1);wolf1.setCustomName(name);
        wolf1.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20 * 20,0,false,false,false));
        wolf1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 20,1,false,false,false));
        wolf1.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 20,1,false,false,false));
    }
}
