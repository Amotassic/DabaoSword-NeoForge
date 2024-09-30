package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.event.listener.CardCBs;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.equipment.Equipment;
import com.amotassic.dabaosword.util.ModTools;
import com.amotassic.dabaosword.util.Skill;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;
import java.util.stream.IntStream;

import static com.amotassic.dabaosword.util.ModTools.*;

public class SkillItem extends Item implements ICurioItem, Skill {
    public SkillItem(Properties p_41383_) {super(p_41383_);}

    public static class Benxi extends SkillItem {
        public Benxi(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int benxi = getTag(stack);
            tooltip.add(Component.nullToEmpty("tags: " + benxi));
            tooltip.add(Component.translatable("item.dabaosword.benxi.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.benxi.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (!slotContext.entity().level().isClientSide && slotContext.entity() instanceof Player player && noLongHand(player) && noTieji(slotContext.entity())) {
                int benxi = getTag(stack);
                if (hasTrinket(ModItems.CHITU, player) && hasTrinket(SkillCards.MASHU, player)) {
                    player.addEffect(new MobEffectInstance(ModItems.REACH, 10,benxi + 2,false,false,true));
                } else if (hasTrinket(ModItems.CHITU, player) || hasTrinket(SkillCards.MASHU, player)) {
                    player.addEffect(new MobEffectInstance(ModItems.REACH, 10,benxi + 1,false,false,true));
                } else if (benxi != 0) {
                    player.addEffect(new MobEffectInstance(ModItems.REACH, 10,benxi - 1,false,false,true));
                }
            }
        }

        @Override
        public void postAttack(ItemStack stack, LivingEntity target, LivingEntity attacker, float amount) {
            if (attacker instanceof Player player && !player.getTags().contains("benxi")) {
                int ben = getTag(stack);
                if (ben > 1) {
                    player.addTag("benxi");
                    setTag(stack, ben - 2);
                    draw(player);
                    voice(player, Sounds.BENXI);
                }
            }
        }

        private boolean noLongHand(Player player) {
            return player.getMainHandItem().getItem() != ModItems.JUEDOU && player.getMainHandItem().getItem() != ModItems.DISCARD;
        }
    }

    public static class Buqu extends SkillItem {
        public Buqu(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int c = getTag(stack);
            if(Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("item.dabaosword.buqu.tooltip1").withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("item.dabaosword.buqu.tooltip2").withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("item.dabaosword.buqu.tooltip3").withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("item.dabaosword.buqu.tooltip4").withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("item.dabaosword.buqu.tooltip5").withStyle(ChatFormatting.GREEN));
            } else {
                tooltip.add(Component.literal("chuang: " + c));
                tooltip.add(Component.translatable("item.dabaosword.buqu.tooltip").withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("dabaosword.shifttooltip"));
            }
        }

        @Override
        public void onHurt(ItemStack stack, LivingEntity entity, DamageSource source, float amount) {
            if (entity instanceof Player player && player.isDeadOrDying()) {
                int c = getTag(stack);
                voice(player, Sounds.BUQU);
                if (new Random().nextFloat() >= (float) c /13) {
                    player.displayClientMessage(Component.translatable("buqu.tip1").withStyle(ChatFormatting.GREEN).append(String.valueOf(c + 1)), false);
                    setTag(stack, c + 1);
                    player.setHealth(1);
                } else player.displayClientMessage(Component.translatable("buqu.tip2").withStyle(ChatFormatting.RED), false);
            }
        }
    }

    public static class Duanliang extends SkillItem {
        public Duanliang(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.duanliang.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() instanceof Player player) viewAs(player, stack, 5, ModTools::nonBasic, new ItemStack(ModItems.BINGLIANG_ITEM), Sounds.DUANLIANG);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Fangzhu extends SkillItem {
        public Fangzhu(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.fangzhu.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void onHurt(ItemStack stack, LivingEntity entity, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity attacker && entity != attacker) {
                int i = attacker instanceof Player ? (int) (20 * amount + 60) : 300;
                attacker.addEffect(new MobEffectInstance(ModItems.TURNOVER, i));
                voice(entity, Sounds.FANGZHU);
            }
        }
    }

