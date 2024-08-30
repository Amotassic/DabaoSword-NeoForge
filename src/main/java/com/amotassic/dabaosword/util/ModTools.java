package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Objects;
import java.util.Random;

import static com.amotassic.dabaosword.item.card.GainCardItem.draw;

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
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || stack.getItem() != item) continue;
            return true;
        }
        return false;
    }
    //移除玩家的1个物品
    public static void removeItem(@NotNull Player player, @NotNull Item item) {
        Inventory inv = player.getInventory();
        int i = inv.getSlotWithRemainingSpace(item.getDefaultInstance());
        inv.removeItem(i, 1);
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
    public static void voice(@NotNull LivingEntity entity, SoundEvent sound) {
        voice(entity, sound, 2);
    }
    public static void voice(@NotNull LivingEntity entity, SoundEvent sound, float volume) {
        if (entity.level() instanceof ServerLevel world) {
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, volume, 1.0F);
        }
    }

    //集智技能触发
    public static void jizhi(Player player) {
        if (hasTrinket(SkillCards.JIZHI.get(), player)) {
            draw(player, 1);
            if (new Random().nextFloat() < 0.5) {voice(player, Sounds.JIZHI1.get());} else {voice(player, Sounds.JIZHI2.get());}
        }
    }
    //奔袭技能触发
    public static void benxi(Player player) {
        if (hasTrinket(SkillCards.BENXI.get(), player)) {
            ItemStack stack = trinketItem(SkillCards.BENXI.get(), player);
            int benxi = getTag(stack);
            if (benxi < 5) {
                setTag(stack, benxi + 1);
                if (new Random().nextFloat() < 0.5) {voice(player, Sounds.BENXI1.get());} else {voice(player, Sounds.BENXI2.get());}
            }
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
