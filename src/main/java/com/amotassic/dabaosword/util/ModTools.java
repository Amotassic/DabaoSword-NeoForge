package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.CardPileInventory;
import com.amotassic.dabaosword.api.card.Card;
import com.amotassic.dabaosword.api.card.Suit;
import com.amotassic.dabaosword.api.skill.ExData;
import com.amotassic.dabaosword.api.skill.ISkill;
import com.amotassic.dabaosword.api.skill.Skill;
import com.amotassic.dabaosword.api.skill.Trigger;
import com.amotassic.dabaosword.data.CardSuitAndRank;
import com.amotassic.dabaosword.event.PVPGameEvents;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.card.CardItem;
import com.amotassic.dabaosword.item.card.Sha;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.ui.FullInvScreenHandler;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.Unpooled;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;
import java.util.function.Predicate;

import static top.theillusivec4.curios.api.CuriosApi.getCuriosInventory;

public class ModTools {
    public static Card c(ItemStack card) {return new Card(card);}
    /**生成一张花色和点数相同，但牌名不同的牌*/
    public static Card c(ItemStack card, CardItem replace) {
        var c = c(card);
        return new Card(replace, c.suit, c.rank);
    }
    public static Skill s(ItemStack skill) {return new Skill(skill);}
    public static ExData d() {return new ExData();}
    //通过predicate寻找对应物品，免得添加标签
    public static Predicate<ItemStack> p(Item item) {return s -> s.is(item);}
    //判断是否是卡牌
    public static boolean isCard(ItemStack s) {return s.getItem() instanceof CardItem;}
    public static final Predicate<ItemStack>
    canSaveDying = p(ModItems.JIU).or(p(ModItems.PEACH)),
    isSha = s -> s.getItem() instanceof Sha,
    isCard = ModTools::isCard,
    isBasic = s -> c(s).type == Card.BASIC,
    isArmoury = s -> c(s).type == Card.ARMOURY,
    isEquipment = s -> c(s).type == Card.EQUIPMENT,
    isDiamondCard = s -> c(s).suit == Suit.Diamond,
    isHeartCard = s -> c(s).suit == Suit.Heart,
    isClubCard = s -> c(s).suit == Suit.Club,
    isSpadeCard = s -> c(s).suit == Suit.Spade,
    isRedCard = isDiamondCard.or(isHeartCard),
    isBlackCard = isClubCard.or(isSpadeCard);

    @SafeVarargs
    public static <T> List<T> toList(T... t) {return new ArrayList<>(Arrays.asList(t));}

    /**判断是否有某个饰品*/
    public static boolean hasTrinket(Item item, LivingEntity entity) {return isEquipped(entity, p(item));}
    public static boolean isEquipped(LivingEntity entity, Predicate<ItemStack> p) {
        return getCuriosInventory(entity).map(c -> c.isEquipped(p)).orElse(false);
    }

    public static ItemStack trinketItem(Item item, LivingEntity entity) {
        return getCuriosInventory(entity).map(h -> h.findFirstCurio(item).map(SlotResult::stack).orElse(ItemStack.EMPTY)).orElse(ItemStack.EMPTY);
    }

    /**获取该实体的所有饰品，输出为ItemStack列表*/
    public static List<ItemStack> allTrinkets(LivingEntity entity) {
        return getCuriosInventory(entity).map(h -> h.findCurios(s -> true).stream().map(SlotResult::stack).toList()).orElse(Collections.emptyList());
    }
    public static List<Tuple<IDynamicStackHandler, Integer>> trinketsWithSlots(LivingEntity entity) {
        return trinketsWithSlots(entity, s -> true);
    }
    public static List<Tuple<IDynamicStackHandler, Integer>> trinketsWithSlots(LivingEntity entity, Predicate<ItemStack> filter) {
        List<Tuple<IDynamicStackHandler, Integer>> pairs = new ArrayList<>();
        getCuriosInventory(entity).ifPresent(c -> c.getCurios().values().forEach(group -> {
            for (int i = 0; i < group.getSlots(); i++) {
                if (filter.test(group.getStacks().getStackInSlot(i))) pairs.add(new Tuple<>(group.getStacks(), i));
            }
        }));
        return pairs;
    }

