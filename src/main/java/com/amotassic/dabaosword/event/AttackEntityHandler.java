package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class AttackEntityHandler {

    @SubscribeEvent
    public static void AttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if (player.level() instanceof ServerLevel && entity instanceof LivingEntity target) {
            if (!(player.getMainHandItem().getItem() == ModItems.JUEDOU.get() || player.getMainHandItem().getItem() == ModItems.DISCARD.get())) {

                //破军：攻击命中盔甲槽有物品的生物后，会让其所有盔甲掉落，配合古锭刀特效使用，pvp神器
                if (hasTrinket(SkillCards.POJUN.get(), player) && !player.hasEffect(ModItems.COOLDOWN)) {
                    ItemStack head = target.getItemBySlot(EquipmentSlot.HEAD);
                    ItemStack chest = target.getItemBySlot(EquipmentSlot.CHEST);
                    ItemStack legs = target.getItemBySlot(EquipmentSlot.LEGS);
                    ItemStack feet = target.getItemBySlot(EquipmentSlot.FEET);
                    if (target instanceof Player player1) {
                        if (!head.isEmpty()) {player1.addItem(head.copy());head.setCount(0);}
                        if (!chest.isEmpty()) {player1.addItem(chest.copy());chest.setCount(0);}
                        if (!legs.isEmpty()) {player1.addItem(legs.copy());legs.setCount(0);}
                        if (!feet.isEmpty()) {player1.addItem(feet.copy());feet.setCount(0);}
                    } else {
                        if (!head.isEmpty()) {target.spawnAtLocation(head.copy());head.setCount(0);}
                        if (!chest.isEmpty()) {target.spawnAtLocation(chest.copy());chest.setCount(0);}
                        if (!legs.isEmpty()) {target.spawnAtLocation(legs.copy());legs.setCount(0);}
                        if (!feet.isEmpty()) {target.spawnAtLocation(feet.copy());feet.setCount(0);}
                    }
                    if (new Random().nextFloat() < 0.5) {voice(player, Sounds.POJUN1.get());} else {voice(player, Sounds.POJUN2.get());}
                    int i = target instanceof Player ? 200 : 40;
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, i, 0, false, false, true));
                }

                if (hasTrinket(ModItems.QINGGANG.get(), player)) {//青釭剑额外伤害
                    float extraDamage = Math.min(20, 0.2f * target.getMaxHealth());
                    target.hurt(player.damageSources().genericKill(), extraDamage); target.invulnerableTime = 0;
                }

                if (hasTrinket(ModItems.QINGLONG.get(), player) && player.getAttackStrengthScale(0) >= 0.9) {
                    player.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 10, 0, false, false, false));
                    player.teleportTo(target.getX(), target.getY(), target.getZ());
                    Vec3 momentum = player.getForward().scale(2);
                    target.hurtMarked = true;
                    target.setDeltaMovement(momentum.x(), 0, momentum.z());
                }


                if (hasTrinket(ModItems.FANGTIAN.get(), player)) {
                    //方天画戟：打中生物后触发特效，给予CD和持续时间
                    ItemStack stack = trinketItem(ModItems.FANGTIAN.get(), player);
                    int cd = getCD(stack);
                    if (cd == 0) {
                        setCD(stack, 20);
                        player.displayClientMessage(Component.translatable("dabaosword.fangtian").withStyle(ChatFormatting.RED), true);
                    }
                }

                if (hasTrinket(SkillCards.LIEGONG.get(), player) && !player.hasEffect(ModItems.COOLDOWN)) {
                    //烈弓：命中给目标一个短暂的冷却效果，防止其自动触发闪
                    target.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 2, 0, false, false, false));
                }

                if (hasTrinket(SkillCards.TIEJI.get(), player) && getShaSlot(player) != -1) {
                    if (new Random().nextFloat() < 0.5) {voice(player, Sounds.TIEJI1.get());} else {voice(player, Sounds.TIEJI2.get());}
                    target.addEffect(new MobEffectInstance(ModItems.TIEJI, 200, 0, false, true, true));
                    if (new Random().nextFloat() < 0.75)
                        target.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 2, 0, false, false, false));
                }
            }
        }
    }
}
