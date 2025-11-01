package com.amotassic.dabaosword.network;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.ui.PileScreenHandler;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.amotassic.dabaosword.util.ModTools.*;

public record SimplePayload(String name, String value) implements CustomPacketPayload {
    public static final Type<SimplePayload> ID = new Type<>(ResourceLocation.parse("dabaosword:simple"));
    public static final StreamCodec<FriendlyByteBuf, SimplePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SimplePayload::name,
            ByteBufCodecs.STRING_UTF8, SimplePayload::value,
            SimplePayload::new
    );
    public static final String ACTIVE_SKILL =       "active_skill";
    public static final String SHENSU =             "shensu";
    public static final String QUICK_SWAP =         "quick_swap";
    public static final String CARD_PILE =          "card_pile";
    public static final String CANCEL_DODGE =       "cancel_dodge";
    public static final String VIEW_INFO =          "view_info";
    public static final String REPLACE_TRINKET =    "replace_trinket";
    private static final Map<String, BiConsumer<ServerPlayer, String>> HANDLERS = Map.of(
            ACTIVE_SKILL,       SimplePayload::activeSkill,
            SHENSU,             SimplePayload::shensu,
            QUICK_SWAP,         SimplePayload::quickSwap,
            CARD_PILE,          SimplePayload::cardPile,
            CANCEL_DODGE,       SimplePayload::cancelDodge,
            VIEW_INFO,          SimplePayload::viewInfo,
            REPLACE_TRINKET,    SimplePayload::replaceTrinket
    );

    public static void execute(SimplePayload payload, IPayloadContext context) {
        if (HANDLERS.containsKey(payload.name)) HANDLERS.get(payload.name).accept((ServerPlayer) context.player(), payload.value);
    }

    public @NotNull Type<? extends CustomPacketPayload> type() {return ID;}

    private static void activeSkill(ServerPlayer player, String target) {
        if (player.hasEffect(ModItems.TIEJI)) {
            player.displayClientMessage(Component.translatable("effect.tieji.tip").withStyle(ChatFormatting.RED), true);
            return;
        }
        int id = Integer.parseInt(target);
        LivingEntity entity = (LivingEntity) player.level().getEntity(id);
        for (var skill : getSkillsMayUse(player)) if (player != entity && skill.activeSkill(player, skill, entity)) return;
        for (var skill : getSkillsMayUse(player)) if (skill.activeSkill(player, skill)) return;
    }

    private static void shensu(ServerPlayer player, String value) {
        ItemStack stack = trinketItem(SkillCards.SHENSU, player);
        if (stack.isEmpty()) return;
        float speed = Float.parseFloat(value);
        CompoundTag nbt = getOrCreateNbt(stack); nbt.putFloat("speed", speed);
        setNbt(stack, nbt);
        //if (getOrCreateNbt(stack).getFloat("speed") > 0) player.sendMessage(Text.literal("Speed: " + speed), true);
    }

    private static void quickSwap(ServerPlayer player, String value) {
        openInv(player, player, player, Component.translatable("key.dabaosword.select_card"), ItemStack.EMPTY, false, false, 3);
    }

    private static void cardPile(ServerPlayer player, String value) {
        if (hasTrinket(ModItems.CARD_PILE, player)) player.openMenu(new SimpleMenuProvider((id, inv, p) -> new PileScreenHandler(id, inv), Component.translatable("card_pile.title")), (buf -> {}));
    }

    private static void cancelDodge(ServerPlayer player, String value) {
        var pair = getDamage(player);
        if (pair == null) return;
        //取消闪避后，先移除记录的伤害，给玩家一个CD防止闪触发
        ItemStack stack = trinketItem(ModItems.CARD_PILE, player);
        CompoundTag nbt = getOrCreateNbt(stack); nbt.remove("DamageDodged");
        setNbt(stack, nbt);
        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN2,2,0,false,false,false));
        player.hurt(pair.getA().getA(), pair.getA().getB());
        give(player, pair.getB());
    }

    private static void viewInfo(ServerPlayer player, String value) {
        Player target = getClosestEntity(player, Player.class, 100, LivingEntity::isAlive);
        if (target != null) openFullInv(player, target, false);
    }

    private static void replaceTrinket(ServerPlayer player, String slot) {
        replaceTrinketSlot(trinketsWithSlots(player, s -> s.is(Tags.SKILLS)), Integer.parseInt(slot));
    }

    @OnlyIn(Dist.CLIENT)
    public static void sendToServer(String name) {sendToServer(name, "");}
    @OnlyIn(Dist.CLIENT)
    public static void sendToServer(String name, String value) {
        PacketDistributor.sendToServer(new SimplePayload(name, value));
    }
}
