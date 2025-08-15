package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class AttackEntityHandler {

    @SubscribeEvent
    public static void AttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if (!entity.level().isClientSide && !player.isSpectator()) {
            //决斗等物品虽然手长，但过远时普通伤害无效
            if (ModTools.shouldReachLong(player) && entity.distanceTo(player) >= 5) {
                event.setCanceled(true);
                return;
            }
            if (entity instanceof LivingEntity target) for (var skill : ModTools.getSkillsMayUse(player)) {
                skill.item.preAttack(player, target, skill);
            }
        }
    }
}
