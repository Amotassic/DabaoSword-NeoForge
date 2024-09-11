package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Objects;

import static com.amotassic.dabaosword.util.ModTools.voice;

public class LuoyiSkill extends SkillItem {
    public LuoyiSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) gainStrength(entity, getEmptyArmorSlot(entity) + 1);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (!slotContext.entity().level().isClientSide) gainStrength(slotContext.entity(), 0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && !user.isShiftKeyDown()) voice(user, Sounds.LUOYI.get());
        return super.use(world, user, hand);
    }

    private void gainStrength(LivingEntity entity, int value) {
        AttributeModifier Modifier = new AttributeModifier(ResourceLocation.parse("attack_damage"), value, AttributeModifier.Operation.ADD_VALUE);
        Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).addOrUpdateTransientModifier(Modifier);
    }

    private int getEmptyArmorSlot(LivingEntity entity) {
        int i = 0;
        for (var slot : entity.getArmorSlots()) {if (slot.isEmpty()) i++;}
        return i;
    }
}
