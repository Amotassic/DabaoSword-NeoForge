package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import static com.amotassic.dabaosword.util.ModTools.*;

public class LongdanSkill extends SkillItem {
    public LongdanSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world && slotContext.entity() instanceof Player player && noTieji(slotContext.entity())) {
            ItemStack stack1 = player.getOffhandItem();
            if (world.getGameTime() % 20 == 0 && stack1.is(Tags.BASIC_CARD)) {
                stack1.shrink(1);
                if (stack1.is(Tags.SHA)) give(player, new ItemStack(ModItems.SHAN.get()));
                if (stack1.getItem() == ModItems.SHAN.get()) give(player, new ItemStack(ModItems.SHA.get()));
                if (stack1.getItem() == ModItems.PEACH.get()) give(player, new ItemStack(ModItems.JIU.get()));
                if (stack1.getItem() == ModItems.JIU.get()) give(player, new ItemStack(ModItems.PEACH.get()));
                voice(player, Sounds.LONGDAN.get());
            }
        }
        super.curioTick(slotContext, stack);
    }
}
