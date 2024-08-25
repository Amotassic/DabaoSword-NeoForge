package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
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

import static com.amotassic.dabaosword.util.ModTools.*;

public class GongaoSkill extends SkillItem {
    public GongaoSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) {
            int extraHP = getTag(stack);

            if (entity.level().getGameTime() % 600 == 0) { // 每30s触发扣体力上限
                if (entity instanceof Player player) {
                    if (extraHP >= 5 && !player.isCreative() && !player.isSpectator()) {
                        give(player, new ItemStack(ModItems.GAIN_CARD.get(), 2));
                        stack.set(ModItems.TAGS, extraHP - 5);
                        voice(player, Sounds.WEIZHONG.get());
                    }
                }
            }
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> multimap = LinkedHashMultimap.create();
        AttributeModifier Modifier = new AttributeModifier(id, getTag(stack), AttributeModifier.Operation.ADD_VALUE);
        multimap.put(Attributes.MAX_HEALTH, Modifier);
        return multimap;
    }
}
