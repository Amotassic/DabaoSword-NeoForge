package com.amotassic.dabaosword.item.skillcard.skills;

import com.amotassic.dabaosword.api.ICardEvent;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.util.Sounds;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.*;

import static com.amotassic.dabaosword.api.event.CardEvents.*;
import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("all")
public class Wei {

    public static class Duanliang extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.duanliang.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            viewAs(slotContext.entity(), stack, 5, isBlackCard.and(isArmoury.negate()), new ItemStack(ModItems.BINGLIANG_ITEM));
            super.curioTick(slotContext, stack);
        }
    }

    public static class Fangzhu extends SkillItem {
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
                            List<ItemStack> candidate = getItems(target, isCard, true, false, true, true);
                            if (!candidate.isEmpty()) {
                                ItemStack chosen = candidate.get(new Random().nextInt(candidate.size()));
                                Component message = Component.translatable("dabaosword.discard", entity.getDisplayName(), target.getDisplayName(), chosen.getDisplayName());
                                if (entity instanceof Player player) player.displayClientMessage(message, false);
                                target.displayClientMessage(message, false);
                                cardDiscard(target, chosen, 1, isEquipped(entity, s -> s.equals(chosen)));
                            }
                        } else {//如果来源不是玩家则随机弃置它的主副手物品和装备
                            List<ItemStack> candidate = getItems(attacker, s -> !s.isEmpty(), true, false, true, false);
                            if(!candidate.isEmpty()) {
                                ItemStack chosen = candidate.get(new Random().nextInt(candidate.size()));
                                if (isCard(chosen)) cardDiscard(entity, chosen, 1, isEquipped(entity, s -> s.equals(chosen)));
                                chosen.shrink(1);
                            }
                        }
                    }
                }
            }
        }
    }

    public static class Gongao extends SkillItem {
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

    public static class Jianxiong extends SkillItem implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 15s"));
            tooltip.add(Component.translatable("item.dabaosword.jianxiong.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.jianxiong.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void onHurt(ItemStack stack, LivingEntity entity, DamageSource source, float amount) {
            if (source.getEntity() instanceof Entity && !entity.hasEffect(ModItems.COOLDOWN)) {
                voice(entity, stack);
                draw(entity);
                entity.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 15,0,false,false,true));
            }
        }

        @Override
        public void onHurtByCard(LivingEntity entity, ItemStack skill, ItemStack card) {
            if (getCD(skill) == 0) {
                voice(entity, skill);
                setCD(skill, 15);
                give(entity, card.copyWithCount(1));
            }
        }
    }

    public static class Jueqing extends SkillItem {
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

    public static class Luoshen extends SkillItem.ActiveSkill {
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

    public static class Qice extends SkillItem.ActiveSkill {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 20s" : "CD: 20s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.qice.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            int cd = getCD(stack);
            if (countCards(user) > 0) {
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

        @Override
        public void onClickGUISlot(Player player, ItemStack stack, Player target, ItemStack selected, int slot) {
            if (!player.isCreative()) {
                while (countCards(player) > 0) {cardDecrement(getCard(player, isCard), 64);}
                setCD(stack, 20);
            }
            give(player, selected);
            voice(player, Sounds.QICE);
            closeGUI(player);
        }
    }

    public static class Qingguo extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.qingguo.tooltip").withStyle(ChatFormatting.BLUE));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            viewAs(slotContext.entity(), stack, 5, isBlackCard, new ItemStack(ModItems.SHAN));
            super.curioTick(slotContext, stack);
        }
    }

    public static class Quanji extends SkillItem {
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
                    if (quan > 4) draw(entity, 2);
                    setTag(stack, quan/2);
                    voice(s, Sounds.PAIYI);
                    return new Tuple<>(0f, (float) quan);
                }
            }
            return null;
        }
    }

    public static class Shanzhuan extends SkillItem {
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
                if (entity instanceof Player target) {
                    if (countAllCards(target) > 0) openInv(player, target, Component.translatable("dabaosword.discard.title", stack.getDisplayName()), stack, false, true, false, 1);
                } else {
                    voice(player, Sounds.SHANZHUAN);
                    if (new Random().nextFloat() < 0.5) {
                        entity.addEffect(new MobEffectInstance(ModItems.BINGLIANG, MobEffectInstance.INFINITE_DURATION,1));
                    } else entity.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
                    player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 5,0,false,false,true));
                }
            }
        }

        @Override
        public void onClickGUISlot(Player player, ItemStack stack, Player target, ItemStack selected, int slotIndex) {
            voice(player, Sounds.SHANZHUAN);
            if (isRedCard.test(selected)) target.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
            else target.addEffect(new MobEffectInstance(ModItems.BINGLIANG, -1,1));
            Component message = Component.translatable("dabaosword.discard", player.getDisplayName(), target.getDisplayName(), selected.getDisplayName());
            player.displayClientMessage(message, false);
            target.displayClientMessage(message, false);
            cardDiscard(target, selected, 1, slotIndex < 4);
            player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 12,0,false,false,true));
            closeGUI(player);
        }
    }

    public static class Shensu extends SkillItem {
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
                    float speed = getOrCreateNbt(trinketItem(SkillCards.SHENSU, attacker)).getFloat("speed");
                    if (speed > walkSpeed) {
                        float m = (speed - walkSpeed) / walkSpeed / 2;
                        attacker.addEffect(new MobEffectInstance(ModItems.COOLDOWN, (int) (5 * 20 * m),0,false,false,true));
                        if (attacker instanceof Player player) player.displayClientMessage(Component.translatable("shensu.info", speed, m), false);
                        voice(attacker, Sounds.SHENSU);
                        return new Tuple<>(m, 0f);
                    }
                }
            }
            return null;
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

    public static class Xingshang extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.xingshang.tooltip").withStyle(ChatFormatting.BLUE));
        }
    }

    public static class Yiji extends SkillItem.ActiveSkillWithTarget {
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
            if (i > 0 ) openInv(user, target, Component.translatable("give_card.title", stack.getDisplayName()), stack, true, false, false, 2);
        }

        @Override
        public void onClickGUISlot(Player player, ItemStack stack, Player target, ItemStack selected, int slotIndex) {
            int i = getTag(stack);
            Component message = Component.translatable("give_card.tip", player.getDisplayName(), stack.getDisplayName(), target.getDisplayName(), selected.getDisplayName());
            target.displayClientMessage(message, false);
            player.displayClientMessage(message, false);
            cardMove(player, target, selected, 1, false, false);
            setTag(stack, i - 1);
            if (i - 1 == 0) closeGUI(player);
        }
    }
}
