package com.amotassic.dabaosword.item.equipment;

import com.amotassic.dabaosword.item.ModItems;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;

public class EquipmentItem extends Item implements ICurioItem {
    public EquipmentItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {

        if (stack.getItem() == ModItems.GUDING_WEAPON.get()) {
            tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        if (stack.getItem() == ModItems.FANGTIAN.get()) {
            tooltip.add(Component.translatable("item.dabaosword.fangtian.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.fangtian.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        if (stack.getItem() == ModItems.HANBING.get()) {
            tooltip.add(Component.translatable("item.dabaosword.hanbing.tooltip").withStyle(ChatFormatting.AQUA));
        }

        if (stack.getItem() == ModItems.QINGGANG.get()) {
            tooltip.add(Component.translatable("item.dabaosword.qinggang.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.qinggang.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        if (stack.getItem() == ModItems.QINGLONG.get()) {
            tooltip.add(Component.translatable("item.dabaosword.qinglong.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.qinglong.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        if (stack.getItem() == ModItems.BAGUA.get()) {
            tooltip.add(Component.translatable("item.dabaosword.bagua.tooltip"));
        }

        if (stack.getItem() == ModItems.BAIYIN.get()) {
            tooltip.add(Component.translatable("item.dabaosword.baiyin.tooltip"));
        }

        if (stack.getItem() == ModItems.RATTAN_ARMOR.get()) {
            tooltip.add(Component.translatable("item.dabaosword.rattanarmor.tooltip"));
        }

        if (stack.getItem() == ModItems.CHITU.get()) {
            tooltip.add(Component.translatable("item.dabaosword.chitu.tooltip"));
        }

        if (stack.getItem() == ModItems.DILU.get()) {
            tooltip.add(Component.translatable("item.dabaosword.dilu.tooltip"));
        }

        if (stack.getItem() == ModItems.CARD_PILE.get()) {
            tooltip.add(Component.translatable("item.dabaosword.card_pile.tooltip"));
        }

        if (stack.getItem() != ModItems.CARD_PILE.get()) {
            if(Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("equipment.tip1").withStyle(ChatFormatting.BOLD));
                tooltip.add(Component.translatable("equipment.tip2").withStyle(ChatFormatting.BOLD));
            } else tooltip.add(Component.translatable("dabaosword.shifttooltip"));
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world) {
            world.players().forEach(player -> player.displayClientMessage(
                    Component.literal(slotContext.entity().getScoreboardName()).append(Component.literal(" equipped ").append(stack.getDisplayName())),false
            ));
        }
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) {
            if (stack.getItem() != ModItems.CARD_PILE.get() && !EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {//给装备上绑定诅咒
                var e = enchantment(entity, Enchantments.BINDING_CURSE);
                stack.enchant(e, 1);
            }
        }
        ICurioItem.super.curioTick(slotContext, stack);
    }

    public Holder<Enchantment> enchantment(Entity entity, ResourceKey<Enchantment> key) {
        var enchantments = entity.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).holders().toList();
        for (var enchant : enchantments) {
            if (enchant.unwrapKey().isPresent() && enchant.unwrapKey().get() == key) return enchant;
        }
        return null;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        int v = armorTrinket(stack) ? 5 : 0;
        Multimap<Holder<Attribute>, AttributeModifier> multimap = LinkedHashMultimap.create();
        AttributeModifier Modifier = new AttributeModifier(id, v, AttributeModifier.Operation.ADD_VALUE);
        multimap.put(Attributes.ARMOR, Modifier);
        return multimap;
    }

    private boolean armorTrinket(ItemStack stack) {
        return stack.getItem() == ModItems.BAIYIN.get() || stack.getItem() == ModItems.RATTAN_ARMOR.get() || stack.getItem() == ModItems.BAGUA.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        equipItem(player, stack);
        return InteractionResultHolder.success(stack);
    }

    public static void equipItem(Player player, ItemStack stack) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            Tuple<IDynamicStackHandler, SlotContext> firstSlot = null;

            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                IDynamicStackHandler stackHandler = entry.getValue().getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    String id = entry.getKey();
                    NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                    SlotContext slotContext = new SlotContext(id, player, i, false, renderStates.size() > i && renderStates.get(i));

                    if (stackHandler.isItemValid(i, stack)) {
                        ItemStack present = stackHandler.getStackInSlot(i);

                        if (present.isEmpty()) {
                            stackHandler.setStackInSlot(i, stack.copy());
                            if (!player.isCreative()) stack.setCount(0);
                            return;
                        } else if (firstSlot == null) firstSlot = new Tuple<>(stackHandler, slotContext);
                    }
                }
            }

            if (firstSlot != null) {
                IDynamicStackHandler stackHandler = firstSlot.getA();
                SlotContext slotContext = firstSlot.getB();
                int i = slotContext.index();
                ItemStack present = stackHandler.getStackInSlot(i);
                if (present.getItem() != stack.getItem()) {
                    stackHandler.setStackInSlot(i, stack.copy());
                    if (!player.isCreative()) stack.setCount(0);
                }
            }
        });
    }
}
