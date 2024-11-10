package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.api.Card;
import com.amotassic.dabaosword.api.CardPileInventory;
import com.amotassic.dabaosword.api.event.CardCBs;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.card.CardItem;
import com.amotassic.dabaosword.item.equipment.Equipment;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ModTools {
    //通过predicate寻找对应物品，免得添加标签
    public static final Predicate<ItemStack> canSaveDying = s -> s.is(ModItems.JIU) || s.is(ModItems.PEACH);
    public static final Predicate<ItemStack> isSha = s -> s.getItem() instanceof CardItem.Sha;
    //public static final Predicate<ItemStack> nonBasic = s -> s.isIn(Tags.Items.CARD) && !s.isIn(Tags.Items.BASIC_CARD);
    //判断是否是卡牌
    public static final Predicate<ItemStack> isCard = s -> s.is(Tags.CARD);
    public static boolean isCard(ItemStack stack) {return stack.is(Tags.CARD);}
    public static final Predicate<ItemStack> isDiamondCard = s -> getSuit(s) == Card.Suits.Diamond;
    public static final Predicate<ItemStack> isHeartCard = s -> getSuit(s) == Card.Suits.Heart;
    public static final Predicate<ItemStack> isClubCard = s -> getSuit(s) == Card.Suits.Club;
    public static final Predicate<ItemStack> isSpadeCard = s -> getSuit(s) == Card.Suits.Spade;
    public static final Predicate<ItemStack> isRedCard = isDiamondCard.or(isHeartCard);
    public static final Predicate<ItemStack> isBlackCard = isClubCard.or(isSpadeCard);
    
    public static boolean noTieji(LivingEntity entity) {return !entity.hasEffect(ModItems.TIEJI);}

    /**判断是否有某个饰品*/
    public static boolean hasTrinket(Item item, LivingEntity entity) {
        if (item instanceof SkillItem) {
            if (item.getDefaultInstance().is(Tags.LOCK_SKILL)) return !trinketItem(item, entity).isEmpty();
            else return !trinketItem(item, entity).isEmpty() && noTieji(entity);}
        return !trinketItem(item, entity).isEmpty();
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
    /**获取牌堆或背包中的一张符合条件的卡牌*/
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

    /**专为处理卡牌减少而写的方法，牌堆中的卡牌减少，需要保存nbt*/
    public static void cardDecrement(Tuple<CardPileInventory, ItemStack> stack, int count) {
        if (stack.getA() == null) stack.getB().shrink(count);
        else stack.getA().removeStack(stack.getB(), count);
    }
    /**另一个用于处理卡牌减少的方法，暂时只用于卡牌弃置和移动的方法中*/
    public static void cardDecrement(LivingEntity entity, ItemStack stack, int count) {
        var pair = getCard(entity, s -> ItemStack.matches(s, stack));
        //如果是牌堆中的卡牌，需要调用牌堆的方法来减少，以保存nbt
        if (pair.getA() != null) pair.getA().removeStack(pair.getB(), count);
        //那么为什么这里没有else呢？因为此stack非彼pair.getRight()，如果不减少会出bug
        stack.shrink(count);
    }
    /**卡牌使用后减少，不需要传入原始的itemStack*/
    public static void cardUseAndDecrement(LivingEntity user, ItemStack card) {
        //即使创造模式，无懈可击也会消耗，为什么呢？我也不知道
        if (card.is(ModItems.WUXIE)) cardDecrement(getCard(user, s -> s.is(ModItems.WUXIE)), 1);
        else {
            //如果使用者是创造模式玩家，则不消耗卡牌
            if (user instanceof Player player && player.getAbilities().instabuild) return;
            //找到和要消耗的完全相同的卡牌，若找不到，则找和要消耗的卡牌同名的牌
            var pair = getCard(user, s -> ItemStack.matches(s, card));
            if (pair.getB().isEmpty()) pair = getCard(user, s -> s.is(card.getItem()));
            var mainHand = user.getMainHandItem();
            //如果使用者是玩家，且消耗了主手上的卡牌后主手空出，则补充一张同名牌到主手上
            if (user instanceof Player player && ItemStack.matches(pair.getB(), mainHand)) {
                cardDecrement(pair, 1);
                if (mainHand.isEmpty()) {
                    var p = getCard(user, s -> s.is(card.getItem()));
                    if (!p.getB().isEmpty()) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, p.getB().copy());
                        player.getMainHandItem().setPopTime(5);
                        cardDecrement(p, p.getB().getCount());
                    }
                }
            } else cardDecrement(pair, 1);
        }
    }

    /**判断生物是否有某个物品*/
    public static boolean hasItem(@NotNull LivingEntity entity, Predicate<ItemStack> predicate) {
        return !getItem(entity, predicate).isEmpty();
    }
    /**获取玩家背包中第一个符合条件的物品，或者生物的符合条件的主副手物品*/
    public static ItemStack getItem(@NotNull LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack stack = player.getInventory().getItem(i);
                if (predicate.test(stack)) return stack;
            }
        } else {
            if (predicate.test(entity.getMainHandItem())) return entity.getMainHandItem();
            if (predicate.test(entity.getOffhandItem())) return entity.getOffhandItem();
        }
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

    /**数玩家所有牌的数量*/
    public static int countCards(Player player) {return countCard(player, isCard);}
    /**数玩家牌堆背包和物品栏的卡牌*/
    public static int countCard(Player player, Predicate<ItemStack> predicate) {
        int n = count(player, predicate);
        for (var card : new CardPileInventory(player).cards) {if (predicate.test(card)) n += card.getCount();}
        return n;
    }
    /**只数玩家物品栏里的物品*/
    public static int count(Player player, Predicate<ItemStack> predicate) {
        Inventory inv = player.getInventory();
        int n = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (predicate.test(stack)) n += stack.getCount();
        }
        return n;
    }

    /**自定义战利品表解析*/
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
        initSuitsAndRanks(stack);
        ItemEntity item = player.drop(stack, false);
        if (item == null) return;
        item.setNoPickUpDelay();
        item.setTarget(player.getUUID());
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

    public static void initSuitsAndRanks(ItemStack stack) {
        if (isCard(stack)) {
            CompoundTag nbt = getOrCreateNbt(stack);
            if (nbt.contains("Card")) return;
            ListTag list = new ListTag();
            CompoundTag compound = new CompoundTag();
            var suitAndRank = getDefaultOrRandomSuitAndRank(stack);
            compound.putString("Suit", suitAndRank.getA());
            compound.putString("Rank", suitAndRank.getB());
            list.add(compound);
            nbt.put("Card", list);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        }
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
    /**仿照1.20代码写的获取物品NBT的方法*/
    public static CompoundTag getOrCreateNbt(ItemStack stack) {
        if (stack.isEmpty()) return new CompoundTag();
        CustomData component = stack.get(DataComponents.CUSTOM_DATA);
        if (component == null) stack.set(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
        return Objects.requireNonNull(stack.get(DataComponents.CUSTOM_DATA)).copyTag();
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

    /**转化卡牌技能通用方法*/
    public static void viewAs(Player player, ItemStack skill, int CD, Predicate<ItemStack> predicate, ItemStack result, SoundEvent sound) {
        if (!player.level().isClientSide && noTieji(player) && getCD(skill) == 0) {
            ItemStack stack = player.getOffhandItem();
            if (predicate.test(stack)) {
                setCD(skill, CD);
                stack.shrink(1);
                give(player, result);
                voice(player, sound);
            }
        }
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
        if(equip) {
            for(var stack : allTrinkets(invOwner)) {
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
            for (var stack : inventory) {targetInv.setItem(inventory.indexOf(stack) + 9, stack);}
            if (!cardSlots.isEmpty()) { //卡牌背包中的牌显示在中间36格
                for(Integer i : cardSlots) {
                    if (i > 8) break; //快捷栏的牌显示在最底层9格
                    targetInv.setItem(i + 45, inv.get(i));
                }
            }
            if (isCard(off)) targetInv.setItem(8, off);
        }
        if (cards == 3) {
            for (ItemStack stack : inv) {
                if (!stack.isEmpty()) targetInv.setItem(inv.indexOf(stack) + 9, stack);
            }
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

    /**如果卡牌可以生效，则触发卡牌的效果，然后调用卡牌使用后事件的方法*/
    public static boolean cardUsePre(LivingEntity user, ItemStack stack, @Nullable LivingEntity target) {
        if (!NeoForge.EVENT_BUS.post(new CardCBs.UsePre(user, stack, target)).isCanceled()) {
            Card card = (Card) stack.getItem();
            card.cardUse(user, stack, target);
            //如果卡牌可以立即生效，则直接触发卡牌使用后事件
            if (!card.notImmediatelyEffective()) cardUsePost(user, stack, target);
            return true;
        }
        return false;
    }

    /**播放音效以及移除卡牌，然后触发卡牌使用后事件*/
    public static void cardUsePost(LivingEntity user, ItemStack stack, @Nullable LivingEntity target) {
        cardUsePost(user, stack, target, true);
    }
    public static void cardUsePost(LivingEntity user, ItemStack stack, @Nullable LivingEntity target, boolean consume) {
        if (stack.getItem() instanceof CardItem) voice(user, stack);
        ItemStack copy = stack.copy();
        if (consume) cardUseAndDecrement(user, copy);
        NeoForge.EVENT_BUS.post(new CardCBs.UsePost(user, stack, target));
    }

    /**调用卡牌弃置监听器的方法，除非stack来自牌堆背包，否则一定要传入原始的stack！
     * 因为还没有真正到事件触发时就已经移除了卡牌，所以事件中使用的stack是复制的！*/
    public static void cardDiscard(LivingEntity entity, ItemStack stack, int count, boolean fromEquip) {
        ItemStack copy = stack.copyWithCount(count);
        //移除被弃置的牌
        cardDecrement(entity, stack, count);
        NeoForge.EVENT_BUS.post(new CardCBs.Discard(entity, copy, count, fromEquip));
    }

    /**调用卡牌移动监听器的方法，除非stack来自牌堆背包，否则一定要传入原始的stack！
     * 因为还没有真正到事件触发时就已经移除了卡牌，所以事件中使用的stack是复制的！*/
    public static void cardMove(LivingEntity from, Player to, ItemStack stack, int count, CardCBs.T type) {
        ItemStack copy = stack.copyWithCount(count);
        //移除来源的牌
        cardDecrement(from, stack, count);
        //如果是移动到物品栏的类型，则给to等量的物品
        if (type == CardCBs.T.INV_TO_INV || type == CardCBs.T.EQUIP_TO_INV) give(to, copy);
        //如果是移动到装备栏的类型，则目标使用或替换该装备
        if (type == CardCBs.T.INV_TO_EQUIP || type == CardCBs.T.EQUIP_TO_EQUIP) Equipment.equipItem(to, copy);
        NeoForge.EVENT_BUS.post(new CardCBs.Move(from, to, copy, count, type));
    }

    public static void writeDamage(DamageSource source, float amount, boolean returnShan, ItemStack stack) {
        ListTag list = new ListTag();
        CompoundTag compound = new CompoundTag();
        compound.putString("type", source.typeHolder().unwrapKey().get().location().toString());
        if (source.getDirectEntity() != null) compound.putInt("source", source.getDirectEntity().getId());
        if (source.getEntity() != null) compound.putInt("attacker", source.getEntity().getId());
        compound.putFloat("amount", amount);
        if (returnShan) compound.putString("returning", "dabaosword:shan");
        list.add(compound);
        CompoundTag nbt = getOrCreateNbt(stack);
        nbt.put("DamageDodged", list);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
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
