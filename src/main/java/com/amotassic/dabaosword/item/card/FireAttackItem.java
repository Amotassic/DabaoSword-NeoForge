package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.amotassic.dabaosword.util.ModTools.*;

public class FireAttackItem extends CardItem {
    public FireAttackItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            Vec3 momentum = user.getForward().scale(3);
            LargeFireball fireballEntity = new LargeFireball(world, user, momentum, 3);
            fireballEntity.setPos(user.getX(), user.getY(0.5) + 0.5, user.getZ());
            world.addFreshEntity(fireballEntity);
            if (!user.isCreative()) {user.getItemInHand(hand).shrink(1);}
            jizhi(user); benxi(user);
            voice(user, Sounds.HUOGONG.get());
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
