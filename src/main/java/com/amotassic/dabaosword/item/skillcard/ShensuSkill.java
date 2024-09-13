package com.amotassic.dabaosword.item.skillcard;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import static com.amotassic.dabaosword.util.ModTools.noTieji;

public class ShensuSkill extends SkillItem {
    public ShensuSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> multimap = LinkedHashMultimap.create();
        LivingEntity entity = slotContext.entity();
        double d = 0;
        if (entity instanceof Player player && noTieji(player)) d = Math.min(getEmptySlots(player), 20d) / 40; //当空余20格时，获得最大加成0.5
        AttributeModifier modifier = new AttributeModifier(id, d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        multimap.put(Attributes.MOVEMENT_SPEED, modifier);
        return multimap;
    }

    private int getEmptySlots(Player player) {
        int i = 0;
        for (var slot : player.getInventory().items) {if (slot.isEmpty()) i++;}
        return i;
    }
}
