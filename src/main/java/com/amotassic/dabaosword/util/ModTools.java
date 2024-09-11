package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.card.GainCardItem;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.Objects;

public class ModTools {
    public static boolean noTieji(LivingEntity entity) {return !entity.hasEffect(ModItems.TIEJI);}

    //判断是否有某个饰品
    public static boolean hasTrinket(Item item, LivingEntity entity) {
        if (item instanceof SkillItem) {
            if (item.getDefaultInstance().is(Tags.LOCK_SKILL)) return trinketItem(item, entity) != null;
            else return trinketItem(item, entity) != null && noTieji(entity);}
        return trinketItem(item, entity) != null;
    }

    public static ItemStack trinketItem(Item item, LivingEntity entity) {
        var optional = CuriosApi.getCuriosInventory(entity);
        if (optional.isPresent()) {
            var handler = optional.get().findFirstCurio(item);
            if (handler.isPresent()) {
                return handler.get().stack();
            }
        }
        return null;
    }

    //判断玩家是否有某个物品
    public static boolean hasItem(@NotNull Player player, @NotNull Item item) {
        return getItem(player, item) != ItemStack.EMPTY;
    }

    public static ItemStack getItem(@NotNull Player player, Item item) {
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == item) return stack;
        }
        return ItemStack.EMPTY;
    }

    //判断是否是非基本牌
    public static boolean nonBasic(ItemStack stack) {
        return stack.is(Tags.CARD) && !stack.is(Tags.BASIC_CARD);
    }

    //判断是否是卡牌，包含GAIN_CARD
    public static boolean isCard(ItemStack stack) {
        return stack.is(Tags.CARD) || stack.getItem() == ModItems.GAIN_CARD.get();
    }

    //判断是否有含某个标签的物品
    public static Boolean hasItemInTag(TagKey<Item> tag, @NotNull Player player) {
        return player.getInventory().contains(tag);
    }

    public static int getSlotInTag(TagKey<Item> tag, @NotNull Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !stack.is(tag)) continue;
            return i;
        }
        return -1;
    }

    //获取背包中第一个含有含某个标签的物品
    public static ItemStack stackInTag(TagKey<Item> tag, @NotNull Player player) {
        Inventory inv = player.getInventory();
        int i = getSlotInTag(tag, player);
        return inv.getItem(i);
    }

    public static int getShaSlot(@NotNull Player player) {
        for (int i = 0; i < 18; ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !stack.is(Tags.SHA)) continue;
            return i;
        }
        return -1;
    }
    //只检测玩家物品栏前18格是否有杀
    public static ItemStack shaStack(@NotNull Player player) {
        return player.getInventory().getItem(getShaSlot(player));
    }

    //播放语音
    public static void voice(@NotNull LivingEntity entity, SoundEvent sound) {voice(entity, sound, 2);}
    public static void voice(@NotNull LivingEntity entity, SoundEvent sound, float volume) {
        if (entity.level() instanceof ServerLevel world) {
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, volume, 1.0F);
        }
    }

    public static int count(Player player, TagKey<Item> tag) {
        Inventory inv = player.getInventory();
        int n = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(tag)) n += stack.getCount();
        }
        return n;
    }

    public static int count(Player player, Item item) {
        Inventory inv = player.getInventory();
        int n = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == item) n += stack.getCount();
        }
        return n;
    }

    //改了摸牌物品之后，应该不用这样了，但是它就是方便，暂且保留吧
    public static int countCards(Player player) {return count(player, Tags.CARD) + count(player, ModItems.GAIN_CARD.get());}

    public static void draw(Player player) {draw(player, 1);}
    public static void draw(Player player, int count) {
        for (int n = 0; n<count; n++) {
            List<LootEntry> lootEntries = LootTableParser.parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/draw.json"));
            LootEntry selectedEntry = GainCardItem.selectRandomEntry(lootEntries);

            if (player.hasEffect(ModItems.BINGLIANG)) {
                int amplifier = Objects.requireNonNull(player.getEffect(ModItems.BINGLIANG)).getAmplifier();
                player.removeEffect(ModItems.BINGLIANG);
                voice(player, SoundEvents.VILLAGER_NO,1);
                if (amplifier != 0) {
                    player.addEffect(new MobEffectInstance(ModItems.BINGLIANG, -1, amplifier - 1));
                } //如果有兵粮寸断效果就不摸牌，改为将debuff等级减一
            } else {
                give(player, new ItemStack(BuiltInRegistries.ITEM.get(selectedEntry.item())));
                voice(player, SoundEvents.EXPERIENCE_ORB_PICKUP,1);
            }
        }
    }

    public static void give(Player player, ItemStack stack) {
        ItemEntity item = player.drop(stack, false);
        if (item == null) return;
        item.setNoPickUpDelay();
        item.setTarget(player.getUUID());
    }

    public static int getCD(ItemStack stack) { //获取物品的内置冷却时间
        return stack.get(ModItems.CD) == null ? 0 : Objects.requireNonNull(stack.get(ModItems.CD));
    }

    public static void setCD(ItemStack stack, int seconds) { //设置物品的内置冷却时间
        stack.set(ModItems.CD, seconds);
    }

    public static int getTag(ItemStack stack) { //获取物品的标签的数量
        return stack.get(ModItems.TAGS) == null ? 0 : Objects.requireNonNull(stack.get(ModItems.TAGS));
    }

    public static void setTag(ItemStack stack, int value) { //设置物品的标签的数量
        stack.set(ModItems.TAGS, value);
    }
}
