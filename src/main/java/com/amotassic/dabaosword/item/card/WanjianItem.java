package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

import static com.amotassic.dabaosword.util.ModTools.*;

public class WanjianItem extends CardItem {
    public WanjianItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            user.addTag("wanjian");
            user.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 15,1,false,false,false));
            NeoForge.EVENT_BUS.post(new CardUsePostListener(user, user.getItemInHand(hand), user));
            voice(user, Sounds.WANJIAN.get());
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
