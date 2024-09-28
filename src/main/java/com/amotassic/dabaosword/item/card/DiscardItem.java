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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (entity instanceof Player target) {
                if (hasItem(target, ModItems.WUXIE.get())) {
                    cardUsePost(target, getItem(target, ModItems.WUXIE.get()), null);
                    voice(target, Sounds.WUXIE);
                    cardUsePost(user, stack, entity);
                    voice(user, Sounds.GUOHE);
                } else {
                    openInv(user, target, Component.translatable("dabaosword.discard.title", stack.getDisplayName()), targetInv(target, true, false, 1, user.getMainHandItem()));
                }
            } else {
                List<ItemStack> stacks = new ArrayList<>();
                if (isCard(entity.getMainHandItem())) stacks.add(entity.getMainHandItem());
                if (isCard(entity.getOffhandItem())) stacks.add(entity.getOffhandItem());
                if (!stacks.isEmpty()) {
                    ItemStack chosen = stacks.get(new Random().nextInt(stacks.size()));
                    voice(user, Sounds.GUOHE);
                    chosen.shrink(1);
                    cardUsePost(user, stack, entity);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
