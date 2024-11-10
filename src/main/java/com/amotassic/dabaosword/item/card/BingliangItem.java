package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.amotassic.dabaosword.util.ModTools.cardUsePre;

public class BingliangItem extends CardItem {
    public BingliangItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (cardUsePre(user, user.getMainHandItem(), entity)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    //对生物使用后给予其兵粮寸断效果
    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {
        target.addEffect(new MobEffectInstance(ModItems.BINGLIANG, -1,1));
    }
}
