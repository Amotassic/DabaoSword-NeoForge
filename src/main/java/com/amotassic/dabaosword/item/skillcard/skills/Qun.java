package com.amotassic.dabaosword.item.skillcard.skills;

import com.amotassic.dabaosword.api.Card;
import com.amotassic.dabaosword.api.ICardEvent;
import com.amotassic.dabaosword.api.ReachDefend;
import com.amotassic.dabaosword.command.TriggerSkillCommand;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.amotassic.dabaosword.util.ModTools.*;
import static com.amotassic.dabaosword.util.ModTools.voice;

@SuppressWarnings("all")
public class Qun {

    public static class Jijiu extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 10s"));
            tooltip.add(Component.translatable("item.dabaosword.jijiu.tooltip"));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            viewAs(slotContext.entity(), stack, 10, isRedCard, new ItemStack(ModItems.PEACH));
            super.curioTick(slotContext, stack);
        }
    }

    public static class Jiuchi extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 10s"));
            tooltip.add(Component.translatable("item.dabaosword.jiuchi.tooltip"));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            viewAs(slotContext.entity(), stack, 10, isSpadeCard, new ItemStack(ModItems.JIU));
            super.curioTick(slotContext, stack);
        }
    }

    public static class Jizhan extends SkillItem implements TriggerSkillCommand.CSkill {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.jizhan.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.jizhan.tooltip2"));
        }

        private final Component JIZHAN_TEXT = Component.translatable("jizhan.text",
                Component.translatable("rank.higher").withStyle(ChatFormatting.AQUA).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dabaosword dabaosword:jizhan 1"))),
                Component.translatable("rank.lower").withStyle(ChatFormatting.AQUA).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dabaosword dabaosword:jizhan -1"))));

        @Override
        public int onDrawPhase(Player player, ItemStack stack) {
            voice(player, stack);
            ItemStack last = newCard();
            give(player, last); //先让玩家摸一张牌，保存到lastCard
            player.level().players().forEach(p -> p.displayClientMessage(Component.translatable("jizhan.draw", player.getDisplayName(), stack.getDisplayName(), last.getDisplayName(), Objects.requireNonNull(getRank(last)).rank), false));
            var tag = getOrCreateNbt(stack);
            tag.putInt("lastCardRank", Objects.requireNonNull(getRank(last)).ordinal());
            setNbt(stack, tag);
            player.displayClientMessage(JIZHAN_TEXT, false);
            return -114;
        }

        @Override
        public void triggerSkill(LivingEntity entity, ItemStack stack, int value) {
            int last = getOrCreateNbt(stack).getInt("lastCardRank");
            if (last == -1 || !(entity instanceof Player player)) return;
            ItemStack next = newCard();
            give(player, next); //又让玩家摸一张牌后，比较两张牌的点数，如果玩家选对了，就把新的牌保存到lastCard，否则关闭菜单
            player.level().players().forEach(p -> p.displayClientMessage(Component.translatable("jizhan.draw", player.getDisplayName(), stack.getDisplayName(), next.getDisplayName(), Objects.requireNonNull(getRank(next)).rank), false));
            //下一张牌与上一张牌点数比较，有3种情况：更大返回1，更小返回-1，相等返回0
            int cmp = Integer.compare(Objects.requireNonNull(getRank(next)).ordinal(), last);
            //玩家选择只有两张情况：选更大返回1，选更小返回-1
            var tag = getOrCreateNbt(stack);
            if (cmp == value) {
                tag.putInt("lastCardRank", Objects.requireNonNull(getRank(next)).ordinal());
                player.displayClientMessage(JIZHAN_TEXT, false);
            } else tag.putInt("lastCardRank", -1);
            setNbt(stack, tag);
        }
    }

    public static class Leiji extends SkillItem implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.leiji.tooltip"));
        }

        @Override
        public void postCardUse(LivingEntity user, ItemStack card, LivingEntity target, ItemStack skill) {
            if (card.is(ModItems.SHAN)) {
                voice(user, skill);
                user.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 10,3,false,false,false));
            }
        }

        @Override
        public Priority getPriority(LivingEntity target, DamageSource source, float amount) {return Priority.NORMAL;}

        @Override
        public boolean cancelDamage(LivingEntity target, DamageSource source, float amount) {
            return source.is(DamageTypes.LIGHTNING_BOLT) && source.getEntity() == null;
        }
    }

    public static class Luanji extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.luanji.tooltip"));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            super.curioTick(slotContext, stack);
            if (!entity.level().isClientSide && entity instanceof Player player && noTieji(player) && getCD(stack) == 0) {
                ItemStack off = player.getOffhandItem();
                CompoundTag nbt = getOrCreateNbt(stack);
                Card.Suits firstSuit = null;
                if (nbt.contains("suit")) firstSuit = Card.Suits.valueOf(nbt.getString("suit"));
                if (entity.level().getGameTime() % 100 == 0 && firstSuit != null) {
                    player.displayClientMessage(Component.translatable("item.dabaosword.luanji.suit", stack.getDisplayName(), firstSuit.suit), true);
                }
                Card.Suits suit = getSuit(off);
                if (isCard(off) && suit != null) {
                    if (firstSuit == suit) { //如果记录花色和当前牌花色相同，就移除一张牌，获得万箭齐发，技能进入CD
                        nbt.remove("suit");
                        setNbt(stack, nbt);
                        setCD(stack, 15);
                        off.shrink(1);
                        give(player, new ItemStack(ModItems.WANJIAN));
                        voice(player, Sounds.LUANJI);
                        return;
                    }
                    if (firstSuit == null) { //如果没有记录花色，就移除一张牌，记录该花色
                        nbt.putString("suit", suit.name());
                        setNbt(stack, nbt);
                        off.shrink(1);
                    }
                }
            }
        }
    }

    public static class Taoluan extends SkillItem.ActiveSkill {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.taoluan.tooltip"));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            String[] used = getOrCreateNbt(stack).getString("used").split(";");
            if (used.length == 18) {
                user.displayClientMessage(Component.translatable("item.dabaosword.taoluan.fail").withStyle(ChatFormatting.RED), true);
                return;
            }
            if (user.getHealth() + 5 * countCard(user, canSaveDying) > 4.99) {

                ItemStack[] stacks = {new ItemStack(ModItems.THUNDER_SHA), new ItemStack(ModItems.FIRE_SHA), new ItemStack(ModItems.SHAN), new ItemStack(ModItems.PEACH), new ItemStack(ModItems.JIU), new ItemStack(ModItems.BINGLIANG_ITEM), new ItemStack(ModItems.TOO_HAPPY_ITEM), new ItemStack(ModItems.DISCARD), new ItemStack(ModItems.FIRE_ATTACK), new ItemStack(ModItems.JIEDAO), new ItemStack(ModItems.JUEDOU), new ItemStack(ModItems.NANMAN), new ItemStack(ModItems.STEAL), new ItemStack(ModItems.TAOYUAN), new ItemStack(ModItems.TIESUO), new ItemStack(ModItems.WANJIAN), new ItemStack(ModItems.WUXIE), new ItemStack(ModItems.WUZHONG)};
                Container inventory = new SimpleContainer(20);
                for (var stack1 : stacks) {
                    if (Arrays.stream(used).toList().contains(BuiltInRegistries.ITEM.getKey(stack1.getItem()).getPath())) continue;
                    inventory.setItem(Arrays.stream(stacks).toList().indexOf(stack1), stack1);
                }
                inventory.setItem(18, stack);

                openSimpleMenu(user, user, inventory, Component.translatable("item.dabaosword.taoluan.screen"));
            } else user.displayClientMessage(Component.translatable("item.dabaosword.taoluan.tip").withStyle(ChatFormatting.RED), true);
        }

        @Override
        public void onClickGUISlot(Player player, ItemStack stack, Player target, ItemStack selected, int slotIndex) {
            give(player, selected);
            if (!player.isCreative()) {
                var nbt = getOrCreateNbt(stack);
                String used = nbt.getString("used"); String item = BuiltInRegistries.ITEM.getKey(selected.getItem()).getPath();
                used = used.isEmpty() ? item : used + ";" + item;
                nbt.putString("used", used); setNbt(stack, nbt);
                player.invulnerableTime = 0;
                player.hurt(player.damageSources().genericKill(), 4.99f);
            }
            voice(player, Sounds.TAOLUAN);
            closeGUI(player);
        }
    }

    public static class Weimu extends SkillItem implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.weimu.tooltip"));
        }

        @Override
        public boolean canUseIfTargetHasSkill(LivingEntity user, ItemStack card, LivingEntity target, ItemStack skill) {
            if (isBlackCard.and(isArmoury).test(card)) {
                voice(target, skill); return false;
            }
            return true;
        }

        @Override
        public boolean canHurtByCard(LivingEntity entity, ItemStack skill, ItemStack card) {
            if (card.is(ModItems.NANMAN)) {voice(entity, skill); return false;}
            return true;
        }
    }

    public static class Mashu extends SkillItem implements ReachDefend {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.chitu.tooltip"));
        }

        @Override
        public int getExtraReach(Player player, ItemStack stack) {return 1;}
    }

    public static class Feiying extends SkillItem implements ReachDefend {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.dilu.tooltip"));
        }

        @Override
        public int getDefend(Player player, ItemStack stack) {return 1;}
    }
}
