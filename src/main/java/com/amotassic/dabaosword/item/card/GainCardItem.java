package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.LootEntry;
import com.amotassic.dabaosword.util.LootTableParser;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

public class GainCardItem extends CardItem {
    public GainCardItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide) {
            int m;
            //摸牌
            if (user.getItemInHand(hand).getItem() == ModItems.GAIN_CARD.get()) {
                if (user.isShiftKeyDown()) {m=user.getItemInHand(hand).getCount();} else {m=1;}
                draw(user,m); voice(user, SoundEvents.EXPERIENCE_ORB_PICKUP,1);
                if (!user.isCreative()) {user.getItemInHand(hand).shrink(m);}
            }
            //无中生有
            if (user.getItemInHand(hand).getItem() == ModItems.WUZHONG.get()) {
                draw(user,2);
                if (!user.isCreative()) user.getItemInHand(hand).shrink(1);
                voice(user, Sounds.WUZHONG.get());
                jizhi(user); benxi(user);
            }
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

    public static void draw(Player player, int count) {
        for (int n = 0; n<count; n++) {
            List<LootEntry> lootEntries = LootTableParser.parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/draw.json"));
            LootEntry selectedEntry = selectRandomEntry(lootEntries);

            give(player, new ItemStack(BuiltInRegistries.ITEM.get(selectedEntry.item())));
        }
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
