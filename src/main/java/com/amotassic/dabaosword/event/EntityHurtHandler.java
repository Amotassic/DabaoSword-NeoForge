package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.event.listener.CardDiscardListener;
import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.ModifyDamage;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

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
                        if (stack.getItem() == ModItems.PEACH.get()) voice(player, Sounds.RECOVER.get());
                        if (stack.getItem() == ModItems.JIU.get()) voice(player, Sounds.JIU.get());
                        NeoForge.EVENT_BUS.post(new CardUsePostListener(player, stack, player));
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
            if (entity instanceof Player player) {

                if (player.getHealth() <= amount) {
                    if (hasTrinket(SkillCards.BUQU.get(), player)) {
                        ItemStack stack = trinketItem(SkillCards.BUQU.get(), player);
                        int c = getTag(stack);
                        voice(player, Sounds.BUQU.get());
                        if (new Random().nextFloat() >= (float) c /13) {
                            player.displayClientMessage(Component.translatable("buqu.tip1").withStyle(ChatFormatting.GREEN).append(String.valueOf(c + 1)), false);
                            setTag(stack, c + 1);
                            player.setHealth(5);
                        } else {
                            player.displayClientMessage(Component.translatable("buqu.tip2").withStyle(ChatFormatting.RED), false);
                            save(player, amount);
                        }
                    } else save(player, amount);
                }

                //权计技能：受到生物伤害获得权
                if (hasTrinket(SkillCards.QUANJI.get(), player) && source.getEntity() instanceof LivingEntity) {
                    ItemStack stack = trinketItem(SkillCards.QUANJI.get(), player);
                    int quan = getTag(stack);
                    setTag(stack, quan + 1);
                    voice(player, Sounds.QUANJI.get());
                }

                //遗计
                if (hasTrinket(SkillCards.YIJI.get(), player) && !player.hasEffect(ModItems.COOLDOWN) && player.getHealth() <= 12) {
                    player.addItem(new ItemStack(ModItems.GAIN_CARD, 2));
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 20, 0, false, false, true));
                    trinketItem(SkillCards.YIJI.get(), player).set(ModItems.TAGS, 2);
                    voice(player, Sounds.YIJI.get());
                }

                //放逐
                if (hasTrinket(SkillCards.FANGZHU.get(), player) && source.getEntity() instanceof LivingEntity attacker && player != attacker) {
                    int i = attacker instanceof Player ? (int) (20 * amount + 60) : 300;
                    attacker.addEffect(new MobEffectInstance(ModItems.TURNOVER, i));
                    voice(player, Sounds.FANGZHU.get());
                }

                //刚烈
                if (hasTrinket(SkillCards.GANGLIE.get(), player) && source.getEntity() instanceof LivingEntity attacker && player != attacker) {
                    voice(player, Sounds.GANGLIE.get());
                    for (int i = 0; i < amount; i += 5) {//造成伤害
                        if (new Random().nextFloat() < 0.5) {
                            player.addTag("sha");//以此造成伤害不自动触发杀
                            float f = i + 5 < amount ? 5 : amount - i;
                            attacker.invulnerableTime = 0; attacker.hurt(player.damageSources().playerAttack(player), f);
                        } else {//弃牌
                            if (attacker instanceof Player target) {//如果来源是玩家则弃牌
                                List<ItemStack> candidate = new ArrayList<>();
                                //把背包中的卡牌添加到待选物品中
                                NonNullList<ItemStack> inventory = target.getInventory().items;
                                List<Integer> cardSlots = IntStream.range(0, inventory.size()).filter(j -> isCard(inventory.get(j))).boxed().toList();
                                for (Integer slot : cardSlots) {candidate.add(inventory.get(slot));}
                                //把饰品栏的卡牌添加到待选物品中
                                int equip = 0; //用于标记装备区牌的数量
                                var component = CuriosApi.getCuriosInventory(player);
                                if(component.isPresent()) {
                                    var allEquipped = component.get().getEquippedCurios();
                                    for(int j = 0; j < allEquipped.getSlots(); j++) {
                                        ItemStack stack1 = allEquipped.getStackInSlot(j);
                                        if (stack1.is(Tags.CARD)) candidate.add(stack1); equip++;
                                    }
                                }
                                if(!candidate.isEmpty()) {
                                    int index = new Random().nextInt(candidate.size()); ItemStack chosen = candidate.get(index);
                                    target.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("dabaosword.discard")).append(chosen.getDisplayName()),false);
                                    NeoForge.EVENT_BUS.post(new CardDiscardListener(player, chosen, 1, index > candidate.size() - equip));
                                }
                            } else {//如果来源不是玩家则随机弃置它的主副手物品和装备
                                List<ItemStack> candidate = new ArrayList<>();
                                if (!attacker.getMainHandItem().isEmpty()) candidate.add(attacker.getMainHandItem());
                                if (!attacker.getOffhandItem().isEmpty()) candidate.add(attacker.getOffhandItem());
                                for (ItemStack armor : attacker.getArmorSlots()) {if (!armor.isEmpty()) candidate.add(armor);}
                                if(!candidate.isEmpty()) {
                                    int index = new Random().nextInt(candidate.size()); ItemStack chosen = candidate.get(index);
                                    chosen.shrink(1);
                                }
                            }
                        }
                    }
                }

            }

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
                    //功獒技能触发
                    if (hasTrinket(SkillCards.GONGAO.get(), player)) {
                        ItemStack stack = trinketItem(SkillCards.GONGAO.get(), player);
                        int extraHP = getTag(stack);
                        stack.set(ModItems.TAGS, extraHP + 1);
                        player.heal(1);
                        voice(player, Sounds.GONGAO.get());
                    }
                }
                if (entity instanceof Player) {
                    draw(player, 2);
                    player.displayClientMessage(Component.translatable("dabaosword.draw.player"),true);
                    //功獒技能触发
                    if (hasTrinket(SkillCards.GONGAO.get(), player)) {
                        ItemStack stack = trinketItem(SkillCards.GONGAO.get(), player);
                        int extraHP = getTag(stack);
                        stack.set(ModItems.TAGS, extraHP + 5);
                        player.heal(5);
                        voice(player, Sounds.GONGAO.get());
                    }
                }
            }

            if (source.getEntity() instanceof Player player) {
                //狂骨：攻击命中敌人时，如果受伤超过5则回血，否则摸一张牌
                if (hasTrinket(SkillCards.KUANGGU.get(), player) && !player.hasEffect(ModItems.COOLDOWN)) {
                    if (player.getMaxHealth()-player.getHealth()>=5) {player.heal(5);}
                    else draw(player);
                    voice(player, Sounds.KUANGGU.get());
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 8,0,false,false,true));
                }

                //擅专：我言既出，谁敢不从！
                if (hasTrinket(SkillCards.SHANZHUAN.get(), player) && !player.hasEffect(ModItems.COOLDOWN)) {
                    var stack = trinketItem(SkillCards.SHANZHUAN.get(), player);
                    if (entity instanceof Player target) ActiveSkillHandler.openInv(player, target, Component.translatable("dabaosword.discard.title", stack.getDisplayName()), ActiveSkillHandler.targetInv(target, true, false, 1, stack));
                    else {
                        voice(player, Sounds.SHANZHUAN.get());
                        if (new Random().nextFloat() < 0.5) {
                            entity.addEffect(new MobEffectInstance(ModItems.BINGLIANG, MobEffectInstance.INFINITE_DURATION,1));
                        } else {
                            entity.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
                        }
                        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 5,0,false,false,true));
                    }
                }
            }

            if (source.getDirectEntity() instanceof Player player) {
                //寒冰剑冻伤
                if (hasTrinket(ModItems.HANBING.get(), player)) {
                    voice(player, Sounds.HANBING.get());
                    entity.invulnerableTime = 0;
                    entity.setTicksFrozen(500);
                }

                //杀的相关结算
                if (shouldSha(player)) {
                    ItemStack stack = player.getMainHandItem().is(Tags.SHA) ? player.getMainHandItem() : shaStack(player);
                    player.addTag("sha");
                    if (stack.getItem() == ModItems.SHA.get()) {
                        voice(player, Sounds.SHA.get());
                        if (!hasTrinket(ModItems.RATTAN_ARMOR.get(), entity)) {
                            entity.invulnerableTime = 0; entity.hurt(source, 5);
                        } else voice(entity, Sounds.TENGJIA1.get());
                    }
                    if (stack.getItem() == ModItems.FIRE_SHA.get()) {
                        voice(player, Sounds.SHA_FIRE.get());
                        entity.invulnerableTime = 0; entity.setRemainingFireTicks(100);
                    }
                    if (stack.getItem() == ModItems.THUNDER_SHA.get()) {
                        voice(player, Sounds.SHA_THUNDER.get());
                        entity.invulnerableTime = 0; entity.hurt(player.damageSources().magic(),5);
                        LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                        if (lightningEntity != null) {
                            lightningEntity.moveTo(entity.getX(), entity.getY(), entity.getZ());
                            lightningEntity.setVisualOnly(true);
                            world.addFreshEntity(lightningEntity);
                        }
                    }
                    if (entity.isCurrentlyGlowing()) { //处理铁索连环的效果 铁索传导过去的伤害会触发2次加伤，这符合三国杀的逻辑，所以不改了
                        if (stack.getItem() != ModItems.SHA.get()) entity.removeEffect(MobEffects.GLOWING);
                        AABB box = new AABB(player.getOnPos()).inflate(20); // 检测范围，根据需要修改
                        for (LivingEntity nearbyEntity : world.getEntitiesOfClass(LivingEntity.class, box, entities -> entities.isCurrentlyGlowing() && entities != entity)) {
                            if (stack.getItem() == ModItems.FIRE_SHA.get()) {
                                nearbyEntity.removeEffect(MobEffects.GLOWING); nearbyEntity.hurt(source, amount);
                                nearbyEntity.invulnerableTime = 0; nearbyEntity.setRemainingFireTicks(100);
                            }
                            if (stack.getItem() == ModItems.THUNDER_SHA.get()) {
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
                    NeoForge.EVENT_BUS.post(new CardUsePostListener(player, stack, entity));
                }

                //奔袭：命中后减少2手长，摸一张牌
                if (hasTrinket(SkillCards.BENXI.get(), player) && !player.getTags().contains("benxi")) {
                    ItemStack stack = trinketItem(SkillCards.BENXI.get(), player);
                    int ben = getTag(stack);
                    if (ben > 1) {
                        player.addTag("benxi"); stack.set(ModItems.TAGS, ben - 2);
                        draw(player);
                        voice(player, Sounds.BENXI.get());
                    }
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
