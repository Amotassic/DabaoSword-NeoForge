package com.amotassic.dabaosword.item.card.equipment;

import com.amotassic.dabaosword.api.card.Card;
import com.amotassic.dabaosword.api.skill.ISkill;
import com.amotassic.dabaosword.api.skill.Skill;
import com.amotassic.dabaosword.item.card.CardItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Map;

import static com.amotassic.dabaosword.api.CardEvents.cardDiscard;
import static com.amotassic.dabaosword.util.ModTools.*;
import static net.minecraft.ChatFormatting.BOLD;

public class Equipment extends CardItem implements ISkill {
    public Equipment() {super(new Properties().stacksTo(1));}

    public final int getType() {return Card.EQUIPMENT;}

    public final boolean lockOn() {return true;}

    @Override
    public final void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        addSRTip(c(stack), tooltip); addTip(s(stack), tooltip);

        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("equipment.tip1").withStyle(BOLD));
            tooltip.add(Component.translatable("equipment.tip2").withStyle(BOLD));
        } else tooltip.add(Component.translatable("dabaosword.shift_tip", Component.keybind("key.sneak")));
    }
    public void addTip(Skill skill, List<Component> tooltip) {}
    /**防止重写错方法*/
    public final void addTip(ItemStack stack, List<Component> tooltip) {}

    @Override
    public final boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        var entity = slotContext.entity();
        if (entity instanceof Player player && !player.isCreative()) return false;
        return ISkill.super.canUnequip(slotContext, stack);
    }

    public final void curioTick(SlotContext slotContext, ItemStack stack) {
        ISkill.super.curioTick(slotContext, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            onUse(user, stack, user);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public final void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        useOrReplaceEquip(target, card);
    }

    public static void useOrReplaceEquip(LivingEntity entity, ItemStack stack) {
        var optional = CuriosApi.getCuriosInventory(entity);
        if (optional.isPresent()) {
            Map<String, ICurioStacksHandler> curios = optional.get().getCurios();
            Tuple<IDynamicStackHandler, SlotContext> firstSlot = null;

            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                IDynamicStackHandler stackHandler = entry.getValue().getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    String id = entry.getKey();
                    NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                    SlotContext slotContext = new SlotContext(id, entity, i, false, renderStates.size() > i && renderStates.get(i));

                    if (stackHandler.isItemValid(i, stack)) {
                        ItemStack present = stackHandler.getStackInSlot(i);

                        if (present.isEmpty()) {
                            stackHandler.setStackInSlot(i, stack.copy());
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
                cardDiscard(entity, d().cards(present, present.getCount(), true));
                stackHandler.setStackInSlot(i, stack.copy());
            }
        }
    }
}
