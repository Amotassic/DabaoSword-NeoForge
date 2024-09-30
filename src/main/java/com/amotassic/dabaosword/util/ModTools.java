package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.event.listener.CardCBs;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import com.amotassic.dabaosword.ui.SimpleMenuHandler;
import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

public class ModTools {
    public static boolean noTieji(LivingEntity entity) {return !entity.hasEffect(ModItems.TIEJI);}

    //判断是否有某个饰品
    public static boolean hasTrinket(Item item, LivingEntity entity) {
        if (item instanceof SkillItem) {
            if (item.getDefaultInstance().is(Tags.LOCK_SKILL)) return trinketItem(item, entity) != ItemStack.EMPTY;
            else return trinketItem(item, entity) != ItemStack.EMPTY && noTieji(entity);}
        return trinketItem(item, entity) != ItemStack.EMPTY;
    }

    public static ItemStack trinketItem(Item item, LivingEntity entity) {
        var optional = CuriosApi.getCuriosInventory(entity);
        if (optional.isPresent()) {
            var handler = optional.get().findFirstCurio(item);
            if (handler.isPresent()) {
                return handler.get().stack();
            }
        }
        return ItemStack.EMPTY;
    }

    public static List<ItemStack> allTrinkets(LivingEntity entity) {
        var optional = CuriosApi.getCuriosInventory(entity);
        if (optional.isEmpty()) return Collections.emptyList();
        List<ItemStack> list = new ArrayList<>();
        var handler = optional.get().getEquippedCurios();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            list.add(stack);
        }
        return list;
    }

    //判断技能是否能触发（依据是否为锁定技和是否有铁骑效果）
    public static boolean canTrigger(Item item, LivingEntity entity) {
        if (item.getDefaultInstance().is(Tags.LOCK_SKILL)) return true;
        return noTieji(entity);
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
        return stack.is(Tags.CARD) || stack.getItem() == ModItems.GAIN_CARD;
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
    public static int countCards(Player player) {return count(player, Tags.CARD) + count(player, ModItems.GAIN_CARD);}

    //自定义战利品表解析
    public static ResourceLocation parseLootTable(ResourceLocation lootTableId) {
        Gson gson = new Gson();
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(ModTools.class.getResourceAsStream("/data/dabaosword/" + lootTableId.getPath())));
        JsonObject o = gson.fromJson(reader, JsonObject.class);
        double totalWeight = 0;
        for (var element : o.getAsJsonArray("results")) {
            totalWeight += element.getAsJsonObject().get("weight").getAsDouble();
        }
        double randomValue = new Random().nextDouble() * totalWeight;
        double currentWeight = 0;
        for (JsonElement element : o.getAsJsonArray("results")) {
            JsonObject result = element.getAsJsonObject();
            currentWeight += result.get("weight").getAsDouble();
            if (randomValue < currentWeight) {
                return ResourceLocation.parse(result.get("item").getAsString());
            }
        }
        return ResourceLocation.parse("minecraft:air");
    }

    public static void draw(Player player) {draw(player, 1);}
    public static void draw(Player player, int count) {
        for (int n = 0; n<count; n++) {
            if (player.hasEffect(ModItems.BINGLIANG)) {
                int amplifier = Objects.requireNonNull(player.getEffect(ModItems.BINGLIANG)).getAmplifier();
                player.removeEffect(ModItems.BINGLIANG);
                voice(player, SoundEvents.VILLAGER_NO,1);
                if (amplifier != 0) {
                    player.addEffect(new MobEffectInstance(ModItems.BINGLIANG, -1, amplifier - 1));
                } //如果有兵粮寸断效果就不摸牌，改为将debuff等级减一
            } else {
                var selectedId = parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/draw.json"));
                give(player, new ItemStack(BuiltInRegistries.ITEM.get(selectedId)));
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
        return stack.get(AllRegs.Other.CD) == null ? 0 : Objects.requireNonNull(stack.get(AllRegs.Other.CD));
    }

    public static void setCD(ItemStack stack, int seconds) { //设置物品的内置冷却时间
        stack.set(AllRegs.Other.CD, seconds);
    }

    public static int getTag(ItemStack stack) { //获取物品的标签的数量
        return stack.get(AllRegs.Other.TAGS) == null ? 0 : Objects.requireNonNull(stack.get(AllRegs.Other.TAGS));
    }

    public static void setTag(ItemStack stack, int value) { //设置物品的标签的数量
        stack.set(AllRegs.Other.TAGS, value);
    }

    //转化卡牌技能通用方法
    public static void viewAs(Player player, ItemStack skill, int CD, Predicate<ItemStack> predicate, ItemStack result, SoundEvent sound) {viewAs(player, skill,CD, predicate, 1, result, sound);}
    public static void viewAs(Player player, ItemStack skill, int CD, Predicate<ItemStack> predicate, int count, ItemStack result, SoundEvent sound) {
        if (!player.level().isClientSide && noTieji(player) && getCD(skill) == 0) {
            ItemStack stack = player.getOffhandItem();
            if (predicate.test(stack)) {
                setCD(skill, CD);
                stack.shrink(count);
                give(player, result);
                voice(player, sound);
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
        if(equip) {
            for(var stack : allTrinkets(target)) {
                if (stack.getTags().toList().equals(ModItems.GUDING_WEAPON.getDefaultInstance().getTags().toList())) targetInv.setItem(0, stack);
                if (stack.getTags().toList().equals(ModItems.BAGUA.getDefaultInstance().getTags().toList())) targetInv.setItem(1, stack);
                if (stack.getItem() == ModItems.DILU) targetInv.setItem(2, stack);
                if (stack.getItem() == ModItems.CHITU) targetInv.setItem(3, stack);
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

    public static void cardUsePost(Player user, ItemStack stack, @Nullable LivingEntity target) {
        NeoForge.EVENT_BUS.post(new CardCBs.UsePost(user, stack, target));
    }

    public static void cardDiscard(Player player, ItemStack stack, int count, boolean fromEquip) {
        NeoForge.EVENT_BUS.post(new CardCBs.Discard(player, stack, count, fromEquip));
    }

    public static void cardMove(LivingEntity from, Player to, ItemStack stack, int count, CardCBs.T type) {
        NeoForge.EVENT_BUS.post(new CardCBs.Move(from, to, stack, count, type));
    }
}
