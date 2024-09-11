package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.event.listener.CardDiscardListener;
import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.ShensuSkill;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.amotassic.dabaosword.util.ModTools.*;

public class ModifyDamage {
    //注入"applyArmorToDamage"以修改生物受到的伤害值
    public static float modify(LivingEntity entity, DamageSource source, float value) {
        ItemStack head = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = entity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = entity.getItemBySlot(EquipmentSlot.FEET);
        boolean noArmor = head.isEmpty() && chest.isEmpty() && legs.isEmpty() && feet.isEmpty();

        float multiply = 0; //倍率增伤乘区
        float add = 0; //固定数值加减伤害区

        if (source.getDirectEntity() instanceof LivingEntity attacker) {
            if (noArmor || hasTrinket(SkillCards.POJUN.get(), attacker)) {
                //古锭刀对没有装备的生物伤害增加 限定版翻倍 卡牌版加5
                if (attacker.getMainHandItem().getItem() == ModItems.GUDINGDAO.get()) multiply += 1;
                if (hasTrinket(ModItems.GUDING_WEAPON.get(), attacker)) add += 5;
            }

            //排异技能：攻击伤害增加
            if (hasTrinket(SkillCards.QUANJI.get(), attacker)) {
                ItemStack stack = trinketItem(SkillCards.QUANJI.get(), attacker);
                int quan = getTag(stack);
                if (quan > 0) {
                    if (quan > 4 && entity instanceof Player player) draw(player, 2);
                    setTag(stack, quan/2);
                    voice(attacker, Sounds.PAIYI.get());
                    add += quan;
                }
            }

            //烈弓：命中后加伤害，至少为5
            if (hasTrinket(SkillCards.LIEGONG.get(), attacker) && !attacker.hasEffect(ModItems.COOLDOWN)) {
                float f = Math.max(13 - attacker.distanceTo(entity), 5);
                attacker.addEffect(new MobEffectInstance(ModItems.COOLDOWN, (int) (40 * f),0,false,false,true));
                voice(attacker, Sounds.LIEGONG.get());
                add += f;
            }

            if (hasTrinket(SkillCards.SHENSU.get(), attacker) && !attacker.hasEffect(ModItems.COOLDOWN)) {
                float walkSpeed = 4.317f;
                float speed = (float) ShensuSkill.speed;
                if (speed > walkSpeed) {
                    float m = (speed - walkSpeed) / walkSpeed / 2;
                    attacker.addEffect(new MobEffectInstance(ModItems.COOLDOWN, (int) (5 * 20 * m),0,false,false,true));
                    if (attacker instanceof Player player) player.displayClientMessage(Component.translatable("shensu.info", speed, m), false);
                    voice(attacker, Sounds.SHENSU.get());
                    multiply += m;
                }
            }
        }

        //穿藤甲时，若承受火焰伤害，则 战火燃尽，嘤熊胆！（伤害大于5就只加5）
        if (source.is(DamageTypeTags.IS_FIRE) && hasTrinket(ModItems.RATTAN_ARMOR.get(), entity)) add += value > 5 ? 5 : value;

        //增伤区伤害结算
        value = value * (1 + multiply) + add;

        //白银狮子减伤
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && source.getEntity() instanceof LivingEntity && hasTrinket(ModItems.BAIYIN.get(), entity)) value *= 0.4f;

