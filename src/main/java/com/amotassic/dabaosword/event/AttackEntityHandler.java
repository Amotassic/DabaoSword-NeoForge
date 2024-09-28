package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.equipment.Equipment;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import static com.amotassic.dabaosword.util.ModTools.allTrinkets;
import static com.amotassic.dabaosword.util.ModTools.canTrigger;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class AttackEntityHandler {

    @SubscribeEvent
    public static void AttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if (player.level() instanceof ServerLevel && entity instanceof LivingEntity target) {
            if (!(player.getMainHandItem().is(ModItems.JUEDOU) || player.getMainHandItem().is(ModItems.DISCARD))) {
                for (var stack : allTrinkets(player)) {
                    if (stack.getItem() instanceof SkillItem skill && canTrigger(skill, player)) skill.preAttack(stack, target, player);
                    if (stack.getItem() instanceof Equipment skill) skill.preAttack(stack, target, player);
                }
            }
        }
    }
}
