package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.event.listener.CardCBs;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.AllRegs;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import static com.amotassic.dabaosword.util.ModTools.*;

public class PlayerInvScreenHandler extends AbstractContainerMenu {
    private final Player target;
    private final ItemStack eventStack;
    private final int cards;

    public PlayerInvScreenHandler(int syncId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(syncId, new SimpleContainer(60), (Player) inv.player.level().getEntity(buf.readInt()));
    }

    public PlayerInvScreenHandler(int syncId, Container inventory, Player target) {
        super(AllRegs.Other.PLAYER_INV_SCREEN_HANDLER.get(), syncId);
        this.target = target;
        this.cards = inventory.getItem(54).getCount();
        this.eventStack = inventory.getItem(55);
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 16 + i * 18));
            }
        }
    }

    @Override
    public void clicked(int slotIndex, int button, ClickType clickType, Player player) {
        if (slotIndex >= 0 && slotIndex < 54 && target != null) {
            var stack = getStack(player, eventStack);
            var targetStack = selected(target, slotIndex); //根据情况来判断需要选择自己的stack还是目标的stack
            var selfStack = selected(player, slotIndex);

            if (eventStack.is(ModItems.SUNSHINE_SMILE)) {
                ItemStack mainHand = player.getMainHandItem();
                if (selfStack.isEmpty()) { //如果玩家点了一个空的格子————
                    int emptySlot = player.getInventory().getFreeSlot();
                    if (emptySlot != -1) { //如果主手不为空，就把主手的物品移动到其他空格子，主手设为空
                        player.getInventory().setItem(emptySlot, mainHand.copy());
                        mainHand.setCount(0);
                    }
                } else { //如果玩家选了一个非空的格子，就交换主手和该格子的物品
                    ItemStack mainCopy = mainHand.copy(); ItemStack swapCopy = selfStack.copy();
                    if (player.getOffhandItem().equals(selfStack)) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, swapCopy);
                        player.setItemInHand(InteractionHand.OFF_HAND, mainCopy);
                    } else {
                        int swapSlot = player.getInventory().findSlotMatchingItem(selfStack);
                        player.setItemInHand(InteractionHand.MAIN_HAND, swapCopy);
                        player.getInventory().setItem(swapSlot, mainCopy);
                    }
                }
                closeGUI(player);
            }

            if (selfStack != ItemStack.EMPTY) {

                if (stack.getItem() == SkillCards.RENDE) {
                    voice(player, Sounds.RENDE);
                    target.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("give_card.tip", stack.getDisplayName(), target.getDisplayName())), false);
                    player.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("give_card.tip", stack.getDisplayName(), target.getDisplayName())), false);
                    cardMove(player, target, selfStack, 1, CardCBs.T.INV_TO_INV);
                    int cd = getCD(stack);
                    if (player.getHealth() < player.getMaxHealth() && cd == 0 && new Random().nextFloat() < 0.5) {
                        player.heal(5); voice(player, Sounds.RECOVER);
                        player.displayClientMessage(Component.translatable("recover.tip").withStyle(ChatFormatting.GREEN), true);
                        setCD(stack, 30);
                    }
                }

                if (stack.getItem() == SkillCards.YIJI) {
                    int i = getTag(stack);
                    target.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("give_card.tip", stack.getDisplayName(), target.getDisplayName())), false);
                    player.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("give_card.tip", stack.getDisplayName(), target.getDisplayName())), false);
                    cardMove(player, target, selfStack, 1, CardCBs.T.INV_TO_INV);
                    setTag(stack, i - 1);
                    if (i - 1 == 0) closeGUI(player);
                }
            }

            if (targetStack != ItemStack.EMPTY) {

                if (stack.getItem() == SkillCards.SHANZHUAN) {
                    voice(player, Sounds.SHANZHUAN);
                    if (targetStack.is(Tags.BASIC_CARD)) target.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
                    else target.addEffect(new MobEffectInstance(ModItems.BINGLIANG, MobEffectInstance.INFINITE_DURATION,1));
                    target.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("dabaosword.discard")).append(targetStack.getDisplayName()), false);
                    cardDiscard(target, targetStack, 1, slotIndex < 4);
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 8,0,false,false,true));
                    closeGUI(player);
                }

                if (stack.getItem() == SkillCards.GONGXIN) {
                    cardDiscard(target, targetStack, 1, false);
                    closeGUI(player);
                }

                if (stack.getItem() == SkillCards.ZHIHENG) {
                    int z = getTag(stack);
                    voice(player, Sounds.ZHIHENG);
                    cardDiscard(target, targetStack, 1, slotIndex < 4);
                    if (new Random().nextFloat() < 0.1) {
                        draw(player, 2);
                        player.displayClientMessage(Component.translatable("zhiheng.extra").withStyle(ChatFormatting.GREEN), true);
                    } else draw(player);
                    setTag(stack, z - 1);
                    if (z - 1 == 0) closeGUI(player);
                }

                if (stack.getItem() == ModItems.STEAL) {
                    voice(player, Sounds.SHUNSHOU);
                    target.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("dabaosword.steal")).append(targetStack.getDisplayName()), false);
                    CardCBs.T type = slotIndex < 4 ? CardCBs.T.EQUIP_TO_INV : CardCBs.T.INV_TO_INV;
                    if (isCard(targetStack)) cardMove(target, player, targetStack, 1, type);
                    //如果选择的物品是卡牌才触发事件
                    else {give(player, targetStack.copyWithCount(1)); /*顺手：复制一个物品*/
                        targetStack.shrink(1);}
                    cardUsePost(player, stack, target);
                    closeGUI(player);
                }

                if (stack.getItem() == ModItems.DISCARD) {
                    voice(player, Sounds.GUOHE);
                    target.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("dabaosword.discard")).append(targetStack.getDisplayName()), false);
                    cardDiscard(target, targetStack, 1, slotIndex < 4);
                    cardUsePost(player, stack, target);
                    closeGUI(player);
                }
            }
        }
    }

    private ItemStack selected(Player player, int slotIndex) {
        var itemStack = getSlot(slotIndex).getItem();
        if (!itemStack.isEmpty()) {
            if (slotIndex < 4) {
                var component = CuriosApi.getCuriosInventory(player);
                if(component.isPresent()) {
                    var allEquipped = component.get().getEquippedCurios();
                    for(int i = 0; i < allEquipped.getSlots(); i++) {
                        ItemStack stack = allEquipped.getStackInSlot(i);
                        if (itemStack.equals(stack)) return stack;
                    }
                }
            }
            if (slotIndex > 3 && slotIndex < 8) {
                for (ItemStack stack : player.getArmorSlots()) {
                    if (itemStack.equals(stack)) return stack;
                }
            }
            if (slotIndex >= 8) {
                NonNullList<ItemStack> inventory = player.getInventory().items;
                for (ItemStack stack : inventory) {
                    if (itemStack.equals(stack)) return stack;
                }
                if (player.getOffhandItem().equals(itemStack)) return player.getOffhandItem();
            }
        } else if (cards == 1 && slotIndex >= 8) {
            List<ItemStack> candidate = new ArrayList<>();
            NonNullList<ItemStack> inventory = player.getInventory().items;
            List<Integer> cardSlots = IntStream.range(0, inventory.size()).filter(i -> isCard(inventory.get(i))).boxed().toList();
            for (Integer slot : cardSlots) {candidate.add(inventory.get(slot));}
            ItemStack off = player.getOffhandItem();
            if (isCard(off)) candidate.add(off);
            if(!candidate.isEmpty()) {
                int index = new Random().nextInt(candidate.size());
                return candidate.get(index);
            }
        }
        return ItemStack.EMPTY;
    }

    private ItemStack getStack(Player player, ItemStack eventStack) { //获取原本的stack
        List<ItemStack> candidate = new ArrayList<>();
        var component = CuriosApi.getCuriosInventory(player);
        if(component.isPresent()) {
            var allEquipped = component.get().getEquippedCurios();
            for(int i = 0; i < allEquipped.getSlots(); i++) {
                candidate.add(allEquipped.getStackInSlot(i));
            }
        }
        candidate.addAll(player.getInventory().items);
        for (ItemStack stack : candidate) {
            if (stack.equals(eventStack)) return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {return ItemStack.EMPTY;}

    @Override
    public boolean stillValid(Player player) {
        return !player.hasEffect(ModItems.COOLDOWN2) || (player.hasEffect(ModItems.COOLDOWN2) && Objects.requireNonNull(player.getEffect(ModItems.COOLDOWN2)).getAmplifier() != 2);
    }

    private void closeGUI(Player player) {
        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 1,2,false,false,false));
    }
}
