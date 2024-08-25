package com.amotassic.dabaosword.item.equipment;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

public class RattanArmor extends EquipmentItem {
    public RattanArmor(Properties p_41383_) {super(p_41383_);}

    //实现渡江不沉的效果，代码来自https://github.com/focamacho/RingsOfAscension/中的水上行走戒指
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if(entity.isShiftKeyDown()) return;

        BlockPos entityPos = entity.getOnPos();

        boolean water = entity.level().getFluidState(new BlockPos(entityPos.getX(),
                        (int) (entity.getBoundingBox().min(Direction.Axis.Y)), entityPos.getZ()))
                .is(Fluids.WATER);

        if(water) {
            Vec3 motion = entity.getDeltaMovement();
            entity.setDeltaMovement(motion.x, 0.0D, motion.z);
            entity.fallDistance = 0;
            entity.setOnGround(true);
        }
        super.curioTick(slotContext, stack);
    }
}
