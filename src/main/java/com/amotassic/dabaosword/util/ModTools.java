package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.api.Card;
import com.amotassic.dabaosword.api.CardPileInventory;
import com.amotassic.dabaosword.api.ISha;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import com.amotassic.dabaosword.ui.SimpleMenuHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class ModTools {
    //通过predicate寻找对应物品，免得添加标签
    public static Predicate<ItemStack> p(Item item) {return s -> s.is(item);}
    public static final Predicate<ItemStack> canSaveDying = p(ModItems.JIU).or(p(ModItems.PEACH));
    public static final Predicate<ItemStack> isSha = s -> s.getItem() instanceof ISha;
    //判断是否是卡牌
    public static final Predicate<ItemStack> isCard = ModTools::isCard;
    public static boolean isCard(ItemStack s) {return s.getItem() instanceof Card card && card.getType() != null;}
    public static final Predicate<ItemStack> isBasic = s -> s.getItem() instanceof Card c && c.getType() == Card.Type.BASIC;
    public static final Predicate<ItemStack> isArmoury = s -> s.getItem() instanceof Card c && c.getType() == Card.Type.ARMOURY;
    public static final Predicate<ItemStack> isEquipment = s -> s.getItem() instanceof Card c && c.getType() == Card.Type.EQUIPMENT;
    public static final Predicate<ItemStack> isDiamondCard = s -> getSuit(s) == Card.Suits.Diamond;
    public static final Predicate<ItemStack> isHeartCard = s -> getSuit(s) == Card.Suits.Heart;
    public static final Predicate<ItemStack> isClubCard = s -> getSuit(s) == Card.Suits.Club;
    public static final Predicate<ItemStack> isSpadeCard = s -> getSuit(s) == Card.Suits.Spade;
    public static final Predicate<ItemStack> isRedCard = isDiamondCard.or(isHeartCard);
    public static final Predicate<ItemStack> isBlackCard = isClubCard.or(isSpadeCard);
    public static boolean isWanjian(DamageSource source) {
        return source.getDirectEntity() instanceof Arrow arrow && arrow.getTags().contains("a");
    }
    public static boolean isHuogong(DamageSource source) {
        return source.getDirectEntity() instanceof LargeFireball fireball && fireball.getTags().contains("a");
    }
    public static boolean isShandian(DamageSource source) {
        return source.getDirectEntity() instanceof LightningBolt lightning && lightning.getTags().contains("a");
    }
    
    public static boolean noTieji(LivingEntity entity) {return !entity.hasEffect(ModItems.TIEJI);}

    /**判断是否有某个饰品*/
    public static boolean hasTrinket(Item item, LivingEntity entity) {
        if (item instanceof SkillItem) {
            if (item.getDefaultInstance().is(Tags.LOCK_SKILL)) return !trinketItem(item, entity).isEmpty();
            else return !trinketItem(item, entity).isEmpty() && noTieji(entity);}
        return !trinketItem(item, entity).isEmpty();
    }
    public static boolean isEquipped(LivingEntity entity, Predicate<ItemStack> p) {
        var optional = CuriosApi.getCuriosInventory(entity);
        return optional.map(c -> c.isEquipped(p)).orElse(false);
    }

    public static ItemStack trinketItem(Item item, LivingEntity entity) {
        var optional = CuriosApi.getCuriosInventory(entity);
        if (optional.isPresent()) {
            var handler = optional.get().findFirstCurio(item);
            if (handler.isPresent()) return handler.get().stack();
        } return ItemStack.EMPTY;
    }

    /**获取该实体的所有饰品，输出为ItemStack列表*/
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

    /**判断技能是否能触发（依据是否为锁定技和是否有铁骑效果）*/
    public static boolean canTrigger(ItemStack item, LivingEntity entity) {
        if (item.getItem() instanceof SkillItem) {
            if (item.is(Tags.LOCK_SKILL)) return true;
            return noTieji(entity);
        } return true;
    }

    /**判断牌堆和背包中是否有符合条件的卡牌*/
    public static boolean hasCard(LivingEntity entity, Predicate<ItemStack> predicate) {
        return !getCard(entity, predicate).getB().isEmpty();
    }
    /**获取牌堆或背包中的一张符合条件的卡牌*/@SuppressWarnings("all")
    public static Tuple<CardPileInventory, ItemStack> getCard(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity instanceof Player player) {
            CardPileInventory inventory = new CardPileInventory(player);
            for (int i = inventory.cards.size() - 1; i >= 0; i--) { //倒序检索
                var card = inventory.getItem(i);
                if (predicate.test(card)) return new Tuple<>(inventory, card);
            }
        }
        return new Tuple<>(null, getItem(entity, predicate));
    }

    /**判断生物是否有某个物品*/
    public static boolean hasItem(@NotNull LivingEntity entity, Predicate<ItemStack> predicate) {
        return !getItem(entity, predicate).isEmpty();
    }
    /**获取玩家背包中第一个符合条件的物品，或者生物的符合条件的主副手物品*/
    public static ItemStack getItem(@NotNull LivingEntity entity, Predicate<ItemStack> predicate) {
        for (var stack : getItems(entity, predicate, true, false, false, false)) if (predicate.test(stack)) return stack;
        return ItemStack.EMPTY;
    }

    /**播放语音*/
    public static void voice(@NotNull LivingEntity entity, SoundEvent sound) {voice(entity, sound, 2);}
    public static void voice(@NotNull LivingEntity entity, SoundEvent sound, float volume) {
        if (entity.level() instanceof ServerLevel world) {
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, volume, 1.0F);
        }
    }
    public static void voice(@NotNull LivingEntity entity, ItemStack stack) {
        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(stack.getItem().toString()));
        if (sound != null) voice(entity, sound);
    }

    /**数玩家所有手牌的数量*/
    public static int countCards(LivingEntity entity) {return countCard(entity, isCard);}
    /**数玩家所有卡牌，包括已装备的牌*/
    public static int countAllCards(LivingEntity entity) {return count(entity, isCard, false, true, true);}
    /**数玩家牌堆背包和物品栏的卡牌*/
    public static int countCard(LivingEntity entity, Predicate<ItemStack> predicate) {
        return count(entity, predicate, false, true, false);
    }
    /**只数玩家物品栏里的物品*/
    public static int count(LivingEntity entity, Predicate<ItemStack> predicate, boolean armor, boolean pile, boolean trinkets) {
        int n = 0;
        for (var stack : getItems(entity, predicate, true, armor, trinkets, pile)) n += stack.getCount();
        return n;
    }

    /**将一个生物的所有符合条件的物品整理成一个list
     * @param main 如果是玩家，包括玩家的物品栏和副手物品，否则只包括生物的主副手物品*/
    public static List<ItemStack> getItems(LivingEntity entity, Predicate<ItemStack> p, boolean main, boolean armor, boolean trinket, boolean pile) {
        List<ItemStack> items = new ArrayList<>();
        //如果是玩家则把牌堆中的物品添加到待选物品中
        if (pile && entity instanceof Player player) for (var stack : new CardPileInventory(player).cards) if (p.test(stack)) items.add(stack);
        if (main) { //如果是玩家则把背包和副手的物品添加到待选物品中，否则只添加主副手物品
            if (entity instanceof Player player) {
                for (var stack : player.getInventory().items) if (p.test(stack)) items.add(stack);
            } else if (p.test(entity.getMainHandItem())) items.add(entity.getMainHandItem());
            if (p.test(entity.getOffhandItem())) items.add(entity.getOffhandItem());
        }
        if (armor) for (var stack : entity.getArmorSlots()) if (p.test(stack)) items.add(stack);
        if (trinket) for (var stack : allTrinkets(entity)) if (p.test(stack)) items.add(stack);
        return items;
    }

    /**自定义战利品表解析*/
    public static ResourceLocation parseLootTable(ResourceLocation lootTableId) {
        Gson gson = new Gson();
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(ModTools.class.getResourceAsStream("/data/dabaosword/" + lootTableId.getPath())));
        JsonObject o = gson.fromJson(reader, JsonObject.class);
        float totalWeight = 0;
        for (var element : o.getAsJsonArray("results")) {
            totalWeight += element.getAsJsonObject().get("weight").getAsFloat();
        }
        float randomValue = new Random().nextFloat(totalWeight);
        float currentWeight = 0;
        for (JsonElement element : o.getAsJsonArray("results")) {
            JsonObject result = element.getAsJsonObject();
            currentWeight += result.get("weight").getAsFloat();
            if (randomValue < currentWeight) return ResourceLocation.parse(result.get("item").getAsString());
        }
        return ResourceLocation.parse("minecraft:air");
    }

    public static void draw(LivingEntity entity) {draw(entity, 1);}
    public static void draw(LivingEntity entity, int count) {
        for (int n = 0; n<count; n++) {
            if (entity.hasEffect(ModItems.BINGLIANG)) {
                int amplifier = Objects.requireNonNull(entity.getEffect(ModItems.BINGLIANG)).getAmplifier();
                entity.removeEffect(ModItems.BINGLIANG);
                voice(entity, SoundEvents.VILLAGER_NO,1);
                if (amplifier != 0) {
                    entity.addEffect(new MobEffectInstance(ModItems.BINGLIANG, -1, amplifier - 1));
                } //如果有兵粮寸断效果就不摸牌，改为将debuff等级减一
            } else {
                give(entity, newCard());
                voice(entity, SoundEvents.EXPERIENCE_ORB_PICKUP,1);
            }
        }
    }


    public static ItemStack newCardNoSR() {
        return new ItemStack(BuiltInRegistries.ITEM.get(parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/draw.json"))));
    }
    public static ItemStack newCard() {return initSuitsAndRanks(newCardNoSR());}
    public static ItemStack newCard(Predicate<ItemStack> predicate) {
        ItemStack stack = newCardNoSR();
        while (!predicate.test(stack)) stack = newCardNoSR();
        return initSuitsAndRanks(stack);
    }

    public static void give(LivingEntity entity, ItemStack stack) {
        initSuitsAndRanks(stack);
        if (entity instanceof Player player) {
            ItemEntity item = player.drop(stack, false);
            if (item == null) return;
            item.setInvulnerable(true);
            item.setNoPickUpDelay();
            item.setTarget(player.getUUID());
            return;
        }
        if (entity.getMainHandItem().isEmpty()) entity.setItemInHand(InteractionHand.MAIN_HAND, stack);
        else if (entity.getOffhandItem().isEmpty()) entity.setItemInHand(InteractionHand.OFF_HAND, stack);
    }

    public static Tuple<String, String> getDefaultOrRandomSuitAndRank(ItemStack stack) {
        //在json文件中为了方便读写，花色用了Suits.name()，点数用了Ranks.rank
        Tuple<String, String> random = new Tuple<>(Card.Suits.get(Component.empty()).name(), Card.Ranks.get("0").rank);
        String srPath = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath() + ".json";
        Gson gson = new Gson();
        InputStream stream = ModTools.class.getResourceAsStream("/data/dabaosword/default_suit_and_rank/" + srPath);
        if (stream == null) return random;

        InputStreamReader reader = new InputStreamReader(stream);
        JsonObject json = gson.fromJson(reader, JsonObject.class);
        int size = json.getAsJsonArray("suits_and_ranks").size();
        //随机获取一组花色和点数
        JsonObject obj = json.getAsJsonArray("suits_and_ranks").get(new Random().nextInt(size)).getAsJsonObject();
        if (obj.has("suit") && obj.has("rank")) {
            return new Tuple<>(obj.get("suit").getAsString(), obj.get("rank").getAsString());
        } else return random;
    }

    public static ItemStack initSuitsAndRanks(ItemStack stack) {
        if (isCard(stack)) {
            CompoundTag nbt = getOrCreateNbt(stack);
            if (nbt.contains("Card")) return stack;
            ListTag list = new ListTag();
            CompoundTag compound = new CompoundTag();
            var suitAndRank = getDefaultOrRandomSuitAndRank(stack);
            compound.putString("Suit", suitAndRank.getA());
            compound.putString("Rank", suitAndRank.getB());
            list.add(compound);
            nbt.put("Card", list);
            setNbt(stack, nbt);
        } return stack;
    }

    public static Tuple<Card.Suits, Card.Ranks> getSuitAndRank(ItemStack stack) {
        if (isCard(stack)) {
            CompoundTag compound = getOrCreateNbt(stack);
            if (compound.contains("Card")) {
                CompoundTag nbtCompound = ((ListTag) Objects.requireNonNull(compound.get("Card"))).getCompound(0);
                Card.Suits suit = Card.Suits.valueOf(nbtCompound.getString("Suit"));
                Card.Ranks rank = Card.Ranks.get(nbtCompound.getString("Rank"));
                return new Tuple<>(suit, rank);
            }
        }
        return null;
    }
    public static Card.Suits getSuit(ItemStack stack) {
        var sr = getSuitAndRank(stack);
        if (sr == null) return null; return sr.getA();
    }
    public static Card.Ranks getRank(ItemStack stack) {
        var sr = getSuitAndRank(stack);
        if (sr == null) return null; return sr.getB();
    }
    /**仿照1.20代码写的获取物品NBT的方法*/
    public static CompoundTag getOrCreateNbt(ItemStack stack) {
        if (stack.isEmpty()) return new CompoundTag();
        CustomData component = stack.get(DataComponents.CUSTOM_DATA);
        if (component == null) setNbt(stack, new CompoundTag());
        return Objects.requireNonNull(stack.get(DataComponents.CUSTOM_DATA)).copyTag();
    }
    public static void setNbt(ItemStack stack, CompoundTag nbt) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
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

    public static void openInv(Player player, Player target, Component title, ItemStack stack, boolean openSelfInv, boolean equip, boolean armor, int cards) {
        if (!player.level().isClientSide) {
            Player invOwner = openSelfInv ? player : target;
            player.openMenu(new SimpleMenuProvider((i, inv, player1) -> new PlayerInvScreenHandler(i, targetInv(invOwner, equip, armor, cards, stack, openSelfInv), target), title), (buf -> buf.writeInt(target.getId())));
        }
    }

    public static Container targetInv(Player invOwner, Boolean equip, Boolean armor, int cards, ItemStack eventStack, boolean openSelfInv) {
        /*
        Boolean equip: 是否显示装备牌
        Boolean armor: 是否显示玩家的盔甲
        int cards: 是否显示手牌。0：完全不显示；1：显示随机选取手牌；2：显示所有手牌；3：显示所有物品
        */
        Container targetInv = new SimpleContainer(60);
        if (equip) {
            for (var stack : allTrinkets(invOwner)) {
                if (stack.getTags().toList().equals(ModItems.GUDING_WEAPON.getDefaultInstance().getTags().toList())) targetInv.setItem(0, stack);
                if (stack.getTags().toList().equals(ModItems.BAGUA.getDefaultInstance().getTags().toList())) targetInv.setItem(1, stack);
                if (stack.is(ModItems.DILU)) targetInv.setItem(2, stack);
                if (stack.is(ModItems.CHITU)) targetInv.setItem(3, stack);
            }//四件装备占1~4格
        }

        ItemStack off = invOwner.getOffhandItem();
        List<ItemStack> armors = List.of(invOwner.getItemBySlot(EquipmentSlot.HEAD), invOwner.getItemBySlot(EquipmentSlot.CHEST), invOwner.getItemBySlot(EquipmentSlot.LEGS), invOwner.getItemBySlot(EquipmentSlot.FEET));
        for (ItemStack stack : armors) {
            if (armor && !stack.isEmpty()) targetInv.setItem(armors.indexOf(stack) + 4, stack);
        }//4件盔甲占5~8格

        NonNullList<ItemStack> inv = invOwner.getInventory().items;
        List<Integer> cardSlots = IntStream.range(0, inv.size()).filter(i -> isCard(inv.get(i))).boxed().toList();
        if (cards == 2) {
            var inventory = new CardPileInventory(invOwner).cards;
            for (var stack : inventory) targetInv.setItem(inventory.indexOf(stack) + 9, stack);
            if (!cardSlots.isEmpty()) { //卡牌背包中的牌显示在中间36格
                for(Integer i : cardSlots) {
                    if (i > 8) break; //快捷栏的牌显示在最底层9格
                    targetInv.setItem(i + 45, inv.get(i));
                }
            }
            if (isCard(off)) targetInv.setItem(8, off);
        }
        if (cards == 3) {
            for (ItemStack stack : inv) if (!stack.isEmpty()) targetInv.setItem(inv.indexOf(stack) + 9, stack);
            targetInv.setItem(8, off);
        }
        targetInv.setItem(54, new ItemStack(ModItems.GAIN_CARD, cards));//用于传递显示卡牌信息
        targetInv.setItem(55, eventStack);//用于传递stack信息
        if (openSelfInv) targetInv.setItem(56, new ItemStack(ModItems.GAIN_CARD));
        return targetInv;
    }

    public static void openSimpleMenu(Player player, Player target, Container inventory, Component title) {
        if (!player.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider(((i, inv, player1) -> new SimpleMenuHandler(i, inventory, target)), title), (buf -> buf.writeInt(target.getId())));
        }
    }

    public static void closeGUI(Player player) {
        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 1,2,false,false,false));
    }

    public static DamageSource getDamageSource(Entity source, ResourceKey<DamageType> type) {
        return new DamageSource(source.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), source);
    }

    public static void writeDamage(DamageSource source, float amount, boolean returnShan, ItemStack stack) {
        ListTag list = new ListTag();
        CompoundTag compound = new CompoundTag();
        //noinspection OptionalGetWithoutIsPresent
        compound.putString("type", source.typeHolder().unwrapKey().get().location().toString());
        if (source.getDirectEntity() != null) compound.putInt("source", source.getDirectEntity().getId());
        if (source.getEntity() != null) compound.putInt("attacker", source.getEntity().getId());
        compound.putFloat("amount", amount);
        if (returnShan) compound.putString("returning", "dabaosword:shan");
        list.add(compound);
        CompoundTag nbt = getOrCreateNbt(stack);
        nbt.put("DamageDodged", list);
        setNbt(stack, nbt);
    }
    //牌堆记录闪避伤害的方法
    public static Tuple<Tuple<DamageSource, Float>, ItemStack> getDamage(Player player) {
        if (player.level() instanceof ServerLevel world) {
            ItemStack stack = trinketItem(ModItems.CARD_PILE, player);
            CompoundTag compound = getOrCreateNbt(stack);
            if (compound.contains("DamageDodged")) {
                CompoundTag nbt = ((ListTag) Objects.requireNonNull(compound.get("DamageDodged"))).getCompound(0);
                ResourceKey<DamageType> type = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse(nbt.getString("type")));
                Holder<DamageType> entry = world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type);
                Entity source = world.getEntity(nbt.getInt("source"));
                Entity attacker = world.getEntity(nbt.getInt("attacker"));
                DamageSource damageSource = new DamageSource(entry, source, attacker);
                ItemStack returning = ItemStack.EMPTY;
                if (nbt.contains("returning")) returning = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(nbt.getString("returning"))));
                float amount = nbt.getFloat("amount");
                return new Tuple<>(new Tuple<>(damageSource, amount), returning);
            }
        }
        return null;
    }
}
