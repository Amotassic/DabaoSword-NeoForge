package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

public class HuojiSkill extends SkillItem {
    public HuojiSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide && entity instanceof Player player && noTieji(entity)) {
            ItemStack stack1 = player.getOffhandItem();
            int cd = getCD(stack);
            if (cd == 0 && !stack1.isEmpty() && stack1.is(Tags.BASIC_CARD)) {
                setCD(stack, 15);
                viewAs(player, Tags.BASIC_CARD, ModItems.FIRE_ATTACK.get(), Sounds.HUOJI1.get(), Sounds.HUOJI2.get());
            }
        }
        super.curioTick(slotContext, stack);
    }

    public static void viewAs(@NotNull Player player, TagKey<Item> tag, Item item, SoundEvent sound1, SoundEvent sound2) {
        ItemStack stack = player.getOffhandItem();
        if (!stack.isEmpty() && stack.is(tag)) {
            stack.shrink(1);
            give(player, item.getDefaultInstance());
            if (new Random().nextFloat() < 0.5) {voice(player, sound1);} else {voice(player, sound2);}
        }
    }
}
