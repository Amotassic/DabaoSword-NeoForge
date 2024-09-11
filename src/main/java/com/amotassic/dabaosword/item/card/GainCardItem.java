package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.LootEntry;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

public class GainCardItem extends CardItem {
    public GainCardItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            if (!player.isCreative() && !player.isSpectator() && stack.getItem() == ModItems.GAIN_CARD.get()) {
                draw(player, stack.getCount());
                stack.setCount(0);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide) {
            int m;
            //摸牌
            if (user.getItemInHand(hand).getItem() == ModItems.GAIN_CARD.get()) {
                if (user.isShiftKeyDown()) {m=user.getItemInHand(hand).getCount();} else {m=1;}
                draw(user,m);
                if (!user.isCreative()) {user.getItemInHand(hand).shrink(m);}
            }
            //无中生有
            if (user.getItemInHand(hand).getItem() == ModItems.WUZHONG.get()) {
                draw(user,2);
                NeoForge.EVENT_BUS.post(new CardUsePostListener(user, user.getItemInHand(hand), user));
                voice(user, Sounds.WUZHONG.get());
            }
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

    public static LootEntry selectRandomEntry(List<LootEntry> lootEntries) {
        double totalWeight = lootEntries.stream().mapToDouble(LootEntry::weight).sum();
        double randomValue = new Random().nextDouble() * totalWeight;
        double currentWeight = 0;
        for (LootEntry entry : lootEntries) {
            currentWeight += entry.weight();
            if (randomValue < currentWeight) {
                return entry;
            }
        }
        // 如果没有匹配的条目，默认返回最后一个条目
        return lootEntries.getLast();
    }
}
