package com.amotassic.dabaosword.network;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.event.listener.ActiveSkillListener;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.theillusivec4.curios.api.CuriosApi;

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
    }
}
