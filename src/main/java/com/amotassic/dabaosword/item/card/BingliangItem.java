package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import static com.amotassic.dabaosword.util.ModTools.*;

public class BingliangItem extends CardItem {
    public BingliangItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide) {
            if (entity instanceof Player player && hasItem(player, ModItems.WUXIE.get())) {
                NeoForge.EVENT_BUS.post(new CardUsePostListener(player, getItem(player, ModItems.WUXIE.get()), null));
                voice(player, Sounds.WUXIE.get());
            } else entity.addEffect(new MobEffectInstance(ModItems.BINGLIANG, MobEffectInstance.INFINITE_DURATION,1));
            NeoForge.EVENT_BUS.post(new CardUsePostListener(user, stack, entity));
            voice(user, Sounds.BINGLIANG.get());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
