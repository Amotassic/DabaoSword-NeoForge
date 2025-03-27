package com.amotassic.dabaosword.item.card.equipment;

import com.amotassic.dabaosword.api.skill.Skill;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class Mount extends Equipment {

    public void addTip(Skill skill, List<Component> tooltip) {tooltip.add(getTip());}

    public static class Attack extends Mount {
        public int getExtraReach(LivingEntity entity, Skill skill) {return 1;}
    }

    public static class Defend extends Mount {
        public int getDefend(LivingEntity entity, Skill skill) {return 1;}
    }
}
