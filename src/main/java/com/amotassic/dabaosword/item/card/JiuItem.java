package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

import static com.amotassic.dabaosword.util.ModTools.voice;

public class JiuItem extends CardItem {
    public JiuItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!user.hasEffect(MobEffects.DAMAGE_BOOST) && !world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 10, 0));
            NeoForge.EVENT_BUS.post(new CardUsePostListener(user, user.getItemInHand(hand), user));
            voice(user, Sounds.JIU.get());
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