    public static class Ganglie extends SkillItem {
        public Ganglie(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.ganglie.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.ganglie.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void onHurt(ItemStack stack, LivingEntity entity, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity attacker && entity != attacker) {
                voice(entity, Sounds.GANGLIE);
                for (int i = 0; i < amount; i += 5) {//造成伤害
                    if (new Random().nextFloat() < 0.5) {
                        entity.addTag("sha");//以此造成伤害不自动触发杀
                        float f = i + 5 < amount ? 5 : amount - i;
                        attacker.invulnerableTime = 0; attacker.hurt(entity.damageSources().mobAttack(entity), f);
                    } else {//弃牌
                        if (attacker instanceof Player target) {//如果来源是玩家则弃牌
                            List<ItemStack> candidate = new ArrayList<>();
                            //把背包中的卡牌添加到待选物品中
                            NonNullList<ItemStack> inventory = target.getInventory().items;
                            List<Integer> cardSlots = IntStream.range(0, inventory.size()).filter(j -> isCard(inventory.get(j))).boxed().toList();
                            for (Integer slot : cardSlots) {candidate.add(inventory.get(slot));}
                            //把饰品栏的卡牌添加到待选物品中
                            int equip = 0; //用于标记装备区牌的数量
                            var component = CuriosApi.getCuriosInventory(target);
                            if(component.isPresent()) {
                                var allEquipped = component.get().getEquippedCurios();
                                for(int j = 0; j < allEquipped.getSlots(); j++) {
                                    ItemStack stack1 = allEquipped.getStackInSlot(j);
                                    if (stack1.is(Tags.CARD)) candidate.add(stack1); equip++;
                                }
                            }
                            if(!candidate.isEmpty()) {
                                int index = new Random().nextInt(candidate.size()); ItemStack chosen = candidate.get(index);
                                target.displayClientMessage(Component.literal(entity.getScoreboardName()).append(Component.translatable("dabaosword.discard")).append(chosen.getDisplayName()),false);
                                cardDiscard(target, chosen, 1, index > candidate.size() - equip);
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
    }

    public static class Gongao extends SkillItem {
        public Gongao(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.gongao.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.gongao.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (!entity.level().isClientSide) {
                int extraHP = getTag(stack);

                if (entity.level().getGameTime() % 600 == 0) { // 每30s触发扣体力上限
                    if (entity instanceof Player player) {
                        if (extraHP >= 5 && !player.isCreative() && !player.isSpectator()) {
                            draw(player, 2);
                            setTag(stack, extraHP - 5);
                            voice(player, Sounds.WEIZHONG);
                        }
                    }
                }
            }
        }

        @Override
        public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
            Multimap<Holder<Attribute>, AttributeModifier> multimap = LinkedHashMultimap.create();
            AttributeModifier Modifier = new AttributeModifier(id, getTag(stack), AttributeModifier.Operation.ADD_VALUE);
            multimap.put(Attributes.MAX_HEALTH, Modifier);
            return multimap;
        }

        @Override
        public void postDamage(ItemStack stack, LivingEntity target, LivingEntity player, float amount) {
            if (target.isDeadOrDying()) {
                if (target instanceof Monster) {
                    int extraHP = getTag(stack);
                    setTag(stack, extraHP +1);
                    player.heal(1);
                    voice(player, Sounds.GONGAO);
                }
                if (target instanceof Player) {
                    int extraHP = getTag(stack);
                    setTag(stack, extraHP + 5);
                    player.heal(5);
                    voice(player, Sounds.GONGAO);
                }
            }
        }
    }

    public static class Gongxin extends ActiveSkillWithTarget {
        public Gongxin(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 30s" : "CD: 30s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.gongxin.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            int cd = getCD(stack);
            if (cd > 0) user.displayClientMessage(Component.translatable("dabaosword.cooldown").withStyle(ChatFormatting.RED), true);
            else {
                voice(user, Sounds.GONGXIN);
                openInv(user, target, Component.translatable("gongxin.title"), targetInv(target, false, false, 2, stack));
                setCD(stack, 30);
            }
        }
    }

    public static class Guose extends SkillItem {
        public Guose(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.guose.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() instanceof Player player) viewAs(player, stack, 15, s -> s.is(ModItems.SHAN), new ItemStack(ModItems.TOO_HAPPY_ITEM), Sounds.GUOSE);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Huoji extends SkillItem {
        public Huoji(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.huoji.tooltip").withStyle(ChatFormatting.RED));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() instanceof Player player) viewAs(player, stack, 15, s -> s.is(Tags.BASIC_CARD), new ItemStack(ModItems.FIRE_ATTACK), Sounds.HUOJI);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Jueqing extends SkillItem {
        public Jueqing(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.jueqing.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.jueqing.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public Priority getPriority(LivingEntity target, DamageSource source, float amount) {return Priority.LOWEST;}

        @Override
        public boolean cancelDamage(LivingEntity target, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity attacker && hasTrinket(SkillCards.JUEQING, attacker)) {
                target.hurt(target.damageSources().genericKill(), Math.min(Math.max(7, target.getMaxHealth() / 3), amount));
                voice(attacker, Sounds.JUEQING, 1);
                return true;
            }
            return false;
        }
    }

    public static class Kanpo extends SkillItem {
        public Kanpo(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 10s" : "CD: 10s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.kanpo.tooltip").withStyle(ChatFormatting.RED));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() instanceof Player player) viewAs(player, stack, 10, s -> s.is(Tags.ARMOURY_CARD), new ItemStack(ModItems.WUXIE), Sounds.KANPO);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Kuanggu extends SkillItem {
        public Kuanggu(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 8s"));
            tooltip.add(Component.translatable("item.dabaosword.kuanggu.tooltip").withStyle(ChatFormatting.RED));
        }

        @Override
        public void postAttack(ItemStack stack, LivingEntity target, LivingEntity attacker, float amount) {
            if (attacker instanceof Player player && !player.hasEffect(ModItems.COOLDOWN)) {
                if (player.getMaxHealth() - player.getHealth()>=5) player.heal(5);
                else draw(player);
                voice(player, Sounds.KUANGGU);
                player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 8,0,false,false,true));
            }
        }
    }

    public static class Kurou extends ActiveSkill {
        public Kurou(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.kurou.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            if (user.getHealth() + 5 * count(user, Tags.RECOVER) > 4.99) {
                draw(user, 2);
                if (!user.isCreative()) {
                    user.invulnerableTime = 0;
                    user.hurt(user.damageSources().genericKill(), 4.99f);
                }
                voice(user, Sounds.KUROU);
            } else {user.displayClientMessage(Component.translatable("item.dabaosword.kurou.tip").withStyle(ChatFormatting.RED), true);}
        }
    }

    public static class Lianying extends SkillItem {
        public Lianying(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.lianying.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (entity.level() instanceof ServerLevel world && entity instanceof Player player) {
                int cd = getCD(stack);
                if (world.getGameTime() % 20 == 0 && cd == 1) { //确保一秒内只触发一次
                    draw(player, 1);
                    voice(player, Sounds.LIANYING);
                }
            }
            super.curioTick(slotContext, stack);
        }
    }

    public static class Liegong extends SkillItem {
        public Liegong(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.liegong.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.liegong.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player player) {
            if (!player.hasEffect(ModItems.COOLDOWN)) {
                //烈弓：命中后给目标一个短暂的冷却效果，防止其自动触发闪
                target.addEffect(new MobEffectInstance(ModItems.COOLDOWN2,2,0,false,false,false));
            }
        }

        @Override
        public Tuple<Float, Float> modifyDamage(LivingEntity target, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity attacker) { //命中后加伤害，至少为5
                if (hasTrinket(SkillCards.LIEGONG, attacker) && !attacker.hasEffect(ModItems.COOLDOWN)) {
                    float f = Math.max(13 - attacker.distanceTo(target), 5);
                    attacker.addEffect(new MobEffectInstance(ModItems.COOLDOWN, (int) (40 * f),0,false,false,true));
                    voice(attacker, Sounds.LIEGONG);
                    System.out.println(f);
                    return new Tuple<>(0f, f);
                }
            }
            return super.modifyDamage(target, source, amount);
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (!entity.level().isClientSide && noTieji(entity)) {
                if (!entity.hasEffect(ModItems.COOLDOWN)) gainReach(entity,13);
                else gainReach(entity,0);
            }
            super.curioTick(slotContext, stack);
        }

        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (!entity.level().isClientSide) gainReach(entity,0);
        }

        private void gainReach(LivingEntity entity, int value) {
            AttributeModifier Modifier = new AttributeModifier(ResourceLocation.withDefaultNamespace("range_13"), value, AttributeModifier.Operation.ADD_VALUE);
            Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.ENTITY_INTERACTION_RANGE)).addOrUpdateTransientModifier(Modifier);
            Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.BLOCK_INTERACTION_RANGE)).addOrUpdateTransientModifier(Modifier);
        }
    }

