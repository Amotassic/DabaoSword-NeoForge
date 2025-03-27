package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Set;

import static com.amotassic.dabaosword.util.ModTools.excuteServerCommand;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class ShandianItem extends CardItem.Armoury {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world instanceof ServerLevel sw && hand == InteractionHand.MAIN_HAND) {
            String[] command = {"weather thunder 15s"};
            excuteServerCommand(user, command, true);
            //world.setWeather(0, 15, true, true);

            Set<LivingEntity> targets = new HashSet<>(sw.players());
            AABB box = new AABB(user.getOnPos()).inflate(10);
            targets.addAll(world.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive));
            onUse(user, user.getMainHandItem(), targets.toArray(new LivingEntity[0]));

            return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        if (target != user) voice(target, this);
        target.addEffect(new MobEffectInstance(ModItems.SHANDIAN, 299));
    }

    @Override public boolean askForWuxie() {return true;}
}
