package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.equipment.Equipment;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class AttackEntityHandler {

    @SubscribeEvent
    public static void AttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if (player.level() instanceof ServerLevel && entity instanceof LivingEntity target) {
            if (!(player.getMainHandItem().is(ModItems.JUEDOU) || player.getMainHandItem().is(ModItems.DISCARD))) {
                var optional = CuriosApi.getCuriosInventory(player);
                if (optional.isPresent()) {
                    var handler = optional.get().getEquippedCurios();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (stack.getItem() instanceof SkillItem skill) skill.preAttack(stack, target, player);
                        if (stack.getItem() instanceof Equipment skill) skill.preAttack(stack, target, player);
                    }
                }
            }
        }
    }
}
