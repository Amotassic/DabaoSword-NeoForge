package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import static com.amotassic.dabaosword.api.event.CardEvents.cardUsePre;
import static com.amotassic.dabaosword.util.ModTools.excuteServerCommand;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class ShandianItem extends CardItem {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (cardUsePre(user, user.getMainHandItem(), null)) return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {
        if (user.level() instanceof ServerLevel world) {
            String[] command = {"weather thunder 15s"};
            excuteServerCommand(user, command, true);
            //world.setWeather(0, 15, true, true);
            world.players().forEach(player -> {
                player.addEffect(new MobEffectInstance(ModItems.SHANDIAN, 299));
                if (player != user) voice(player, Sounds.SHANDIAN);
            });
            AABB box = new AABB(user.getOnPos()).inflate(10);
            for (LivingEntity near : world.getEntitiesOfClass(LivingEntity.class, box, e -> !(e instanceof Player))) {
                near.addEffect(new MobEffectInstance(ModItems.SHANDIAN, 299));
            }
        }
    }
}
