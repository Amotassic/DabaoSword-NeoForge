package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.event.listener.ActiveSkillListener;
import com.amotassic.dabaosword.event.listener.CardMoveListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.equipment.Equipment;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import com.amotassic.dabaosword.ui.SimpleMenuHandler;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ActiveSkillHandler {

    @SubscribeEvent
    public static void ActiveSkill(ActiveSkillListener event) {
        Player user = event.getEntity();
        ItemStack stack = event.getStack();
        Player target = event.getTarget();
        if (user.level() instanceof ServerLevel && noTieji(user)) {

            if (stack.getItem() == SkillCards.ZHIHENG) {
                int z = getTag(stack);
                if (z > 0) openInv(user, user, Component.translatable("zhiheng.title"), targetInv(user, true, false, 2, stack));
                else user.displayClientMessage(Component.translatable("zhiheng.fail").withStyle(ChatFormatting.RED), true);
            }

            if (stack.getItem() == SkillCards.LUOSHEN) {
                int cd = getCD(stack);
                if (cd > 0) user.displayClientMessage(Component.translatable("dabaosword.cooldown").withStyle(ChatFormatting.RED), true);
                else {
                    voice(user, Sounds.LUOSHEN);
                    if (new Random().nextFloat() < 0.5) {
                        draw(user);
                        user.displayClientMessage(Component.translatable("item.dabaosword.luoshen.win").withStyle(ChatFormatting.GREEN), true);
                    } else {
                        setCD(stack, 30);
                        user.displayClientMessage(Component.translatable("item.dabaosword.luoshen.lose").withStyle(ChatFormatting.RED), true);
                    }
                }
            }

            if (stack.getItem() == SkillCards.KUROU) {
                if (user.getHealth() + 5 * count(user, Tags.RECOVER) > 4.99) {
                    draw(user, 2);
                    if (!user.isCreative()) {
                        user.invulnerableTime = 0;
                        user.hurt(user.damageSources().genericKill(), 4.99f);
                    }
                    voice(user, Sounds.KUROU);
                } else {user.displayClientMessage(Component.translatable("item.dabaosword.kurou.tip").withStyle(ChatFormatting.RED), true);}
            }

            if (stack.getItem() == SkillCards.QICE) {
                ItemStack offStack = user.getOffhandItem();
                int cd = getCD(stack);
                if (!offStack.isEmpty() && offStack.is(Tags.CARD) && offStack.getCount() > 1) {
                    if (cd == 0) {

                        ItemStack[] stacks = {new ItemStack(ModItems.BINGLIANG_ITEM), new ItemStack(ModItems.TOO_HAPPY_ITEM), new ItemStack(ModItems.DISCARD), new ItemStack(ModItems.FIRE_ATTACK), new ItemStack(ModItems.JIEDAO), new ItemStack(ModItems.JUEDOU), new ItemStack(ModItems.NANMAN), new ItemStack(ModItems.STEAL), new ItemStack(ModItems.TAOYUAN), new ItemStack(ModItems.TIESUO), new ItemStack(ModItems.WANJIAN), new ItemStack(ModItems.WUXIE), new ItemStack(ModItems.WUZHONG)};
                        Container inventory = new SimpleContainer(20);
                        for (var stack1 : stacks) inventory.setItem(Arrays.stream(stacks).toList().indexOf(stack1), stack1);
                        inventory.setItem(18, stack); //用于在Handler中找到原本的stack

                        openSimpleMenu(user, user, inventory, Component.translatable("item.dabaosword.qice.screen"));
                    }
                    else {user.displayClientMessage(Component.translatable("dabaosword.cooldown").withStyle(ChatFormatting.RED), true);}
                }
                else {user.displayClientMessage(Component.translatable("item.dabaosword.qice.tip").withStyle(ChatFormatting.RED), true);}
            }

            if (stack.getItem() == SkillCards.TAOLUAN) {
                if (user.getHealth() + 5 * count(user, Tags.RECOVER) > 4.99) {

                    ItemStack[] stacks = {new ItemStack(ModItems.THUNDER_SHA), new ItemStack(ModItems.FIRE_SHA), new ItemStack(ModItems.SHAN), new ItemStack(ModItems.PEACH), new ItemStack(ModItems.JIU), new ItemStack(ModItems.BINGLIANG_ITEM), new ItemStack(ModItems.TOO_HAPPY_ITEM), new ItemStack(ModItems.DISCARD), new ItemStack(ModItems.FIRE_ATTACK), new ItemStack(ModItems.JIEDAO), new ItemStack(ModItems.JUEDOU), new ItemStack(ModItems.NANMAN), new ItemStack(ModItems.STEAL), new ItemStack(ModItems.TAOYUAN), new ItemStack(ModItems.TIESUO), new ItemStack(ModItems.WANJIAN), new ItemStack(ModItems.WUXIE), new ItemStack(ModItems.WUZHONG)};
                    Container inventory = new SimpleContainer(20);
                    for (var stack1 : stacks) inventory.setItem(Arrays.stream(stacks).toList().indexOf(stack1), stack1);
                    inventory.setItem(18, stack);

                    openSimpleMenu(user, user, inventory, Component.translatable("item.dabaosword.taoluan.screen"));
                }
                else {user.displayClientMessage(Component.translatable("item.dabaosword.taoluan.tip").withStyle(ChatFormatting.RED), true);}
            }

            //==============================以下技能需要target==============================

            if (stack.getItem() == SkillCards.ZHIJIAN) {
                ItemStack itemStack = user.getMainHandItem();
                if (itemStack.getItem() instanceof Equipment && itemStack.getItem() != ModItems.CARD_PILE.get()) {
                    NeoForge.EVENT_BUS.post(new CardMoveListener(user, target, itemStack, itemStack.getCount(), CardMoveListener.Type.INV_TO_EQUIP));
                    Equipment.equipItem(target, itemStack);
                    voice(user, Sounds.ZHIJIAN);
                    draw(user);
                } else user.displayClientMessage(Component.translatable("zhijian.fail").withStyle(ChatFormatting.RED), true);
            }

            if (stack.getItem() == SkillCards.GONGXIN) {
                int cd = getCD(stack);
                if (cd > 0) user.displayClientMessage(Component.translatable("dabaosword.cooldown").withStyle(ChatFormatting.RED), true);
                else {
                    voice(user, Sounds.GONGXIN);
                    openInv(user, target, Component.translatable("gongxin.title"), targetInv(target, false, false, 2, stack));
                    setCD(stack, 30);
                }
            }

            if (stack.getItem() == SkillCards.YIJI) {
                int i = getTag(stack);
                if (i > 0 ) openInv(user, target, Component.translatable("give_card.title", stack.getDisplayName()), targetInv(user, false, false, 2, stack));
            }

            if (stack.getItem() == SkillCards.RENDE) {
                openInv(user, target, Component.translatable("give_card.title", stack.getDisplayName()), targetInv(user, false, false, 2, stack));
            }
        }
    }

    public static void openInv(Player player, Player target, Component title, Container targetInv) {
        if (!player.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider((i, inv, player1) -> new PlayerInvScreenHandler(i, targetInv, target), title), (buf -> buf.writeInt(target.getId())));
        }
    }

    public static Container targetInv(Player target, Boolean equip, Boolean armor, int cards, ItemStack eventStack) {
        /*
        Boolean equip: 是否显示装备牌
        Boolean armor: 是否显示玩家的盔甲
        int cards: 是否显示手牌。0：完全不显示；1：显示随机选取手牌；2：显示所有手牌；3：显示所有物品
        */
        Container targetInv = new SimpleContainer(60);
        var component = CuriosApi.getCuriosInventory(target);
        if(component.isPresent() && equip) {
            var allEquipped = component.get().getEquippedCurios();
            for(int i = 0; i < allEquipped.getSlots(); i++) {
                ItemStack stack = allEquipped.getStackInSlot(i);
                if (stack.getTags().toList().equals(ModItems.GUDING_WEAPON.get().getDefaultInstance().getTags().toList())) targetInv.setItem(0, stack);
                if (stack.getTags().toList().equals(ModItems.BAGUA.get().getDefaultInstance().getTags().toList())) targetInv.setItem(1, stack);
                if (stack.getItem() == ModItems.DILU.get()) targetInv.setItem(2, stack);
                if (stack.getItem() == ModItems.CHITU.get()) targetInv.setItem(3, stack);
            }//四件装备占1~4格
        }

        List<ItemStack> armors = List.of(target.getItemBySlot(EquipmentSlot.HEAD), target.getItemBySlot(EquipmentSlot.CHEST), target.getItemBySlot(EquipmentSlot.LEGS), target.getItemBySlot(EquipmentSlot.FEET));
        for (ItemStack stack : armors) {
            if (armor && !stack.isEmpty()) targetInv.setItem(armors.indexOf(stack) + 4, stack);
        }//4件盔甲占5~8格

        NonNullList<ItemStack> targetInventory = target.getInventory().items;
        List<Integer> cardSlots = IntStream.range(0, targetInventory.size()).filter(i -> isCard(targetInventory.get(i))).boxed().toList();
        if (cards == 2 && !cardSlots.isEmpty()) {
            for(Integer i : cardSlots) {
                targetInv.setItem(i + 9, targetInventory.get(i));
            }
        }//副手物品在第9格，其他背包中的物品依次排列
        ItemStack off = target.getOffhandItem();
        if (cards == 2 && isCard(off)) targetInv.setItem(8, off);
        if (cards == 3) {
            for (ItemStack stack : targetInventory) {
                if (!stack.isEmpty()) targetInv.setItem(targetInventory.indexOf(stack) + 9, stack);
            }
            targetInv.setItem(8, off);
        }
        targetInv.setItem(54, new ItemStack(ModItems.GAIN_CARD, cards));//用于传递显示卡牌信息
        targetInv.setItem(55, eventStack);//用于传递stack信息
        return targetInv;
    }

    public static void openSimpleMenu(Player player, Player target, Container inventory, Component title) {
        if (!player.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider(((i, inv, player1) -> new SimpleMenuHandler(i, inventory, target)), title), (buf -> buf.writeInt(target.getId())));
        }
    }
}
