package com.amotassic.dabaosword.item.skillcard.skills;

import com.amotassic.dabaosword.api.ICardEvent;
import com.amotassic.dabaosword.api.ReachDefend;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.api.event.CardEvents.cardMove;
import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("all")
public class Shu {

    public static class Benxi extends SkillItem implements ReachDefend, ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int benxi = getTag(stack);
            tooltip.add(Component.nullToEmpty("tags: " + benxi));
            tooltip.add(Component.translatable("item.dabaosword.benxi.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.benxi.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public int getExtraReach(Player player, ItemStack stack) {return getTag(stack);}

        @Override
        public void postCardUse(LivingEntity user, ItemStack card, LivingEntity target, ItemStack skill) {
            int benxi = getTag(skill);
            if (benxi < 5) {setTag(skill, benxi + 1); voice(user, skill);}
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
    }

    public static class Huoji extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.huoji.tooltip").withStyle(ChatFormatting.RED));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            viewAs(slotContext.entity(), stack, 15, isRedCard, new ItemStack(ModItems.FIRE_ATTACK));
            super.curioTick(slotContext, stack);
        }
    }

    public static class Jizhi extends SkillItem implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.jizhi.tooltip").withStyle(ChatFormatting.RED));
        }

        @Override
        public void postCardUse(LivingEntity user, ItemStack card, LivingEntity target, ItemStack skill) {
            if (isArmoury.test(card)) {draw(user); voice(user, skill);}
        }
    }

    public static class Kanpo extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 10s" : "CD: 10s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.kanpo.tooltip").withStyle(ChatFormatting.RED));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            viewAs(slotContext.entity(), stack, 10, isBlackCard, new ItemStack(ModItems.WUXIE));
            super.curioTick(slotContext, stack);
        }
    }

    public static class Kuanggu extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 8s"));
            tooltip.add(Component.translatable("item.dabaosword.kuanggu.tooltip").withStyle(ChatFormatting.RED));
        }

        @Override
        public void postAttack(ItemStack stack, LivingEntity target, LivingEntity attacker, float amount) {
            if (!attacker.hasEffect(ModItems.COOLDOWN)) {
                if (attacker.getMaxHealth() - attacker.getHealth()>=5) attacker.heal(5);
                else draw(attacker);
                voice(attacker, stack);
                attacker.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 8,0,false,false,true));
            }
        }
    }

    public static class Liegong extends SkillItem implements ReachDefend {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.liegong.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.liegong.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public int getExtraReach(Player player, ItemStack stack) {
            return player.hasEffect(ModItems.COOLDOWN) ? 0 : 13;
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
                    return new Tuple<>(0f, f);
                }
            }
            return null;
        }
    }

    public static class Longdan extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.longdan.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.longdan.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().level() instanceof ServerLevel world && slotContext.entity() instanceof Player player && noTieji(slotContext.entity())) {
                ItemStack stack1 = player.getOffhandItem(); ItemStack copy = stack1.copy();
                if (world.getGameTime() % 20 == 0 && isBasic.test(stack1)) {
                    stack1.shrink(1);
                    if (isSha.test(copy)) give(player, new ItemStack(ModItems.SHAN));
                    if (copy.is(ModItems.SHAN)) give(player, new ItemStack(ModItems.SHA));
                    if (copy.is(ModItems.PEACH)) give(player, new ItemStack(ModItems.JIU));
                    if (copy.is(ModItems.JIU)) give(player, new ItemStack(ModItems.PEACH));
                    voice(player, Sounds.LONGDAN);
                }
            }
            super.curioTick(slotContext, stack);
        }
    }

    public static class Rende extends SkillItem.ActiveSkillWithTarget {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 30s" : "CD: 30s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.rende.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.rende.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            openInv(user, target, Component.translatable("give_card.title", stack.getDisplayName()), stack, true, false, false, 2);
        }

        @Override
        public void onClickGUISlot(Player player, ItemStack stack, Player target, ItemStack selected, int slotIndex) {
            voice(player, Sounds.RENDE);
            Component message = Component.translatable("give_card.tip", player.getDisplayName(), stack.getDisplayName(), target.getDisplayName(), selected.getDisplayName());
            target.displayClientMessage(message, false);
            player.displayClientMessage(message, false);
            cardMove(player, target, selected, 1, false, false);
            int cd = getCD(stack);
            if (player.getHealth() < player.getMaxHealth() && cd == 0 && new Random().nextFloat() < 0.5) {
                player.heal(5); voice(player, Sounds.RECOVER);
                player.displayClientMessage(Component.translatable("recover.tip").withStyle(ChatFormatting.GREEN), true);
                setCD(stack, 30);
            }
        }
    }

    public static class Tieji extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.tieji.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.tieji.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player player) {
            if (hasItem(player, isSha)) {
                voice(player, Sounds.TIEJI);
                target.addEffect(new MobEffectInstance(ModItems.TIEJI,200,0,false,true,true));
                if (new Random().nextFloat() < 0.75) target.addEffect(new MobEffectInstance(ModItems.COOLDOWN2,2,0,false,false,false));
            }
        }
    }

    public static class Wusheng extends SkillItem implements ReachDefend {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.wusheng.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.wusheng.tooltip2").withStyle(ChatFormatting.RED));
        }

        @Override
        public int getExtraReach(Player player, ItemStack stack) {
            return isSha.test(player.getMainHandItem()) ? 13 : 0;
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            viewAs(slotContext.entity(), stack, 5, isRedCard, new ItemStack(ModItems.SHA));
            super.curioTick(slotContext, stack);
        }
    }
}