    public static class Liuli extends SkillItem {
        public Liuli(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.liuli.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public Priority getPriority(LivingEntity target, DamageSource source, float amount) {return Priority.NORMAL;}

        @Override
        public boolean cancelDamage(LivingEntity target, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity attacker && target instanceof Player player) {
                if (hasTrinket(SkillCards.LIULI, player) && hasItemInTag(Tags.CARD, player) && !player.hasEffect(ModItems.INVULNERABLE)) {
                    ItemStack stack = stackInTag(Tags.CARD, player);
                    LivingEntity nearEntity = getLiuliEntity(player, attacker);
                    if (nearEntity != null) {
                        player.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 15,0,false,false,false));
                        voice(player, Sounds.LIULI);
                        cardDiscard(player, stack, 1, false);
                        nearEntity.invulnerableTime = 0; nearEntity.hurt(source, amount);
                        return true;
                    }
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
    }

    public static class Longdan extends SkillItem {
        public Longdan(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.longdan.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.longdan.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().level() instanceof ServerLevel world && slotContext.entity() instanceof Player player && noTieji(slotContext.entity())) {
                ItemStack stack1 = player.getOffhandItem();
                if (world.getGameTime() % 20 == 0 && stack1.is(Tags.BASIC_CARD)) {
                    stack1.shrink(1);
                    if (stack1.is(Tags.SHA)) give(player, new ItemStack(ModItems.SHAN));
                    if (stack1.getItem() == ModItems.SHAN) give(player, new ItemStack(ModItems.SHA));
                    if (stack1.getItem() == ModItems.PEACH) give(player, new ItemStack(ModItems.JIU));
                    if (stack1.getItem() == ModItems.JIU) give(player, new ItemStack(ModItems.PEACH));
                    voice(player, Sounds.LONGDAN);
                }
            }
            super.curioTick(slotContext, stack);
        }
    }

