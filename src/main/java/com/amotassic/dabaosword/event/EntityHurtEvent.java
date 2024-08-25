package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.amotassic.dabaosword.network.ServerNetworking.openInv;
import static com.amotassic.dabaosword.network.ServerNetworking.targetInv;
import static com.amotassic.dabaosword.util.ModTools.*;
import static com.amotassic.dabaosword.util.ModTools.voice;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EntityHurtEvent {

    @SubscribeEvent
    public static void EntityHurt(LivingDamageEvent.Post event){
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getNewDamage();
        ItemStack head = entity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = entity.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = entity.getItemBySlot(EquipmentSlot.FEET);
        boolean noArmor = head.isEmpty() && chest.isEmpty() && legs.isEmpty() && feet.isEmpty();

        if (entity.level() instanceof ServerLevel world) {

            if (entity instanceof Player player) {

                //穿藤甲时，若承受火焰伤害，则 战火燃尽，嘤熊胆！
                if (source.is(DamageTypeTags.IS_FIRE) && hasTrinket(ModItems.RATTAN_ARMOR.get(), player) && !player.getTags().contains("rattan")) {
                    player.addTag("rattan");
                    player.invulnerableTime = 0; player.hurt(source, amount > 5 ? 5 : amount);
                    player.getTags().remove("rattan");
                }

                //权计技能：受到生物伤害获得权
                if (hasTrinket(SkillCards.QUANJI.get(), player) && source.getEntity() instanceof LivingEntity) {
                    ItemStack stack = trinketItem(SkillCards.QUANJI.get(), player);
                    int quan = getTag(stack);
                    quan++; setTag(stack, quan);
                    if (new Random().nextFloat() < 0.5) {voice(player, Sounds.QUANJI1.get());} else {voice(player, Sounds.QUANJI2.get());}
                }

                //遗计
                if (hasTrinket(SkillCards.YIJI.get(), player) && !player.hasEffect(ModItems.COOLDOWN) && player.getHealth() <= 12) {
                    player.addItem(new ItemStack(ModItems.GAIN_CARD, 2));
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 20, 0, false, false, true));
                    if (new Random().nextFloat() < 0.5) {voice(player, Sounds.YIJI1.get());} else {voice(player, Sounds.YIJI2.get());}
                }

                //放逐
                if (hasTrinket(SkillCards.FANGZHU.get(), player) && source.getEntity() instanceof LivingEntity attacker && player != attacker) {
                    int i = attacker instanceof Player ? (int) (20 * amount + 60) : 300;
                    attacker.addEffect(new MobEffectInstance(ModItems.TURNOVER, i));
                    if (new Random().nextFloat() < 0.5) {voice(player, Sounds.FANGZHU1.get());} else {voice(player, Sounds.FANGZHU2.get());}
                }

                //刚烈
                if (hasTrinket(SkillCards.GANGLIE.get(), player) && source.getEntity() instanceof LivingEntity attacker && player != attacker) {
                    if (new Random().nextFloat() < 0.5) {voice(player, Sounds.GANGLIE1.get());} else {voice(player, Sounds.GANGLIE2.get());}
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
                                List<Integer> cardSlots = IntStream.range(0, inventory.size()).filter(j -> inventory.get(j).is(Tags.CARD) || inventory.get(j).getItem() == ModItems.GAIN_CARD.get()).boxed().toList();
                                for (Integer slot : cardSlots) {candidate.add(inventory.get(slot));}
                                //把饰品栏的卡牌添加到待选物品中
                                var component = CuriosApi.getCuriosInventory(player);
                                if(component.isPresent()) {
                                    var allEquipped = component.get().getEquippedCurios();
                                    for(int j = 0; j < allEquipped.getSlots(); j++) {
                                        candidate.add(allEquipped.getStackInSlot(j));
                                    }
                                }
                                if(!candidate.isEmpty()) {
                                    Random r = new Random(); int index = r.nextInt(candidate.size()); ItemStack chosen = candidate.get(index);
                                    target.displayClientMessage(Component.literal(player.getScoreboardName()).append(Component.translatable("dabaosword.discard")).append(chosen.getDisplayName()),false);
                                    chosen.shrink(1);
                                }
                            } else {//如果来源不是玩家则随机弃置它的主副手物品和装备
                                List<ItemStack> candidate = new ArrayList<>();
                                if (!attacker.getMainHandItem().isEmpty()) candidate.add(attacker.getMainHandItem());
                                if (!attacker.getOffhandItem().isEmpty()) candidate.add(attacker.getOffhandItem());
                                for (ItemStack armor : attacker.getArmorSlots()) {if (!armor.isEmpty()) candidate.add(armor);}
                                if(!candidate.isEmpty()) {
                                    Random r = new Random(); int index = r.nextInt(candidate.size()); ItemStack chosen = candidate.get(index);
                                    chosen.shrink(1);
                                }
                            }
                        }
                    }
                }

            }

            if (source.getDirectEntity() instanceof LivingEntity attacker) {
                //古锭刀对没有装备的生物伤害增加 限定版翻倍
                if (attacker.getMainHandItem().getItem() == ModItems.GUDINGDAO.get() && !attacker.getTags().contains("guding")) {
                    if (noArmor || hasTrinket(SkillCards.POJUN.get(), (Player) attacker)) {
                        attacker.addTag("guding");
                        entity.invulnerableTime = 0;
                        entity.hurt(source, amount);
                        attacker.getTags().remove("guding");
                    }
                }
            }

            if (source.getEntity() instanceof Player player) {
                //狂骨：攻击命中敌人时，如果受伤超过5则回血，否则摸一张牌
                if (hasTrinket(SkillCards.KUANGGU.get(), player) && !player.hasEffect(ModItems.COOLDOWN)) {
                    if (player.getMaxHealth()-player.getHealth()>=5) {player.heal(5);}
                    else {player.addItem(new ItemStack(ModItems.GAIN_CARD));}
                    if (new Random().nextFloat() < 0.5) {voice(player, Sounds.KUANGGU1.get());} else {voice(player, Sounds.KUANGGU2.get());}
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 8,0,false,false,true));
                }

                //擅专：我言既出，谁敢不从！
                if (hasTrinket(SkillCards.SHANZHUAN.get(), player) && !player.hasEffect(ModItems.COOLDOWN)) {
                    var stack = trinketItem(SkillCards.SHANZHUAN.get(), player);
                    if (entity instanceof Player target) openInv(player, target, Component.translatable("dabaosword.discard.title", stack.getDisplayName()), targetInv(target, true, false, 1, stack));
                    else {
                        if (new Random().nextFloat() < 0.5) {
                            voice(player, Sounds.SHANZHUAN1.get());
                            entity.addEffect(new MobEffectInstance(ModItems.BINGLIANG, MobEffectInstance.INFINITE_DURATION,1));
                        } else {
                            voice(player, Sounds.SHANZHUAN2.get());
                            entity.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
                        }
                        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 5,0,false,false,true));
                    }
                }

                if (player.getTags().contains("px")) {
                    entity.invulnerableTime = 0;
                }
            }

            if (source.getDirectEntity() instanceof Player player) {
                //古锭刀对没有装备的生物伤害增加 卡牌版加5
                if (hasTrinket(ModItems.GUDING_WEAPON.get(), player) && !player.getTags().contains("guding")) {
                    if (noArmor || hasTrinket(SkillCards.POJUN.get(), player)) {
                        player.addTag("guding");
                        entity.invulnerableTime = 0; entity.hurt(source,5);
                        player.getTags().remove("guding");
                    }
                }

                //寒冰剑冻伤
                if (hasTrinket(ModItems.HANBING.get(), player)) {entity.invulnerableTime = 0; entity.setTicksFrozen(500);}

                //杀的相关结算
                if (shouldSha(player) && !entity.isCurrentlyGlowing()) {
                    ItemStack stack = player.getMainHandItem().is(Tags.SHA) ? player.getMainHandItem() : shaStack(player);
                    player.addTag("sha");
                    if (stack.getItem() == ModItems.SHA.get()) {
                        voice(player, Sounds.SHA.get());
                        if (!(entity instanceof Player && hasTrinket(ModItems.RATTAN_ARMOR.get(), (Player) entity))) {
                            entity.invulnerableTime = 0; entity.hurt(source, 5);
                        }
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
                    } benxi(player);
                    if (!player.isCreative()) {stack.shrink(1);}
                }

                //排异技能：攻击伤害增加
                if (hasTrinket(SkillCards.QUANJI.get(), player) && !player.getTags().contains("quanji")) {
                    ItemStack stack = trinketItem(SkillCards.QUANJI.get(), player);
                    int quan = getTag(stack);
                    if (quan > 0) {
                        player.addTag("quanji");
                        entity.invulnerableTime = 0; entity.hurt(source, quan);
                        if (quan > 4 && entity instanceof Player) {
                            give((Player) entity, new ItemStack(ModItems.GAIN_CARD.get(), 2));
                        }
                        int quan1 = quan/2; stack.set(ModItems.TAGS, quan1);
                        float j = new Random().nextFloat();
                        if (j < 0.25) {voice(player, Sounds.PAIYI1.get());
                        } else if (0.25 <= j && j < 0.5) {voice(player, Sounds.PAIYI2.get(),3);
                        } else if (0.5 <= j && j < 0.75) {voice(player, Sounds.PAIYI3.get());
                        } else {voice(player, Sounds.PAIYI4.get(),3);}
                    }
                }

                //奔袭：命中后减少2手长，摸一张牌
                if (hasTrinket(SkillCards.BENXI.get(), player) && !player.getTags().contains("benxi")) {
                    ItemStack stack = trinketItem(SkillCards.BENXI.get(), player);
                    int ben = getTag(stack);
                    if (ben > 1) {
                        player.addTag("benxi"); stack.set(ModItems.TAGS, ben - 2);
                        give(player, new ItemStack(ModItems.GAIN_CARD.get()));
                        if (new Random().nextFloat() < 0.5) {voice(player, Sounds.BENXI1.get());} else {voice(player, Sounds.BENXI2.get());}
                    }
                }

            }
        }
    }

    static boolean shouldSha(Player player) {
        return getShaSlot(player) != -1 && !player.getTags().contains("sha") && !player.getTags().contains("juedou") && !player.getTags().contains("wanjian");
    }
}
