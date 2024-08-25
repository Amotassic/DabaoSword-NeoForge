package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Objects;

import static com.amotassic.dabaosword.util.ModTools.noTieji;

public class LiegongSkill extends SkillItem {
    public LiegongSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide && noTieji(entity)) {
            if (!entity.hasEffect(ModItems.COOLDOWN)) gainReach(entity,13);
            else gainReach(entity,0);
        }
        super.curioTick(slotContext, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) gainReach(entity,0);
    }

    private void gainReach(LivingEntity entity, int value) {
        AttributeModifier Modifier = new AttributeModifier(ResourceLocation.withDefaultNamespace("range_13"), value, AttributeModifier.Operation.ADD_VALUE);
        Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.ENTITY_INTERACTION_RANGE)).addOrUpdateTransientModifier(Modifier);
        Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.BLOCK_INTERACTION_RANGE)).addOrUpdateTransientModifier(Modifier);
    }
}
