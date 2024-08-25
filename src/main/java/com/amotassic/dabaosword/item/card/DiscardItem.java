package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.amotassic.dabaosword.network.ServerNetworking.openInv;
import static com.amotassic.dabaosword.network.ServerNetworking.targetInv;
import static com.amotassic.dabaosword.util.ModTools.*;

public class DiscardItem extends CardItem {
    public DiscardItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && selected && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(ModItems.REACH, 10,114,false,false,false));
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Player target && !user.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (hasItem(target, ModItems.WUXIE.get())) {
                voice(target, Sounds.WUXIE.get());
                removeItem(target, ModItems.WUXIE.get());
                jizhi(target); benxi(target);
                voice(user, Sounds.GUOHE.get());
                if (!user.isCreative()) {stack.shrink(1);}
                jizhi(user); benxi(user);
            } else {
                openInv(user, target, Component.translatable("dabaosword.discard.title", stack.getDisplayName()), targetInv(target, true, false, 1, user.getMainHandItem()));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
