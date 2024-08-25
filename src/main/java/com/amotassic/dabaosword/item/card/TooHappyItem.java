package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.amotassic.dabaosword.util.ModTools.*;

public class TooHappyItem extends CardItem {
    public TooHappyItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide) {
            if (entity instanceof Player player) {
                if (hasItem(player, ModItems.WUXIE.get())) {
                    removeItem(player, ModItems.WUXIE.get());
                    jizhi(player); benxi(player);
                    voice(player, Sounds.WUXIE.get());
                } else {player.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));}
            } else {entity.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 15));}
            if (!user.isCreative()) {stack.shrink(1);}
            jizhi(user); benxi(user);
            voice(user, Sounds.LEBU.get());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
