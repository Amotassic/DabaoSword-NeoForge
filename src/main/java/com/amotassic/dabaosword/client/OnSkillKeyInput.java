package com.amotassic.dabaosword.client;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.network.ActiveSkillPayload;
import com.amotassic.dabaosword.network.QuickSwapPayload;
import com.amotassic.dabaosword.network.ShensuPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;

import static com.amotassic.dabaosword.util.ModTools.hasTrinket;

@EventBusSubscriber(value = Dist.CLIENT, modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class OnSkillKeyInput {

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        var user = Minecraft.getInstance().player;
        var result = Minecraft.getInstance().hitResult;
        if (user != null) {
            if (DabaoSwordClient.SELECT_CARD.consumeClick()) {
                PacketDistributor.sendToServer(new QuickSwapPayload(user.getId()));
                return;
            }

            if (DabaoSwordClient.ACTIVE_SKILL.consumeClick()) {
                if (haveSkill(user, stack -> stack.getItem() instanceof SkillItem.ActiveSkillWithTarget)) {
                    if (result != null && result.getType() == HitResult.Type.ENTITY) {
                        if (((EntityHitResult) result).getEntity() instanceof Player player) {
                            PacketDistributor.sendToServer(new ActiveSkillPayload(player.getId()));
                            return;
                        }
                    }
                }
                if (haveSkill(user, stack -> stack.getItem() instanceof SkillItem.ActiveSkill)) PacketDistributor.sendToServer(new ActiveSkillPayload(user.getId()));
            }
        }
    }

    @SubscribeEvent
    public static void endClientTick(ClientTickEvent.Post event) {
        var player = Minecraft.getInstance().player;
        if (player != null && hasTrinket(SkillCards.SHENSU, player)) {
            Vec3 lastPos = new Vec3(player.xOld, player.yOld, player.zOld);
            float speed = (float) (player.position().distanceTo(lastPos) * 20);
            if (speed > 0) PacketDistributor.sendToServer(new ShensuPayload(speed));
        }
    }

    public static boolean haveSkill(Player player, Predicate<ItemStack> predicate) {
        var optional = CuriosApi.getCuriosInventory(player);
        if (optional.isPresent()) {
            var handler = optional.get().findFirstCurio(predicate);
            if (handler.isPresent()) {
                return handler.get().stack() != null;
            }
        }
        return false;
    }
}
