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

import static com.amotassic.dabaosword.util.ModTools.cardUsePost;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class NanmanItem extends CardItem {
    public NanmanItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            Component[] names = {
                    Component.translatable("nanman.dog1"),
                    Component.translatable("nanman.dog2"),
                    Component.translatable("nanman.dog3")
            };
            for (Component name : names) {summonDog(world, user, name);}

            cardUsePost(user, user.getItemInHand(hand), null);
            voice(user, Sounds.NANMAN);
            return InteractionResultHolder.success(user.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(user.getItemInHand(hand));
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
