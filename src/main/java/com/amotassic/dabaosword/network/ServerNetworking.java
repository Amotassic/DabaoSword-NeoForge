package com.amotassic.dabaosword.network;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.amotassic.dabaosword.util.ModTools.allTrinkets;
import static com.amotassic.dabaosword.util.ModTools.trinketItem;

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
            if (stack != null) {
                CustomData component = stack.get(DataComponents.CUSTOM_DATA);
                if (component != null) {
                    CompoundTag nbt = component.copyTag(); nbt.putFloat("speed", speed);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                }
                //if (Objects.requireNonNull(stack.get(DataComponents.CUSTOM_DATA)).copyTag().getFloat("speed") > 0) player.displayClientMessage(Component.literal("Speed: " + speed), true);
            }
        });
    }
}
