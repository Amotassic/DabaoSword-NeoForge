package com.amotassic.dabaosword.network;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.event.listener.ActiveSkillListener;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.theillusivec4.curios.api.CuriosApi;

import static com.amotassic.dabaosword.util.ModTools.trinketItem;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ServerNetworking {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ActiveSkillPayload.ID, ActiveSkillPayload.CODEC, (payload, context) -> {
            Player player = context.player();
            Player target = (Player) player.level().getEntity(payload.id());
            var optional = CuriosApi.getCuriosInventory(player);
            if (optional.isPresent()) {
                var allEquipped = optional.get().getEquippedCurios();
                for (int i = 0; i < allEquipped.getSlots(); i++) {
                    ItemStack stack = allEquipped.getStackInSlot(i);
                    if(stack.getItem() instanceof SkillItem.ActiveSkillWithTarget && target != player) {
                        NeoForge.EVENT_BUS.post(new ActiveSkillListener(player, stack, target));
                        return;
                    }
                    if(stack.getItem() instanceof SkillItem.ActiveSkill && target == player) {
                        NeoForge.EVENT_BUS.post(new ActiveSkillListener(player, stack, player));
                        return;
                    }
                }
            }
        });

        registrar.playToServer(ShensuPayload.ID, ShensuPayload.CODEC, (p, c) -> {
            Player player = c.player();
            float speed = p.f();
            ItemStack stack = trinketItem(SkillCards.SHENSU.get(), player);
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
