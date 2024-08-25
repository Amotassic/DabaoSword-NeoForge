package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

public class QuanjiSkill extends SkillItem {
    public QuanjiSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && !user.isShiftKeyDown()) {
            if (new Random().nextFloat() < 0.5) {voice(user, Sounds.ZILI1.get());} else {voice(user, Sounds.ZILI2.get());}
        }
        return super.use(world, user, hand);
    }
}