    public static class Luanji extends SkillItem {
        public Luanji(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.luanji.tooltip"));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() instanceof Player player) viewAs(player, stack, 15, s -> s.is(Tags.CARD) && s.getCount() > 1, 2, new ItemStack(ModItems.WANJIAN), Sounds.LUANJI);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Luoshen extends ActiveSkill {
        public Luoshen(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 30s" : "CD: 30s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.luoshen.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            int cd = getCD(stack);
            if (cd > 0) user.displayClientMessage(Component.translatable("dabaosword.cooldown").withStyle(ChatFormatting.RED), true);
            else {
                voice(user, Sounds.LUOSHEN);
                if (new Random().nextFloat() < 0.5) {
                    draw(user);
                    user.displayClientMessage(Component.translatable("item.dabaosword.luoshen.win").withStyle(ChatFormatting.GREEN), true);
                } else {
                    setCD(stack, 30);
                    user.displayClientMessage(Component.translatable("item.dabaosword.luoshen.lose").withStyle(ChatFormatting.RED), true);
                }
            }
        }
    }

    public static class Luoyi extends SkillItem {
        public Luoyi(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.luoyi.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (!entity.level().isClientSide) gainStrength(entity, getEmptyArmorSlot(entity) + 1);
        }

        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
            if (!slotContext.entity().level().isClientSide) gainStrength(slotContext.entity(), 0);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
            if (!world.isClientSide && !user.isShiftKeyDown()) voice(user, Sounds.LUOYI);
            return super.use(world, user, hand);
        }

