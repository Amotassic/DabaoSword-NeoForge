package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.api.skill.ISkill;
import com.amotassic.dabaosword.api.skill.Relation;
import com.amotassic.dabaosword.api.skill.SkillExecutor;
import com.amotassic.dabaosword.api.skill.SkillInfo;
import com.amotassic.dabaosword.util.AllRegs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillCards {
    public static final Map<Item, List<SkillExecutor>> SKILL_MAP = new HashMap<>();

    public static Item SHENSU = AllRegs.Skills.SHENSU.get();
    public static Item MASHU = AllRegs.Skills.MASHU.get();
    public static Item FEIYING = AllRegs.Skills.FEIYING.get();

    public static void addSkillEffect() {
        var itemList = BuiltInRegistries.ITEM.stream().filter(item -> item instanceof ISkill).toList();
        for (Item skill : itemList) {
            List<SkillExecutor> effectDatas = new ArrayList<>();
            Class<?> skillClass = skill.getClass();
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            for (Method method : skillClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(SkillInfo.class)) {
                    SkillInfo info = method.getAnnotation(SkillInfo.class);
                    MethodHandle handle;
                    try {
                        handle = lookup.unreflect(method).bindTo(skill);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    SkillExecutor effectData = new SkillExecutor(info.trigger(), Relation.getPredicate(info.relation()), handle);
                    effectDatas.add(effectData);
                }
            }
            SKILL_MAP.put(skill, effectDatas);
        }
    }
}
