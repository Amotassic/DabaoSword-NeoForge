package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.event.listener.CardMoveListener;
import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;

import static com.amotassic.dabaosword.util.ModTools.*;

public class JiedaoItem extends CardItem {
    public JiedaoItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        ItemStack stack1 = entity.getMainHandItem();
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && !stack1.isEmpty()) {
            if (entity instanceof Player player && hasItem(player, ModItems.WUXIE.get())) {
                NeoForge.EVENT_BUS.post(new CardUsePostListener(player, getItem(player, ModItems.WUXIE.get()), null));
                voice(player, Sounds.WUXIE.get());
            } else {
                if (isCard(stack1)) NeoForge.EVENT_BUS.post(new CardMoveListener(entity, user, stack1, stack1.getCount(), CardMoveListener.Type.INV_TO_INV));
                else give(user, stack1.copy()); stack1.setCount(0);
            }
            voice(user, Sounds.JIEDAO.get());
            NeoForge.EVENT_BUS.post(new CardUsePostListener(user, stack, entity));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