        private void gainStrength(LivingEntity entity, int value) {
            AttributeModifier Modifier = new AttributeModifier(ResourceLocation.parse("attack_damage"), value, AttributeModifier.Operation.ADD_VALUE);
            Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).addOrUpdateTransientModifier(Modifier);
        }

        private int getEmptyArmorSlot(LivingEntity entity) {
            int i = 0;
            for (var slot : entity.getArmorSlots()) {if (slot.isEmpty()) i++;}
            return i;
        }
    }

    public static class Pojun extends SkillItem {
        public Pojun(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 10s"));
            tooltip.add(Component.translatable("item.dabaosword.pojun.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player player) {
            //破军：攻击命中盔甲槽有物品的生物后，会让其所有盔甲掉落，配合古锭刀特效使用，pvp神器
            if (!player.hasEffect(ModItems.COOLDOWN)) {
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
                voice(player, Sounds.POJUN);
                int i = target instanceof Player ? 200 : 40;
                player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, i,0, false,false,true));
            }
        }
    }

    public static class Qice extends ActiveSkill {
        public Qice(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 20s" : "CD: 20s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.qice.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            ItemStack offStack = user.getOffhandItem();
            int cd = getCD(stack);
            if (!offStack.isEmpty() && offStack.is(Tags.CARD) && offStack.getCount() > 1) {
                if (cd == 0) {

                    ItemStack[] stacks = {new ItemStack(ModItems.BINGLIANG_ITEM), new ItemStack(ModItems.TOO_HAPPY_ITEM), new ItemStack(ModItems.DISCARD), new ItemStack(ModItems.FIRE_ATTACK), new ItemStack(ModItems.JIEDAO), new ItemStack(ModItems.JUEDOU), new ItemStack(ModItems.NANMAN), new ItemStack(ModItems.STEAL), new ItemStack(ModItems.TAOYUAN), new ItemStack(ModItems.TIESUO), new ItemStack(ModItems.WANJIAN), new ItemStack(ModItems.WUXIE), new ItemStack(ModItems.WUZHONG)};
                    Container inventory = new SimpleContainer(20);
                    for (var stack1 : stacks) inventory.setItem(Arrays.stream(stacks).toList().indexOf(stack1), stack1);
                    inventory.setItem(18, stack); //用于在Handler中找到原本的stack

                    openSimpleMenu(user, user, inventory, Component.translatable("item.dabaosword.qice.screen"));
                }
                else {user.displayClientMessage(Component.translatable("dabaosword.cooldown").withStyle(ChatFormatting.RED), true);}
            }
            else {user.displayClientMessage(Component.translatable("item.dabaosword.qice.tip").withStyle(ChatFormatting.RED), true);}
        }
    }

