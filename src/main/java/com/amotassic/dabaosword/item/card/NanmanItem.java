package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import static com.amotassic.dabaosword.util.ModTools.*;

public class NanmanItem extends CardItem {
    public NanmanItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            Component a = Component.translatable("nanman.dog1");
            Component b = Component.translatable("nanman.dog2");
            Component c = Component.translatable("nanman.dog3");
            BlockPos blockPos = user.getOnPos();
            //召唤3条狗
            Wolf wolf1 = new Wolf(EntityType.WOLF, world);
            wolf1.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null);
            wolf1.tame(user);wolf1.setTame(true, true);world.addFreshEntity(wolf1);wolf1.setCustomName(a);
            wolf1.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20 * 20,0,false,false,false));
            wolf1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 20,1,false,false,false));
            wolf1.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 20,1,false,false,false));

            Wolf wolf2 = new Wolf(EntityType.WOLF, world);
            wolf2.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null);
            wolf2.tame(user);wolf2.setTame(true, true);world.addFreshEntity(wolf2);wolf2.setCustomName(b);
            wolf2.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20 * 20,0,false,false,false));
            wolf2.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 20,1,false,false,false));
            wolf2.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 20,1,false,false,false));

            Wolf wolf3 = new Wolf(EntityType.WOLF, world);
            wolf3.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null);
            wolf3.tame(user);wolf3.setTame(true, true);world.addFreshEntity(wolf3);wolf3.setCustomName(c);
            wolf3.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20 * 20,0,false,false,false));
            wolf3.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 20,1,false,false,false));
            wolf3.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 20,1,false,false,false));

            if (!user.isCreative()) {user.getItemInHand(hand).shrink(1);}
            jizhi(user); benxi(user);
            voice(user, Sounds.NANMAN.get());
            return InteractionResultHolder.success(user.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(user.getItemInHand(hand));
    }
}