    public static void replaceTrinketSlot(List<Tuple<IDynamicStackHandler, Integer>> pairs, int slot) {
        if (slot < 1 || slot >= pairs.size()) return;
        List<ItemStack> copys = new ArrayList<>(pairs.stream().map(p -> p.getA().getStackInSlot(p.getB()).copy()).toList());
        ItemStack stack = copys.get(slot).copy();
        for (int i = slot; i > 0; i--) {
            copys.set(i, copys.get(i - 1));
        }
        copys.set(0, stack);
        for (int i = 0; i < pairs.size(); i++) {
            pairs.get(i).getA().setStackInSlot(pairs.get(i).getB(), copys.get(i));
        }
    }

    public static CardPileInventory getCardPack(Player player) {
        if (player instanceof ServerPlayer sp) return PVPGameEvents.PLAYER_CARD_PACKS.getOrDefault(sp, new CardPileInventory(player));
        return new CardPileInventory(player);
    }

    public static boolean shouldReachLong(LivingEntity entity) {
        for (var hand : InteractionHand.values()) {
            ItemStack stack = entity.getItemInHand(hand);
            if (stack.is(ModItems.DISCARD) || stack.is(ModItems.JUEDOU) || stack.is(ModItems.TOO_HAPPY_ITEM)) return true;
        }
        return false;
    }

