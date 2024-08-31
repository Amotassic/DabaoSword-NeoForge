package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;

import static com.amotassic.dabaosword.util.ModTools.*;

public class TiesuoItem extends CardItem {
    public TiesuoItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && !entity.isCurrentlyGlowing() && hand == InteractionHand.MAIN_HAND) {
            AABB box = user.getBoundingBox().expandTowards(user.getViewVector(1.0F).scale(10));
            for (LivingEntity nearbyEntity : user.level().getEntitiesOfClass(LivingEntity.class, box, nearbyEntity -> !nearbyEntity.isCurrentlyGlowing())) {
                nearbyEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, MobEffectInstance.INFINITE_DURATION, 0, false, true,false));
            }
            NeoForge.EVENT_BUS.post(new CardUsePostListener(user, stack, entity));
            voice(user, Sounds.TIESUO.get());
            user.removeEffect(MobEffects.GLOWING);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (!entity.level().isClientSide && entity.getOffhandItem().getItem() == Items.KNOWLEDGE_BOOK) {
            if (entity instanceof Player && !((Player) entity).isCreative()) {stack.shrink(1);}
        }
        entity.removeEffect(MobEffects.GLOWING);
    }
}
