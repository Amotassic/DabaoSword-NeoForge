package com.amotassic.dabaosword.network;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.CardPileInventory;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.ui.PileScreenHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ServerNetworking {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ActiveSkillPayload.ID, ActiveSkillPayload.CODEC, (payload, context) -> {
            Player player = context.player();
            if (player.hasEffect(ModItems.TIEJI)) {
                player.displayClientMessage(Component.translatable("effect.tieji.tip").withStyle(ChatFormatting.RED), true);
                return;
            }
            Player target = (Player) player.level().getEntity(payload.id());
            for (var stack : allTrinkets(player)) {
                if(stack.getItem() instanceof SkillItem.ActiveSkillWithTarget skill && target != player) {
                    skill.activeSkill(player, stack, target);
                    return;
                }
                if(stack.getItem() instanceof SkillItem.ActiveSkill skill && target == player) {
                    skill.activeSkill(player, stack, player);
                    return;
                }
            }
        });

        registrar.playToServer(ShensuPayload.ID, ShensuPayload.CODEC, (p, c) -> {
            Player player = c.player();
            float speed = p.f();
            ItemStack stack = trinketItem(SkillCards.SHENSU, player);
            if (!stack.isEmpty()) {
                CompoundTag nbt = getOrCreateNbt(stack); nbt.putFloat("speed", speed);
                setNbt(stack, nbt);
                //if (getOrCreateNbt(stack).getFloat("speed") > 0) player.displayClientMessage(Component.literal("Speed: " + speed), true);
            }
        });

        registrar.playToServer(QuickSwapPayload.ID, QuickSwapPayload.CODEC, (p, c) -> {
            Player player = c.player();
            int i = p.id();
            if (i == 0) openInv(player, player, Component.translatable("key.dabaosword.select_card"), new ItemStack(ModItems.WANJIAN), true, false, false, 2);
            if (i == 1) openInv(player, player, Component.translatable("key.dabaosword.select_card"), new ItemStack(ModItems.SUNSHINE_SMILE), true, false, false, 3);
            if (i == 2) player.openMenu(new SimpleMenuProvider((id, inv, player1) -> new PileScreenHandler(id, inv, new CardPileInventory(inv.player)), Component.translatable("card_pile.title")), (buf -> buf.writeInt(0)));
            if (i == 3) {
                var pair = getDamage(player);
                if (pair != null) {
                    //取消闪避后，先移除记录的伤害，给玩家一个CD防止闪触发
                    ItemStack stack = trinketItem(ModItems.CARD_PILE, player);
                    CompoundTag nbt = getOrCreateNbt(stack); nbt.remove("DamageDodged");
                    setNbt(stack, nbt);
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN2,2,0,false,false,false));
                    player.hurt(pair.getA().getA(), pair.getA().getB());
                    give(player, pair.getB());
                }
            }
        });
    }
}
