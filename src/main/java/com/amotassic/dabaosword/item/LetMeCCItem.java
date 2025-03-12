package com.amotassic.dabaosword.item;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.amotassic.dabaosword.command.InfoCommand.openFullInv;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class LetMeCCItem extends Item {
    public LetMeCCItem() {super(new Item.Properties().stacksTo(1));}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.let_me_cc.tooltip"));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand usedHand) {
        if (!user.level().isClientSide && usedHand == InteractionHand.MAIN_HAND) {
            voice(user, Sounds.LET_ME_CC, 1);
            openFullInv(user, entity, true);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, user, entity, usedHand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand usedHand) {
        if (!world.isClientSide && usedHand == InteractionHand.MAIN_HAND) {
            if (!user.isShiftKeyDown()) {
                LivingEntity closest = getClosestEntity(user, LivingEntity.class, 10, entity -> entity != user);
                if (closest != null) {
                    voice(user, Sounds.LET_ME_CC, 1);
                    openFullInv(user, closest, true);
                    return InteractionResultHolder.success(user.getItemInHand(usedHand));
                }
            } else {
                voice(user, Sounds.LET_ME_CC, 1);
                openFullInv(user, user, true);
                return InteractionResultHolder.success(user.getItemInHand(usedHand));
            }
        }
        return super.use(world, user, usedHand);
    }

    public static @Nullable <T extends Entity> T getClosestEntity(Entity entity, Class<T> clazz, double boxLength, Predicate<T> predicate) {
        if (entity.level() instanceof ServerLevel world) {
            AABB box = new AABB(entity.getOnPos()).inflate(boxLength);
            List<T> entities = world.getEntitiesOfClass(clazz, box, predicate);
            if (!entities.isEmpty()) {
                Map<Float, T> map = new HashMap<>();
                for (var e : entities) {
                    map.put(e.distanceTo(entity), e);
                }
                float min = Collections.min(map.keySet());
                return map.values().stream().toList().get(map.keySet().stream().toList().indexOf(min));
            }
        }
        return null;
    }
}
