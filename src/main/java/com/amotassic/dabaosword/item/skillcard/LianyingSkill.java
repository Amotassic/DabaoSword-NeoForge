package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import static com.amotassic.dabaosword.util.ModTools.*;

public class LianyingSkill extends SkillItem {
    public LianyingSkill(Properties p_41383_) {super(p_41383_);}

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.level() instanceof ServerLevel world && entity instanceof Player player) {
            int cd = getCD(stack);
            if (world.getGameTime() % 20 == 0 && cd == 1) { //确保一秒内只触发一次
                draw(player, 1);
                voice(player, Sounds.LIANYING.get());
            }
        }
        super.curioTick(slotContext, stack);
    }
}