        return value;
    }

    public static boolean shouldCancel(LivingEntity entity, DamageSource source, float amount) {
        //无敌效果
        if (entity.hasEffect(ModItems.INVULNERABLE) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return true;

        //弹射物对藤甲无效
        if (source.is(DamageTypeTags.IS_PROJECTILE) && inrattan(entity)) {
            if (source.getDirectEntity() != null) source.getDirectEntity().discard();
            return true;
        }

        if (source.getDirectEntity() instanceof LivingEntity sourceEntity) {
            //沈佳宜防御效果
            if (!(sourceEntity instanceof Player) && entity.hasEffect(ModItems.DEFEND)) {
                if (Objects.requireNonNull(entity.getEffect(ModItems.DEFEND)).getAmplifier() >= 2) return true;
            }

            //若攻击者主手没有物品，则无法击穿藤甲
            if (inrattan(entity) && sourceEntity.getMainHandItem().isEmpty()) return true;

            //决斗等物品虽然手长，但过远时普通伤害无效
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && entity.distanceTo(sourceEntity) > 5) {
                if (sourceEntity.getMainHandItem().getItem() == ModItems.JUEDOU.get() || sourceEntity.getMainHandItem().getItem() == ModItems.DISCARD.get()) return true;
            }

            //被乐的生物无法造成普通攻击伤害
            if (sourceEntity.hasEffect(ModItems.TOO_HAPPY)) return true;
        }

        if (source.getDirectEntity() instanceof Wolf dog && dog.hasEffect(ModItems.INVULNERABLE)) {
            //被南蛮入侵的狗打中可以消耗杀以免疫伤害
            if (entity instanceof Player player) {
                if (getShaSlot(player) != -1) {
                    ItemStack stack = player.getMainHandItem().is(Tags.SHA) ? player.getMainHandItem() : shaStack(player);
                    if (stack.getItem() == ModItems.SHA.get()) voice(player, Sounds.SHA.get());
                    if (stack.getItem() == ModItems.FIRE_SHA.get()) voice(player, Sounds.SHA_FIRE.get());
                    if (stack.getItem() == ModItems.THUNDER_SHA.get()) voice(player, Sounds.SHA_THUNDER.get());
                    NeoForge.EVENT_BUS.post(new CardUsePostListener(player, stack, null));
                    dog.setHealth(0);
                    return true;
                }
                dog.setHealth(0);
            }
        }

        if (source.getEntity() instanceof LivingEntity attacker) {

            //翻面的生物（除了玩家）无法造成伤害
            if (!(attacker instanceof Player) && attacker.hasEffect(ModItems.TURNOVER)) return true;

            if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                //被乐的生物的弹射物无法造成伤害
                if (attacker.hasEffect(ModItems.TOO_HAPPY)) return true;
            }

            if (entity instanceof Player player) {
                //流离
                if (hasTrinket(SkillCards.LIULI.get(), player) && hasItemInTag(Tags.CARD, player) && !player.hasEffect(ModItems.INVULNERABLE) && !player.isCreative()) {
                    ItemStack stack = stackInTag(Tags.CARD, player);
                    LivingEntity nearEntity = getLiuliEntity(player, attacker);
                    if (nearEntity != null) {
                        player.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 10,0,false,false,false));
                        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 10,0,false,false,false));
                        voice(player, Sounds.LIULI.get());
                        NeoForge.EVENT_BUS.post(new CardDiscardListener(player, stack, 1, false));
                        nearEntity.invulnerableTime = 0; nearEntity.hurt(source, amount);
                        return true;
                    }
                }
            }

            //闪的被动效果
            final boolean trigger = baguaTrigger(entity);
            boolean hasShan = entity instanceof Player player ?
                    getShanSlot(player) != -1 || trigger : entity.getOffhandItem().getItem() == ModItems.SHAN.get() || trigger;
            boolean common = !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && hasShan && !entity.hasEffect(ModItems.COOLDOWN2) && !entity.hasEffect(ModItems.INVULNERABLE);
            boolean shouldShan = entity instanceof Player player ?
                    common && !entity.getTags().contains("juedou") && !player.isCreative() : common;
            if (shouldShan) {
                shan(entity, trigger);
                //虽然没有因为杀而触发闪，但如果攻击者的杀处于自动触发状态，则仍会消耗
                if (source.getDirectEntity() instanceof Player player && getShaSlot(player) != -1) {
                    ItemStack stack = player.getMainHandItem().is(Tags.SHA) ? player.getMainHandItem() : shaStack(player);
                    if (stack.getItem() == ModItems.SHA.get()) voice(player, Sounds.SHA.get());
                    if (stack.getItem() == ModItems.FIRE_SHA.get()) voice(player, Sounds.SHA_FIRE.get());
                    if (stack.getItem() == ModItems.THUNDER_SHA.get()) voice(player, Sounds.SHA_THUNDER.get());
                    NeoForge.EVENT_BUS.post(new CardUsePostListener(player, stack, entity));
                }
                return true;
            }

            if (hasTrinket(SkillCards.JUEQING.get(), attacker)) { //绝情效果
                entity.hurt(entity.damageSources().genericKill(), Math.min(7, amount));
                voice(attacker, Sounds.JUEQING.get(), 1);
                return true;
            }
        }

        return false;
    }

    private static @Nullable LivingEntity getLiuliEntity(Entity entity, LivingEntity attacker) {
        if (entity.level() instanceof ServerLevel world) {
            AABB box = new AABB(entity.getOnPos()).inflate(10);
            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, box, entity1 -> entity1 != entity && entity1 != attacker);
            if (!entities.isEmpty()) {
                Map<Float, LivingEntity> map = new HashMap<>();
                for (var e : entities) {
                    map.put(e.distanceTo(entity), e);
                }
                float min = Collections.min(map.keySet());
                return map.values().stream().toList().get(map.keySet().stream().toList().indexOf(min));
            }
        }
        return null;
    }

    private static boolean inrattan(LivingEntity entity) {return hasTrinket(ModItems.RATTAN_ARMOR.get(), entity);}

    private static boolean baguaTrigger(LivingEntity entity) {
        return hasTrinket(ModItems.BAGUA.get(), entity) && new Random().nextFloat() < 0.5;
    }

    private static void shan(LivingEntity entity, boolean bl) {
        ItemStack stack = bl ? ItemStack.EMPTY : shanStack(entity);
        int cd = bl ? 60 : 40;
        entity.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20,0,false,false,false));
        entity.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, cd,0,false,false,false));
        voice(entity, Sounds.SHAN.get());
        if (entity instanceof Player player) {
            NeoForge.EVENT_BUS.post(new CardUsePostListener(player, stack, null));
            if (bl) player.displayClientMessage(Component.translatable("dabaosword.bagua"),true);
        } else stack.shrink(1);
    }

    private static int getShanSlot(Player player) {
        for (int i = 0; i < 18; ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || stack.getItem() != ModItems.SHAN.get()) continue;
            return i;
        }
        return -1;
    }

    private static ItemStack shanStack(LivingEntity entity) {
        if (entity instanceof Player player) return player.getInventory().getItem(getShanSlot(player));
        return entity.getOffhandItem();
    }
}
