package com.amotassic.dabaosword.item.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.amotassic.dabaosword.command.InfoCommand.openFullInv;
import static com.amotassic.dabaosword.util.ModTools.getClosestEntity;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class LetMeCCItem extends Item {
    public LetMeCCItem() {super(new Item.Properties().stacksTo(1));}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.let_me_cc.tooltip"));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand usedHand) {
        if (!user.level().isClientSide && usedHand == InteractionHand.MAIN_HAND) {
            voice(user, this, 1);
            openFullInv(user, entity, true);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, user, entity, usedHand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand usedHand) {
        if (!world.isClientSide && usedHand == InteractionHand.MAIN_HAND) {
            if (!user.isShiftKeyDown()) {
                LivingEntity closest = getClosestEntity(user, LivingEntity.class, 10, LivingEntity::isAlive);
                if (closest != null) {
                    voice(user, this, 1);
                    openFullInv(user, closest, true);
                    return InteractionResultHolder.success(user.getItemInHand(usedHand));
                }
            } else {
                voice(user, this, 1);
                openFullInv(user, user, true);
                return InteractionResultHolder.success(user.getItemInHand(usedHand));
            }
        }
        return super.use(world, user, usedHand);
    }
}