    public static class Qingguo extends SkillItem {
        public Qingguo(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.qingguo.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() instanceof Player player) viewAs(player, stack, 5, ModTools::nonBasic, new ItemStack(ModItems.SHAN), Sounds.QINGGUO);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Qixi extends SkillItem {
        public Qixi(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.qixi.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() instanceof Player player) viewAs(player, stack, 5, ModTools::nonBasic, new ItemStack(ModItems.DISCARD), Sounds.QIXI);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Quanji extends SkillItem {
        public Quanji(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int quan = getTag(stack);
            tooltip.add(Component.nullToEmpty("tags: "+quan));
            tooltip.add(Component.translatable("item.dabaosword.quanji.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.quanji.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
            if (!world.isClientSide && !user.isShiftKeyDown()) voice(user, Sounds.ZILI);
            return super.use(world, user, hand);
        }

        @Override
        public void onHurt(ItemStack stack, LivingEntity entity, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity) {
                int quan = getTag(stack);
                setTag(stack, quan + 1);
                voice(entity, Sounds.QUANJI);
            }
        }

        @Override
        public Tuple<Float, Float> modifyDamage(LivingEntity entity, DamageSource source, float amount) {
            if (source.getDirectEntity() instanceof LivingEntity s && hasTrinket(SkillCards.QUANJI, s)) {
                ItemStack stack = trinketItem(SkillCards.QUANJI, s);
                int quan = getTag(stack);
                if (quan > 0) {
                    if (quan > 4 && entity instanceof Player player) draw(player, 2);
                    setTag(stack, quan/2);
                    voice(s, Sounds.PAIYI);
                    return new Tuple<>(0f, (float) quan);
                }
            }
            return super.modifyDamage(entity, source, amount);
        }
    }

    public static class Rende extends ActiveSkillWithTarget {
        public Rende(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 30s" : "CD: 30s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.rende.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.rende.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            openInv(user, target, Component.translatable("give_card.title", stack.getDisplayName()), targetInv(user, false, false, 2, stack));
        }
    }

    public static class Shanzhuan extends SkillItem {
        public Shanzhuan(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 8s"));
            tooltip.add(Component.translatable("item.dabaosword.shanzhuan.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.shanzhuan.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        //擅专：我言既出，谁敢不从！
        @Override
        public void postDamage(ItemStack stack, LivingEntity entity, LivingEntity attacker, float amount) {
            if (attacker instanceof Player player && !player.hasEffect(ModItems.COOLDOWN)) {
                if (entity instanceof Player target) openInv(player, target, Component.translatable("dabaosword.discard.title", stack.getDisplayName()), targetInv(target, true, false, 1, stack));
                else {
                    voice(player, Sounds.SHANZHUAN);
                    if (new Random().nextFloat() < 0.5) {
                        entity.addEffect(new MobEffectInstance(ModItems.BINGLIANG, MobEffectInstance.INFINITE_DURATION,1));
                    } else {
                        entity.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
                    }
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 5,0,false,false,true));
                }
            }
        }
    }

    public static class Shensu extends SkillItem {
        public Shensu(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.shensu.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.shensu.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public Tuple<Float, Float> modifyDamage(LivingEntity target, DamageSource source, float amount) {
            if (source.getDirectEntity() instanceof LivingEntity attacker) {
                if (hasTrinket(SkillCards.SHENSU, attacker) && !attacker.hasEffect(ModItems.COOLDOWN)) {
                    float walkSpeed = 4.317f;
                    float speed = trinketItem(SkillCards.SHENSU, attacker).get(DataComponents.CUSTOM_DATA).copyTag().getFloat("speed");
                    if (speed > walkSpeed) {
                        float m = (speed - walkSpeed) / walkSpeed / 2;
                        attacker.addEffect(new MobEffectInstance(ModItems.COOLDOWN, (int) (5 * 20 * m),0,false,false,true));
                        if (attacker instanceof Player player) player.displayClientMessage(Component.translatable("shensu.info", speed, m), false);
                        voice(attacker, Sounds.SHENSU);
                        return new Tuple<>(m, 0f);
                    }
                }
            }
            return super.modifyDamage(target, source, amount);
        }

        @Override
        public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
            Multimap<Holder<Attribute>, AttributeModifier> multimap = LinkedHashMultimap.create();
            LivingEntity entity = slotContext.entity();
            double d = 0;
            if (entity instanceof Player player && noTieji(player)) d = Math.min(getEmptySlots(player), 20d) / 40; //当空余20格时，获得最大加成0.5
            AttributeModifier modifier = new AttributeModifier(id, d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            multimap.put(Attributes.MOVEMENT_SPEED, modifier);
            return multimap;
        }

        private int getEmptySlots(Player player) {
            int i = 0;
            for (var slot : player.getInventory().items) {if (slot.isEmpty()) i++;}
            return i;
        }
    }

    public static class Taoluan extends ActiveSkill {
        public Taoluan(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.taoluan.tooltip"));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            if (user.getHealth() + 5 * count(user, Tags.RECOVER) > 4.99) {

                ItemStack[] stacks = {new ItemStack(ModItems.THUNDER_SHA), new ItemStack(ModItems.FIRE_SHA), new ItemStack(ModItems.SHAN), new ItemStack(ModItems.PEACH), new ItemStack(ModItems.JIU), new ItemStack(ModItems.BINGLIANG_ITEM), new ItemStack(ModItems.TOO_HAPPY_ITEM), new ItemStack(ModItems.DISCARD), new ItemStack(ModItems.FIRE_ATTACK), new ItemStack(ModItems.JIEDAO), new ItemStack(ModItems.JUEDOU), new ItemStack(ModItems.NANMAN), new ItemStack(ModItems.STEAL), new ItemStack(ModItems.TAOYUAN), new ItemStack(ModItems.TIESUO), new ItemStack(ModItems.WANJIAN), new ItemStack(ModItems.WUXIE), new ItemStack(ModItems.WUZHONG)};
                Container inventory = new SimpleContainer(20);
                for (var stack1 : stacks) inventory.setItem(Arrays.stream(stacks).toList().indexOf(stack1), stack1);
                inventory.setItem(18, stack);

                openSimpleMenu(user, user, inventory, Component.translatable("item.dabaosword.taoluan.screen"));
            }
            else {user.displayClientMessage(Component.translatable("item.dabaosword.taoluan.tip").withStyle(ChatFormatting.RED), true);}
        }
    }

    public static class Tieji extends SkillItem {
        public Tieji(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.tieji.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.tieji.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player player) {
            if (getShaSlot(player) != -1) {
                voice(player, Sounds.TIEJI);
                target.addEffect(new MobEffectInstance(ModItems.TIEJI,200,0,false,true,true));
                if (new Random().nextFloat() < 0.75) target.addEffect(new MobEffectInstance(ModItems.COOLDOWN2,2,0,false,false,false));
            }
        }
    }

    public static class Yiji extends ActiveSkillWithTarget {
        public Yiji(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 20s"));
            tooltip.add(Component.translatable("item.dabaosword.yiji.tooltip").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.yiji.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void onHurt(ItemStack stack, LivingEntity entity, DamageSource source, float amount) {
            if (entity instanceof Player player && !player.hasEffect(ModItems.COOLDOWN) && player.getHealth() <= 12) {
                draw(player, 2);
                player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 20, 0, false, false, true));
                setTag(stack, 2);
                voice(player, Sounds.YIJI);
            }
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            int i = getTag(stack);
            if (i > 0 ) openInv(user, target, Component.translatable("give_card.title", stack.getDisplayName()), targetInv(user, false, false, 2, stack));
        }
    }

    public static class Zhiheng extends ActiveSkill {
        public Zhiheng(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int z = getTag(stack);
            tooltip.add(Component.literal("uses: " + z));
            tooltip.add(Component.translatable("item.dabaosword.zhiheng.tooltip1").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.zhiheng.tooltip2").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().level() instanceof ServerLevel world) {
                int z = getTag(stack);
                if (z < 10 && world.getGameTime() % 100 == 0) setTag(stack, z + 1);
            }
            super.curioTick(slotContext, stack);
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            int z = getTag(stack);
            if (z > 0) openInv(user, user, Component.translatable("zhiheng.title"), targetInv(user, true, false, 2, stack));
            else user.displayClientMessage(Component.translatable("zhiheng.fail").withStyle(ChatFormatting.RED), true);
        }
    }

    public static class Zhijian extends ActiveSkillWithTarget {
        public Zhijian(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.zhijian.tooltip1").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.zhijian.tooltip2").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            ItemStack itemStack = user.getMainHandItem();
            if (itemStack.getItem() instanceof Equipment && itemStack.getItem() != ModItems.CARD_PILE) {
                cardMove(user, target, itemStack, itemStack.getCount(), CardCBs.T.INV_TO_EQUIP);
                Equipment.equipItem(target, itemStack);
                voice(user, Sounds.ZHIJIAN);
                draw(user);
            } else user.displayClientMessage(Component.translatable("zhijian.fail").withStyle(ChatFormatting.RED), true);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {

        if (stack.getItem() == SkillCards.XIAOJI) {
            tooltip.add(Component.translatable("item.dabaosword.xiaoji.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.LIANYING) {
            tooltip.add(Component.translatable("item.dabaosword.lianying.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.XINGSHANG) {
            tooltip.add(Component.translatable("item.dabaosword.xingshang.tooltip").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.JIZHI) {
            tooltip.add(Component.translatable("item.dabaosword.jizhi.tooltip").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.MASHU) {
            tooltip.add(Component.translatable("item.dabaosword.chitu.tooltip"));
        }

        if (stack.getItem() == SkillCards.FEIYING) {
            tooltip.add(Component.translatable("item.dabaosword.dilu.tooltip"));
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world && !equipped(stack)) {
            world.players().forEach(player -> player.displayClientMessage(
                    Component.literal(slotContext.entity().getScoreboardName()).append(Component.literal(" equipped ").append(stack.getDisplayName())),false
            ));
            setEquipped(stack, true);
        }
    }

    public static boolean equipped(ItemStack stack) {
        if (stack.get(DataComponents.CUSTOM_DATA) != null) {
            return Objects.requireNonNull(stack.get(DataComponents.CUSTOM_DATA)).contains("equipped");
        }
        return false;
    }

    public static void setEquipped(ItemStack stack, boolean equipped) {
        if (equipped) {
            CompoundTag nbt = new CompoundTag(); nbt.putBoolean("equipped", true);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        } else if (stack.get(DataComponents.CUSTOM_DATA) != null) stack.remove(DataComponents.CUSTOM_DATA);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && user.getTags().contains("change_skill") && hand == InteractionHand.OFF_HAND && user.isShiftKeyDown()) {
            ItemStack stack = user.getItemInHand(hand);
            if (stack.is(Tags.SKILL)) {
                stack.setCount(0);
                changeSkill(user);
                user.getTags().remove("change_skill");
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world) {
            int cd = getCD(stack); //世界时间除以20取余为0时，技能内置CD减一秒
            if (cd > 0 && world.getGameTime() % 20 == 0) setCD(stack, cd - 1);
        }
    }

    public static void changeSkill(Player player) {
        var selectedId = parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/change_skill.json"));
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(selectedId));
        if (stack.getItem() != Items.AIR) voice(player, Sounds.GIFTBOX,3);
        give(player, stack);
    }

    public static class ActiveSkill extends SkillItem {
        public ActiveSkill(Properties p_41383_) {super(p_41383_);}
    }

    public static class ActiveSkillWithTarget extends SkillItem {
        public ActiveSkillWithTarget(Properties p_41383_) {super(p_41383_);}
    }
}
