package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.amotassic.dabaosword.util.ModTools.*;

public class JiedaoItem extends CardItem {
    public JiedaoItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        ItemStack stack1 = entity.getMainHandItem();
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && !stack1.isEmpty()) {
            if (entity instanceof Player player) {
                if (hasItem(player, ModItems.WUXIE.get())) {
                    voice(player, Sounds.WUXIE.get());
                    removeItem(player, ModItems.WUXIE.get());
                    jizhi(player); benxi(player);
                } else {user.addItem(stack1.copy()); stack1.setCount(0);}
            } else {user.addItem(stack1.copy()); stack1.setCount(0);}
            voice(user, Sounds.JIEDAO.get());
            if (!user.isCreative()) {stack.shrink(1);}
            jizhi(user); benxi(user);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
