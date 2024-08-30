package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeEvent {

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
                    //烈弓：命中后加伤害，至少为5，给目标一个短暂的冷却效果，防止其自动触发闪
                    target.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 2, 0, false, false, false));
                    float f = Math.max(13 - player.distanceTo(target), 5);
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, (int) (40 * f), 0, false, false, true));
                    target.hurt(player.damageSources().playerAttack(player), f); target.invulnerableTime = 0;
                    if (new Random().nextFloat() < 0.5) {voice(player, Sounds.LIEGONG1.get());} else {voice(player, Sounds.LIEGONG2.get());}
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

    @SubscribeEvent
    public static void EntityDie(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (entity.level() instanceof ServerLevel world) {
            //监听事件：若玩家杀死敌对生物，有概率摸牌，若杀死玩家，摸两张牌
            if (source.getEntity() instanceof Player player && !player.getTags().contains("kill_entity")) {
                player.addTag("kill_entity");
                if (entity instanceof Monster) {
                    if (new Random().nextFloat() < 0.1) {
                        player.addItem(new ItemStack(ModItems.GAIN_CARD));
                        player.displayClientMessage(Component.translatable("dabaosword.draw.monster"),true);
                    }
                    //功獒技能触发
                    if (hasTrinket(SkillCards.GONGAO.get(), player)) {
                        ItemStack stack = trinketItem(SkillCards.GONGAO.get(), player);
                        int extraHP = getTag(stack);
                        stack.set(ModItems.TAGS, extraHP + 1);
                        player.heal(1);
                        if (new Random().nextFloat() < 0.5) {voice(player, Sounds.GONGAO1.get());} else {voice(player, Sounds.GONGAO2.get());}
                    }
                }
                if (entity instanceof Player) {
                    player.addItem(new ItemStack(ModItems.GAIN_CARD, 2));
                    player.displayClientMessage(Component.translatable("dabaosword.draw.player"),true);
                    //功獒技能触发
                    if (hasTrinket(SkillCards.GONGAO.get(), player)) {
                        ItemStack stack = trinketItem(SkillCards.GONGAO.get(), player);
                        int extraHP = getTag(stack);
                        stack.set(ModItems.TAGS, extraHP + 5);
                        player.heal(5);
                        if (new Random().nextFloat() < 0.5) {voice(player, Sounds.GONGAO1.get());} else {voice(player, Sounds.GONGAO2.get());}
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void BeforeDamage(LivingDamageEvent.Pre event){
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        float origin = event.getOriginalDamage();
        float after = event.getNewDamage();
        if (entity.level() instanceof ServerLevel world) {

            if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && source.getEntity() instanceof LivingEntity && hasTrinket(ModItems.BAIYIN.get(), entity)) event.setNewDamage(0.4f * origin);

            if (entity instanceof Player player) {
                float amount = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ? after : hasTrinket(ModItems.BAIYIN.get(), player) ? 0.4f * origin : after;
                if (player.getHealth() <= amount) {
                    if (hasTrinket(SkillCards.BUQU.get(), player)) {
                        ItemStack stack = trinketItem(SkillCards.BUQU.get(), player);
                        int c = getTag(stack);
                        if (new Random().nextFloat() < 0.5) {voice(player, Sounds.BUQU1.get());} else {voice(player, Sounds.BUQU2.get());}
                        if (new Random().nextFloat() >= (float) c /13) {
                            player.displayClientMessage(Component.translatable("buqu.tip1").withStyle(ChatFormatting.GREEN).append(String.valueOf(c + 1)), false);
                            setTag(stack, c + 1);
                            event.setNewDamage(0);
                            player.setHealth(5);
                        } else {
                            player.displayClientMessage(Component.translatable("buqu.tip2").withStyle(ChatFormatting.RED), false);
                            save(player, amount, event);
                        }
                    } else save(player, amount, event);
                }
            }

        }
    }

    public static void save(Player player, float amount, LivingDamageEvent.Pre event) {
        if (hasItemInTag(Tags.RECOVER, player)) {
            //濒死自动使用酒、桃结算：首先计算需要回复的体力为(受到的伤害amount - 玩家当前生命值）
            float recover = amount - player.getHealth();
            int need = (int) (recover / 5) + 1;
            int tao = count(player, Tags.RECOVER);//数玩家背包中回血卡牌的数量（只包含酒、桃）
            if (tao >= need) {//如果剩余回血牌大于需要的桃的数量，则进行下一步，否则直接趋势
                for (int i = 0; i < need; i++) {//循环移除背包中的酒、桃
                    ItemStack stack = stackInTag(Tags.RECOVER, player);
                    if (stack.getItem() == ModItems.PEACH.get()) voice(player, Sounds.RECOVER.get());
                    if (stack.getItem() == ModItems.JIU.get()) voice(player, Sounds.JIU.get());
                    stack.shrink(1);
                } event.setNewDamage(0);
                //最后将玩家的体力设置为 受伤前生命值 - 伤害值 + 回复量
                player.setHealth(player.getHealth() - amount + 5 * need);
            }
        }
    }
}