    /**判断牌堆和背包中是否有符合条件的卡牌*/
    public static boolean hasCard(LivingEntity entity, Predicate<ItemStack> predicate) {
        return !getCard(entity, predicate).isEmpty();
    }
    /**获取牌堆或背包中的一张符合条件的卡牌*/
    public static ItemStack getCard(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity instanceof Player player) {
            for (ItemStack card : getCardPack(player).cards) {
                if (predicate.test(card)) return card;
            }
        }
        return getItem(entity, predicate);
    }

    /**判断生物是否有某个物品*/
    public static boolean hasItem(@NotNull LivingEntity entity, Predicate<ItemStack> predicate) {
        return !getItem(entity, predicate).isEmpty();
    }
    /**获取玩家背包中第一个符合条件的物品，或者生物的符合条件的主副手物品*/
    public static ItemStack getItem(@NotNull LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity instanceof Player player) {
            for (var stack : player.getInventory().items) if (predicate.test(stack)) return stack;
        } else if (predicate.test(entity.getMainHandItem())) return entity.getMainHandItem();
        if (predicate.test(entity.getOffhandItem())) return entity.getOffhandItem();
        return ItemStack.EMPTY;
    }

    /**播放语音*/
    public static void voice(LivingEntity entity, SoundEvent sound, float... volume) {
        if (entity.level() instanceof ServerLevel world) {
            float v = volume.length > 0 ? volume[0] : 2;
            world.playSeededSound(null, entity, Holder.direct(sound), SoundSource.PLAYERS, v, 1.0F, entity.getRandom().nextLong());
        }
    }
    public static void voice(LivingEntity entity, Item item, float... volume) {
        voice(entity, BuiltInRegistries.ITEM.getKey(item).getPath(), volume);
    }
    public static void voice(LivingEntity entity, ItemStack stack, float... volume) {
        voice(entity, stack.getItem(), volume);
    }
    public static void voice(LivingEntity entity, String name, float... volume) {
        voice(entity, getSound("dabaosword", name), volume);
    }
    public static SoundEvent getSound(String namespace, String path) {
        return Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(namespace, path))).value();
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
        if (pile && entity instanceof Player player) for (var stack : getCardPack(player).cards) if (p.test(stack)) items.add(stack);
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

    public static void draw(LivingEntity entity, int... count) {
        int num = count.length > 0 ? count[0] : 1;
        for (int n = 0; n < num; n++) {
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

    public static ItemStack customLoot(LivingEntity entity, String path) {
        LootTable lt = Objects.requireNonNull(entity.level().getServer()).reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("dabaosword", path)));
        LootParams set = (new LootParams.Builder((ServerLevel) entity.level())).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.THIS_ENTITY, entity).create(LootContextParamSets.GIFT);
        var list = lt.getRandomItems(set);
        for (var stack : list) return stack;
        return ItemStack.EMPTY;
    }

    private static final List<ItemStack> CARD_PILE = new ArrayList<>();
    public static ItemStack newCard() {
        if (CARD_PILE.isEmpty()) {
            for (ItemStack stack : CardSuitAndRank.ALL_CARDS) CARD_PILE.add(stack.copy());
            Collections.shuffle(CARD_PILE);
            DabaoSword.LOGGER.info("Shuffled card pile");
        }
        return CARD_PILE.removeFirst();
    }
    public static ItemStack newCard(Item item) {return newCard(p(item));}
    public static ItemStack newCard(Predicate<ItemStack> predicate) {
        List<ItemStack> list = CardSuitAndRank.ALL_CARDS.stream().filter(predicate).toList();
        if (list.isEmpty()) return ItemStack.EMPTY;
        return list.get(new Random().nextInt(list.size())).copy();
    }

    public static void give(LivingEntity entity, ItemStack stack, int... pickupDelay) {
        if (entity instanceof Player player) {
            ItemEntity item = player.drop(stack, false);
            if (item == null) return;
            item.setInvulnerable(true);
            int delay = pickupDelay.length > 0 ? pickupDelay[0] : 0;
            item.setPickUpDelay(delay);
            item.setTarget(player.getUUID());
            item.addTag("follow_owner");
            return;
        }
        if (entity.getMainHandItem().isEmpty()) entity.setItemInHand(InteractionHand.MAIN_HAND, stack);
        else if (entity.getOffhandItem().isEmpty()) entity.setItemInHand(InteractionHand.OFF_HAND, stack);
    }

    /**查找最近的一个符合条件的实体，不包括第一个参数实体本身*/
    public static @Nullable <T extends Entity> T getClosestEntity(Entity entity, Class<T> clazz, double boxLength, Predicate<T> predicate) {
        if (entity.level() instanceof ServerLevel world) {
            AABB box = new AABB(entity.getOnPos()).inflate(boxLength);
            List<T> entities = world.getEntitiesOfClass(clazz, box, predicate.and(e -> e != entity));
            if (!entities.isEmpty()) {
                Map<Float, T> map = new HashMap<>();
                for (var e : entities) {
                    map.put(e.distanceTo(entity), e);
                }
                float min = Collections.min(map.keySet());
                return map.get(min);
            }
        }
        return null;
    }

    /**仿照1.20代码写的获取物品NBT的方法*/
    public static CompoundTag getOrCreateNbt(ItemStack stack) {
        if (stack.isEmpty()) return new CompoundTag();
        var component = stack.get(DataComponents.CUSTOM_DATA);
        if (component == null) return new CompoundTag();
        return component.copyTag();
    }
    public static void setNbt(ItemStack stack, CompoundTag nbt) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }

    public static void openFullInv(Player player, LivingEntity target, boolean editable) {
        if (player.level().isClientSide) return;
        FriendlyByteBuf b = new FriendlyByteBuf(Unpooled.buffer());
        b.writeInt(target.getId()); b.writeBoolean(editable);
        player.openMenu(new SimpleMenuProvider(((i, inv, player1) -> new FullInvScreenHandler(i, inv, b)), target.getName()), (buf -> {buf.writeInt(target.getId()); buf.writeBoolean(editable);}));
    }

    public static void openInv(Player player, Player owner, Player target, Component title, ItemStack stack, boolean equip, boolean armor, int cards) {
        if (player.level().isClientSide) return;
        var tempInv = new TempInventory(player, owner, stack, cards, equip, armor);
        var rows = tempInv.rowsToShow;
        player.openMenu(new SimpleMenuProvider((i, inv, player1) -> new PlayerInvScreenHandler(i, tempInv, target, rows), title), (buf -> {buf.writeInt(target.getId()); buf.writeUtf(rows.toString());}));
    }

    public static void openMenu(Player player, Player target, ItemStack stack, List<ItemStack> stacks, Component title) {
        if (player.level().isClientSide) return;
        var tempInv = new TempInventory(player, stack, stacks);
        var rows = tempInv.rowsToShow;
        player.openMenu(new SimpleMenuProvider(((i, inv, player1) -> new PlayerInvScreenHandler(i, tempInv, target, rows)), title), (buf -> {buf.writeInt(target.getId()); buf.writeUtf(rows.toString());}));
    }

    public static ItemStack paibei(int... n) {return new ItemStack(ModItems.GAIN_CARD, n.length > 0 ? n[0] : 1);}

    public static void closeGUI(Player player) {
        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 1,2,false,false,false));
    }

    public static Holder<Enchantment> getEntry(ResourceKey<Enchantment> key, Entity... entity) {
        Entity e = entity.length > 0 ? entity[0] : null;
        Registry<Enchantment> enchantments;
        if (e == null) enchantments = DabaoSword.server.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        else enchantments = e.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        return enchantments.getHolder(key).orElse(null);
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

    /**一个用于简便执行多条服务器指令的方法*/
    public static void excuteServerCommand(Entity entity, String... commands) {
        if (entity.level() instanceof ServerLevel world) {
            var server = world.getServer();
            var dispatcher = server.getCommands().getDispatcher();
            var commandSource = entity.createCommandSourceStack().withPermission(2).withSuppressedOutput();
            for (String command : commands) {
                if (command.startsWith("/")) command = command.substring(1);
                try {
                    var results = dispatcher.parse(command, commandSource);
                    dispatcher.execute(results);
                } catch (CommandSyntaxException e) {throw new RuntimeException(e);}
            }
        }
    }

    public static void title(ServerPlayer player, Component title) {
        player.connection.send(new ClientboundSetTitleTextPacket(title));
    }

    public static void subtitle(ServerPlayer player, Component sub) {
        player.connection.send(new ClientboundSetSubtitleTextPacket(sub));
    }

    public static List<LivingEntity> getSkillOwners(LivingEntity entity) {
        if (entity.level().isClientSide) return List.of();
        return new ArrayList<>(Objects.requireNonNull(entity.getServer()).getPlayerList().getPlayers());
    }

    public static List<Skill> getSkillsMayUse(LivingEntity entity) {
        Predicate<ItemStack> p = s -> {
            if (!(s.getItem() instanceof ISkill)) return false;
            if (s.getItem() instanceof SkillItem && entity.getTags().contains("duanchang")) return false;
            return s(s).lockOn() || !entity.hasEffect(ModItems.TIEJI);
        };
        return getCuriosInventory(entity).map(c -> c.findCurios(p).stream().map(SlotResult::stack).map(Skill::new).toList()).orElse(Collections.emptyList());
    }

    public static int getResult(Trigger t, LivingEntity owner, LivingEntity triggered, ExData exData) {
        for (Skill skill : getSkillsMayUse(owner)) {
            for (var data : skill.data()) {
                if (!List.of(data.trigger()).contains(t) || !data.relation().test(owner, triggered, exData.source)) continue;
                int i = data.apply(owner, triggered, skill, exData);
                if (i > 0) return i;
            }
        }
        return 0;
    }

    public static String getTagValue(Entity entity, String name) {
        return entity.getTags().stream().filter(s -> s.startsWith(name + "_")).findFirst().map(s -> s.split("_")[1]).orElse("");
    }
    public static int getTagCount(Entity entity, String name) {
        return entity.getTags().stream().filter(s -> s.startsWith(name + "_")).findFirst().map(s -> Integer.parseInt(s.split("_")[1])).orElse(0);
    }

    public static boolean hasTag(Entity entity, String name) {
        return entity.getTags().stream().anyMatch(s -> s.startsWith(name));
    }

    public static void removeTag(Entity entity, String name) {
        var each = entity.getTags().iterator();
        while (each.hasNext()) { //仅移除第一个符合条件的标签
            if (each.next().startsWith(name)) {
                each.remove(); break;
            }
        }
    }

    public static void addTag(Entity entity, String name, int n) {
        removeTag(entity, name);
        entity.addTag(name + "_" + n);
    }
    public static void addTag(Entity entity, String name, String suffix) {
        entity.addTag(name + "_" + suffix);
    }
}
