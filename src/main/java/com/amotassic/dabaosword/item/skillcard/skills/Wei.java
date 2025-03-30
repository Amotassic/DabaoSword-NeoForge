package com.amotassic.dabaosword.item.skillcard.skills;

import com.amotassic.dabaosword.api.skill.*;
import com.amotassic.dabaosword.event.PlayerEvents;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.amotassic.dabaosword.api.CardEvents.*;
import static com.amotassic.dabaosword.util.ModTools.*;
import static net.minecraft.ChatFormatting.BLUE;
import static net.minecraft.ChatFormatting.RED;

public class Wei {

    public static class Duanliang extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(getTip(BLUE));
        }

        @Override
        public void tickSkill(Skill skill, LivingEntity entity) {
            viewAs(entity, skill, 5, isBlackCard.and(isArmoury.negate()), ModItems.BINGLIANG_ITEM);
        }
    }

    public static class Fangzhu extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {tooltip.add(getTip(BLUE));}

        @SkillInfo(trigger = Trigger.ON_HURT, relation = Relation.SELF)
        public int onHurt(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            var source = data.source; var amount = data.amount;
            if (source.getEntity() instanceof LivingEntity attacker && user != attacker) {
                int i = attacker instanceof Player ? (int) (20 * amount + 60) : 300;
                attacker.addEffect(new MobEffectInstance(ModItems.TURNOVER, i));
                voice(user, this);
            }
            return 0;
        }
    }

    public static class Ganglie extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(getTip("1", BLUE));
            tooltip.add(getTip("2", BLUE));
        }

        @SkillInfo(trigger = Trigger.ON_HURT, relation = Relation.SELF)
        public int onHurt(LivingEntity user, LivingEntity entity, Skill skill, ExData data) {
            var source = data.source; var amount = data.amount;
            if (source.getEntity() instanceof LivingEntity attacker && user != attacker) {
                voice(user, this);
                for (int i = 0; i < amount; i += 5) {
                    if (new Random().nextFloat() < 0.5) { //造成伤害
                        user.addTag("sha"); //以此造成伤害不自动触发杀
                        float f = i + 5 < amount ? 5 : amount - i;
                        attacker.invulnerableTime = 0; attacker.hurt(user.damageSources().mobAttack(user), f);
                    } else { //弃牌
                        if (attacker instanceof Player target) { //如果来源是玩家则弃牌
                            List<ItemStack> candidate = getItems(target, isCard, true, false, true, true);
                            if (!candidate.isEmpty()) {
                                ItemStack chosen = candidate.get(new Random().nextInt(candidate.size()));
                                Component message = Component.translatable("dabaosword.discard", user.getDisplayName(), target.getDisplayName(), chosen.getDisplayName());
                                if (user instanceof Player player) player.displayClientMessage(message, false);
                                target.displayClientMessage(message, false);
                                var cData = d().cards(chosen, 1, isEquipped(attacker, s -> s.equals(chosen)));
                                cardDiscard(target, cData);
                            }
                        } else { //如果来源不是玩家则随机弃置它的主副手物品和装备
                            List<ItemStack> candidate = getItems(attacker, s -> !s.isEmpty(), true, false, true, false);
                            if (!candidate.isEmpty()) {
                                ItemStack chosen = candidate.get(new Random().nextInt(candidate.size()));
                                if (isCard(chosen)) {
                                    var cData = d().cards(chosen, 1, isEquipped(attacker, s -> s.equals(chosen)));
                                    cardDiscard(attacker, cData);
                                }
                                else chosen.shrink(1);
                            }
                        }
                    }
                }
            }
            return 0;
        }
    }

    public static class Gongao extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(getTip("1", BLUE));
            tooltip.add(getTip("2", BLUE));
        }

        @Override public boolean lockOn() {return true;}

        @Override
        public void tickSkill(Skill skill, LivingEntity entity) {
            if (entity.level().isClientSide) return;
            int extraHP = skill.getTag();

            if (entity.level().getGameTime() % 600 == 0) { // 每30s触发扣体力上限
                if (entity instanceof Player player) {
                    if (extraHP >= 5 && !player.isCreative() && !player.isSpectator()) {
                        draw(player, 2);
                        skill.setTag(extraHP - 5);
                        voice(player, "weizhong");
                    }
                }
            }
        }

        @SkillInfo(trigger = Trigger.ON_DEATH, relation = Relation.KILLER)
        public int onKill(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            int extraHP = skill.getTag();
            if (target instanceof Monster) {
                extraHP += 1;
                user.heal(1);
                voice(user, this);
            }
            if (target instanceof Player) {
                extraHP += 5;
                user.heal(5);
                voice(user, this);
            }
            skill.setTag(extraHP);
            return 0;
        }

        @Override
        public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
            Multimap<Holder<Attribute>, AttributeModifier> multimap = LinkedHashMultimap.create();
            AttributeModifier Modifier = new AttributeModifier(id, s(stack).getTag(), AttributeModifier.Operation.ADD_VALUE);
            multimap.put(Attributes.MAX_HEALTH, Modifier);
            return multimap;
        }
    }

    public static class Jianxiong extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(Component.literal("CD: 15s"));
            tooltip.add(getTip("1", BLUE));
            tooltip.add(getTip("2", BLUE));
        }

        @SkillInfo(trigger = Trigger.ON_HURT, relation = Relation.SELF)
        public int onHurt(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            var source = data.source;
            if (source.getEntity() instanceof Entity && !user.hasEffect(ModItems.COOLDOWN)) {
                voice(user, this);
                draw(user);
                user.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 15,0,false,false,true));
            }
            return 0;
        }

        @SkillInfo(trigger = Trigger.HURT_BY_CARD, relation = Relation.SELF)
        public int hurtByCard(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            if (skill.getCD() == 0) {
                voice(user, this);
                skill.setCD(15);
                give(user, data.getCard().toStack().copyWithCount(1));
            }
            return 0;
        }
    }

    public static class Jueqing extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(getTip("1", BLUE));
            tooltip.add(getTip("2", BLUE));
        }

        @Override public boolean lockOn() {return true;}

        @SkillInfo(trigger = Trigger.CANCEL_DAMAGE_LOWEST, relation = Relation.ATTACKER_SELF)
        public int onHit(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            var amount = data.amount;
            target.hurt(target.damageSources().genericKill(), Math.min(Math.max(7, target.getMaxHealth() / 3), amount));
            voice(user, this, 1);
            return 1;
        }
    }

    public static class Luoshen extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {tooltip.add(getTip(BLUE));}

        @Override
        public int onDrawPhase(Player player, Skill skill) {
            voice(player, this);
            while (true) {
                var card = newCard();
                player.level().players().forEach(p -> p.displayClientMessage(Component.translatable("item.dabaosword.luoshen.result", player.getDisplayName(), card.getDisplayName()), false));
                if (isBlackCard.test(card)) give(player, card);
                else break;
            }
            return 0;
        }
    }

    public static class Luoyi extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {tooltip.add(getTip(BLUE));}

        @Override public boolean lockOn() {return true;}

        @Override
        public void tickSkill(Skill skill, LivingEntity entity) {
            if (!entity.level().isClientSide) gainStrength(entity, getEmptyArmorSlot(entity) + 1);
        }

        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
            if (!slotContext.entity().level().isClientSide) gainStrength(slotContext.entity(), 0);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
            if (!world.isClientSide && !user.isShiftKeyDown()) voice(user, this);
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

    public static class Qice extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            int cd = skill.getCD();
            tooltip.add(Component.literal(cd == 0 ? "CD: 20s" : "CD: 20s   left: "+ cd +"s"));
            tooltip.add(getTip(BLUE));
        }

        @Override public boolean isActiveSkill() {return true;}

        @Override
        public boolean activeSkill(Player user, Skill skill) {
            if (skill.getCD() == 0) {
                if (countCards(user) > 0) {

                    Item[] items = {ModItems.BINGLIANG_ITEM, ModItems.TOO_HAPPY_ITEM, ModItems.DISCARD, ModItems.FIRE_ATTACK, ModItems.JIEDAO, ModItems.JUEDOU, ModItems.NANMAN, ModItems.STEAL, ModItems.TAOYUAN, ModItems.TIESUO, ModItems.WANJIAN, ModItems.WUXIE, ModItems.WUGU, ModItems.WUZHONG};
                    var stacks = Arrays.stream(items).map(ItemStack::new).toList();

                    openMenu(user, user, skill.stack, stacks, Component.translatable("item.dabaosword.qice.screen"));
                    return true;
                } else user.displayClientMessage(Component.translatable("item.dabaosword.qice.tip").withStyle(RED), true);
            } else user.displayClientMessage(Component.translatable("dabaosword.cooldown").withStyle(RED), true);
            return false;
        }

        @Override
        public void onSlotClick(PlayerInvScreenHandler handler, Player player, Skill skill, Player target, int slot, int button, ClickType action) {
            var selected = handler.getStack(slot);
            if (!player.isCreative()) {
                while (countCards(player) > 0) {cardDecrement(player, getCard(player, isCard), 64);}
                skill.setCD(20);
            }
            give(player, selected);
            voice(player, this);
            closeGUI(player);
        }
    }

    public static class Qingguo extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(getTip(BLUE));
        }

        @Override
        public void tickSkill(Skill skill, LivingEntity entity) {
            viewAs(entity, skill, 5, isBlackCard, ModItems.SHAN);
        }
    }

    public static class Quanji extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(Component.literal("tags: " + skill.getTag()));
            tooltip.add(getTip("1", BLUE));
            tooltip.add(getTip("2", BLUE));
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
            if (!world.isClientSide && !user.isShiftKeyDown()) voice(user, "zili");
            return super.use(world, user, hand);
        }

        @SkillInfo(trigger = Trigger.ON_HURT, relation = Relation.SELF)
        public int onHurt(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            if (data.source.getEntity() instanceof LivingEntity) {
                skill.setTag(skill.getTag() + 1);
                voice(user, this);
            }
            return 0;
        }

        @SkillInfo(trigger = Trigger.MODIFY_DAMAGE, relation = Relation.DIRECT_ATTACKER)
        public int onHit(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            var adds = data.adds;
            int quan = skill.getTag();
            if (quan > 0) {
                if (quan > 4) draw(target);
                skill.setTag(quan/2);
                voice(user, "paiyi");
                adds.add((float) quan);
            }
            return 0;
        }
    }

    public static class Shanzhuan extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(Component.literal("CD: 8s"));
            tooltip.add(getTip("1", BLUE));
            tooltip.add(getTip("2", BLUE));
        }

        //擅专：我言既出，谁敢不从！
        @SkillInfo(trigger = Trigger.ON_HURT, relation = Relation.ATTACKER)
        public int onHit(LivingEntity user, LivingEntity entity, Skill skill, ExData data) {
            if (user instanceof Player player && !user.hasEffect(ModItems.COOLDOWN)) {
                if (entity instanceof Player target) {
                    if (countAllCards(target) > 0) openInv(player, target, target, Component.translatable("dabaosword.discard.title", skill.toHoverableText()), skill.stack, true, false, 1);
                } else {
                    voice(user, this);
                    if (new Random().nextFloat() < 0.5) {
                        entity.addEffect(new MobEffectInstance(ModItems.BINGLIANG, -1,1));
                    } else entity.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
                    user.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 5,0,false,false,true));
                }
            }
            return 0;
        }

        @Override
        public void onSlotClick(PlayerInvScreenHandler handler, Player player, Skill skill, Player target, int slot, int button, ClickType action) {
            var selected = handler.getStack(slot);
            voice(player, this);
            if (isRedCard.test(selected)) target.addEffect(new MobEffectInstance(ModItems.TOO_HAPPY, 20 * 5));
            else target.addEffect(new MobEffectInstance(ModItems.BINGLIANG, -1,1));
            Component message = Component.translatable("dabaosword.discard", player.getDisplayName(), target.getDisplayName(), selected.getDisplayName());
            player.displayClientMessage(message, false);
            target.displayClientMessage(message, false);
            var data = d().cards(selected, 1, slot < 4);
            cardDiscard(target, data);
            player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 12,0,false,false,true));
            closeGUI(player);
        }
    }

    public static class Shensu extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(getTip("1", BLUE));
            tooltip.add(getTip("2", BLUE));
        }

        @SkillInfo(trigger = Trigger.MODIFY_DAMAGE, relation = Relation.DIRECT_ATTACKER)
        public int onHit(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            var muls = data.muls;
            if (!user.hasEffect(ModItems.COOLDOWN)) {
                float walkSpeed = 4.317f;
                float speed = skill.getNbt().getFloat("speed");
                if (speed > walkSpeed) {
                    float m = (speed - walkSpeed) / walkSpeed / 2;
                    user.addEffect(new MobEffectInstance(ModItems.COOLDOWN, (int) (5 * 20 * m),0,false,false,true));
                    if (user instanceof Player player) player.displayClientMessage(Component.translatable("shensu.info", speed, m), true);
                    voice(user, this);
                    muls.add(m);
                }
            }
            return 0;
        }

        @Override
        public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
            Multimap<Holder<Attribute>, AttributeModifier> multimap = LinkedHashMultimap.create();
            LivingEntity entity = slotContext.entity();
            double d = 0;
            if (entity instanceof Player player && !player.hasEffect(ModItems.TIEJI)) d = Math.min(getEmptySlots(player), 20d) / 40; //当空余20格时，获得最大加成0.5
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

    public static class Yiji extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {
            tooltip.add(Component.literal("CD: 20s"));
            tooltip.add(getTip(BLUE));
            tooltip.add(getTip("2", BLUE));
        }

        @Override public boolean isActiveSkill() {return true;}

        @Override
        public void addScreenTip(Skill skill, List<Component> tips) {
            addPresetTips(skill, tips, 0, 1, 2, 4, 5);
            super.addScreenTip(skill, tips);
        }

        @SkillInfo(trigger = Trigger.ON_HURT, relation = Relation.SELF)
        public int onHurt(LivingEntity user, LivingEntity target, Skill skill, ExData data) {
            if (!user.hasEffect(ModItems.COOLDOWN) && user.getHealth() <= 15) {
                draw(user, 2);
                user.addEffect(new MobEffectInstance(ModItems.COOLDOWN, 20 * 20, 0, false, false, true));
                skill.setTag(2);
                voice(user, this);
            }
            return 0;
        }

        @Override
        public boolean activeSkill(Player user, Skill skill, LivingEntity entity) {
            if (entity instanceof Player target) {
                int i = skill.getTag();
                if (i <= 0) return false;
                skill.setMaxSelect(i);
                openInv(user, user, target, Component.translatable("give_card.title", skill.toHoverableText()), skill.stack, false, false, 2);
                return true;
            }
            return false;
        }

        @Override
        public void onGuiClose(PlayerInvScreenHandler handler, Player player, Skill skill, Player target) {
            int count = handler.getSelectedCount();
            if (count == 0) return;
            voice(player, this);
            Component message = Component.translatable("give_card.tip", player.getDisplayName(), skill.toHoverableText(), target.getDisplayName(), count);
            target.displayClientMessage(message, false);
            player.displayClientMessage(message, false);
            skill.setTag(skill.getTag() - count);
            cardMove(player, handler.toExData(), target);
        }
    }

    public static class Xingshang extends SkillItem {
        @Override
        public void addTip(Skill skill, List<Component> tooltip) {tooltip.add(getTip(BLUE));}

        @SkillInfo(trigger = Trigger.ON_DEATH, relation = Relation.NOT_SELF)
        public int playerDie(LivingEntity user, LivingEntity entity, Skill skill, ExData data) {
            if (entity instanceof Player target && user.distanceTo(target) <= 30) {
                voice(user, this);
                cardMove(target, PlayerEvents.cardsToDrop(target), user);
            }
            return 0;
        }
    }
}
