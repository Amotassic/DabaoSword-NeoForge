package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.api.CardPileInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.amotassic.dabaosword.util.ModTools.*;

public class DiscardItem extends CardItem {
    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (cardUsePre(user, user.getMainHandItem(), entity)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity entity) {
        if (user instanceof Player player) {
            if (entity instanceof Player target) {
                openInv(player, target, Component.translatable("dabaosword.discard.title", stack.getDisplayName()), stack, false, true, false, 1);
            } else {
                List<ItemStack> stacks = new ArrayList<>();
                if (isCard(entity.getMainHandItem())) stacks.add(entity.getMainHandItem());
                if (isCard(entity.getOffhandItem())) stacks.add(entity.getOffhandItem());
                if (!stacks.isEmpty()) {
                    ItemStack chosen = stacks.get(new Random().nextInt(stacks.size()));
                    cardDiscard(entity, chosen, 1, false);
                    cardUsePost(player, stack, entity);
                }
            }
        } else {
            if (entity instanceof Player player) { //如果是玩家则弃牌
                List<ItemStack> candidate = new ArrayList<>(new CardPileInventory(player).nonEmpty);
                //把背包中的卡牌添加到待选物品中
                NonNullList<ItemStack> inventory = player.getInventory().items;
                List<Integer> cardSlots = IntStream.range(0, inventory.size()).filter(j -> isCard(inventory.get(j))).boxed().toList();
                for (Integer slot : cardSlots) {
                    candidate.add(inventory.get(slot));
                }
                //把饰品栏的卡牌添加到待选物品中
                int equip = 0; //用于标记装备区牌的数量
                for (var stack1 : allTrinkets(player)) {
                    if (isCard(stack1)) candidate.add(stack1);
                    equip++;
                }
                if (!candidate.isEmpty()) {
                    int index = new Random().nextInt(candidate.size());
                    ItemStack chosen = candidate.get(index);
                    player.displayClientMessage(Component.translatable("dabaosword.discard", user.getDisplayName(), player.getDisplayName(), chosen.getDisplayName()), false);
                    cardDiscard(player, chosen, 1, index > candidate.size() - equip);
                    cardUsePost(user, stack, entity);
                }
            } else { //如果不是玩家则随机弃置它的主副手物品和装备
                List<ItemStack> candidate = new ArrayList<>();
                if (!entity.getMainHandItem().isEmpty()) candidate.add(entity.getMainHandItem());
                if (!entity.getOffhandItem().isEmpty()) candidate.add(entity.getOffhandItem());
                for (ItemStack armor : entity.getArmorSlots()) {
                    if (!armor.isEmpty()) candidate.add(armor);
                }
                if (!candidate.isEmpty()) {
                    int index = new Random().nextInt(candidate.size());
                    ItemStack chosen = candidate.get(index);
                    if (isCard(chosen)) cardDiscard(entity, chosen, 1, false);
                    cardUsePost(user, stack, entity);
                }
            }
        }
    }

    @Override
    public boolean notImmediatelyEffective() {return true;}
}
