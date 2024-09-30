package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.equipment.Equipment;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.util.ModifyDamage;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EntityHurtHandler {

    public static void save(Player player, float amount) {
        if (hasItemInTag(Tags.RECOVER, player)) {
            //濒死自动使用酒、桃结算：首先计算需要回复的体力为(受到的伤害amount - 玩家当前生命值）
            float recover = amount - player.getHealth();
            int need = (int) (recover / 5) + 1;
            int tao = count(player, Tags.RECOVER);//数玩家背包中回血卡牌的数量（只包含酒、桃）
            if (tao >= need) {//如果剩余回血牌大于需要的桃的数量，则进行下一步，否则直接趋势
                for (int i = 0; i < need; i++) {//循环移除背包中的酒、桃
                    if (player.invulnerableTime > 9) {
                        ItemStack stack = stackInTag(Tags.RECOVER, player);
                        if (stack.getItem() == ModItems.PEACH) voice(player, Sounds.RECOVER);
                        if (stack.getItem() == ModItems.JIU) voice(player, Sounds.JIU);
                        cardUsePost(player, stack, player);
                    }
                }
                //最后将玩家的体力设置为 受伤前生命值 - 伤害值 + 回复量
                player.setHealth(player.getHealth() - amount + 5 * need);
            }
        }
    }

    @SubscribeEvent
    public static void EntityHurt(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity(); DamageSource source = event.getSource();
        float amount = event.getNewDamage();

        if (entity.level() instanceof ServerLevel world) {

            for (var stack : allTrinkets(entity)) { //受伤害后触发，优先级高
                if (stack.getItem() instanceof SkillItem skill && canTrigger(skill, entity)) skill.onHurt(stack, entity, source, amount);
                if (stack.getItem() instanceof Equipment skill) skill.onHurt(stack, entity, source, amount);
            }

            if (entity instanceof Player player && player.isDeadOrDying()) save(player, amount);

            if (source.getEntity() instanceof LivingEntity living) {
                if (living.getTags().contains("px")) entity.invulnerableTime = 0;
            }

            //监听事件：若玩家杀死敌对生物，有概率摸牌，若杀死玩家，摸两张牌
            if (source.getEntity() instanceof Player player && entity.getHealth() <= 0) {
                if (entity instanceof Monster) {
                    if (new Random().nextFloat() < 0.1) {
                        draw(player);
                        player.displayClientMessage(Component.translatable("dabaosword.draw.monster"),true);
                    }
                }
                if (entity instanceof Player) {
                    draw(player, 2);
                    player.displayClientMessage(Component.translatable("dabaosword.draw.player"),true);
                }
            }

            if (source.getDirectEntity() instanceof Player player) {
                //杀的相关结算
                if (shouldSha(player)) {
                    ItemStack stack = player.getMainHandItem().is(Tags.SHA) ? player.getMainHandItem() : shaStack(player);
                    player.addTag("sha");
                    if (stack.getItem() == ModItems.SHA) {
                        voice(player, Sounds.SHA);
                        if (!hasTrinket(ModItems.RATTAN_ARMOR, entity)) {
                            entity.invulnerableTime = 0; entity.hurt(source, 5);
                        } else voice(entity, Sounds.TENGJIA1);
                    }
                    if (stack.getItem() == ModItems.FIRE_SHA) {
                        voice(player, Sounds.SHA_FIRE);
                        entity.invulnerableTime = 0; entity.setRemainingFireTicks(100);
                    }
                    if (stack.getItem() == ModItems.THUNDER_SHA) {
                        voice(player, Sounds.SHA_THUNDER);
                        entity.invulnerableTime = 0; entity.hurt(player.damageSources().magic(),5);
                        LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                        if (lightningEntity != null) {
                            lightningEntity.moveTo(entity.getX(), entity.getY(), entity.getZ());
                            lightningEntity.setVisualOnly(true);
                            world.addFreshEntity(lightningEntity);
                        }
                    }
                    if (entity.isCurrentlyGlowing()) { //处理铁索连环的效果 铁索传导过去的伤害会触发2次加伤，这符合三国杀的逻辑，所以不改了
                        if (stack.getItem() != ModItems.SHA) entity.removeEffect(MobEffects.GLOWING);
                        AABB box = new AABB(player.getOnPos()).inflate(20); // 检测范围，根据需要修改
                        for (LivingEntity nearbyEntity : world.getEntitiesOfClass(LivingEntity.class, box, entities -> entities.isCurrentlyGlowing() && entities != entity)) {
                            if (stack.getItem() == ModItems.FIRE_SHA) {
                                nearbyEntity.removeEffect(MobEffects.GLOWING); nearbyEntity.hurt(source, amount);
                                nearbyEntity.invulnerableTime = 0; nearbyEntity.setRemainingFireTicks(100);
                            }
                            if (stack.getItem() == ModItems.THUNDER_SHA) {
                                nearbyEntity.removeEffect(MobEffects.GLOWING); nearbyEntity.hurt(source, amount);
                                nearbyEntity.invulnerableTime = 0; nearbyEntity.hurt(player.damageSources().magic(), 5);
                                LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                                if (lightningEntity != null) {
                                    lightningEntity.moveTo(nearbyEntity.getX(), nearbyEntity.getY(), nearbyEntity.getZ());
                                    lightningEntity.setVisualOnly(true);
                                    world.addFreshEntity(lightningEntity);
                                }
                            }
                        }
                    }
                    cardUsePost(player, stack, entity);
                }
            }

            if (source.getDirectEntity() instanceof LivingEntity living) { //在近战攻击造成伤害后触发
                for (var stack : allTrinkets(living)) {
                    if (stack.getItem() instanceof SkillItem skill && canTrigger(skill, living)) skill.postAttack(stack, entity, living, amount);
                    if (stack.getItem() instanceof Equipment skill) skill.postAttack(stack, entity, living, amount);
                }
            }

            if (source.getEntity() instanceof LivingEntity living) { //只要攻击造成伤害即可触发，包括远程
                for (var stack : allTrinkets(living)) {
                    if (stack.getItem() instanceof SkillItem skill && canTrigger(skill, living)) skill.postDamage(stack, entity, living, amount);
                    if (stack.getItem() instanceof Equipment skill) skill.postDamage(stack, entity, living, amount);
                }
            }

        }
    }

    static boolean shouldSha(Player player) {
        return getShaSlot(player) != -1 && !player.getTags().contains("sha") && !player.getTags().contains("juedou") && !player.getTags().contains("wanjian");
    }

    @SubscribeEvent
    public static void cancel(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getAmount();
        if (ModifyDamage.shouldCancel(entity, source, amount)) event.setCanceled(true);
    }
}
