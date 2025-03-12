package com.amotassic.dabaosword.item.skillcard.skills;

import com.amotassic.dabaosword.api.ICardEvent;
import com.amotassic.dabaosword.item.LetMeCCItem;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.equipment.Equipment;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.api.event.CardEvents.cardDiscard;
import static com.amotassic.dabaosword.api.event.CardEvents.cardMove;
import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("all")
public class Wu {

    public static class Buqu extends SkillItem {
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
                tooltip.add(Component.translatable("dabaosword.shift_tip", Component.keybind("key.sneak")));
            }
        }

        @Override
        public void onHurt(ItemStack stack, LivingEntity entity, DamageSource source, float amount) {
            if (entity instanceof Player player && player.isDeadOrDying()) {
                int c = getTag(stack);
                voice(player, Sounds.BUQU);
                if (new Random().nextFloat() >= (float) c /13) {
                    player.displayClientMessage(Component.translatable("buqu.tip1", c + 1).withStyle(ChatFormatting.GREEN), false);
                    setTag(stack, c + 1);
                    player.setHealth(1);
                } else player.displayClientMessage(Component.translatable("buqu.tip2").withStyle(ChatFormatting.RED), false);
            }
        }
    }

    public static class Fenyin extends SkillItem implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.fenyin.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void postCardUse(LivingEntity user, ItemStack card, LivingEntity target, ItemStack skill) {
            var nbt = getOrCreateNbt(skill);
            int last = nbt.contains("fenyin") ? nbt.getInt("fenyin") : 0;
            int current = isRedCard.test(card) ? 1 : isBlackCard.test(card) ? 2 : 0;
            if (last != 0 && current != 0 && current != last) {draw(user); voice(user, skill);}
            nbt.putInt("fenyin", current);
            setNbt(skill, nbt);
        }
    }

    public static class Gongxin extends SkillItem.ActiveSkillWithTarget {
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
                openInv(user, target, Component.translatable("gongxin.title"), stack, false, false, false, 2);
                setCD(stack, 30);
            }
        }

        @Override
        public void onClickGUISlot(Player player, ItemStack stack, Player target, ItemStack selected, int slotIndex) {
            target.displayClientMessage(Component.translatable("dabaosword.discard", player.getDisplayName(), target.getDisplayName(), selected.getDisplayName()), false);
            cardDiscard(target, selected, 1, false);
            closeGUI(player);
        }
    }

    public static class Guose extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.guose.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            viewAs(slotContext.entity(), stack, 15, isDiamondCard, ModItems.TOO_HAPPY_ITEM);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Kurou extends SkillItem.ActiveSkill {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.kurou.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            if (user.getHealth() + 5 * countCard(user, canSaveDying) > 4.99) {
                draw(user, 2);
                if (!user.isCreative()) {
                    user.invulnerableTime = 0;
                    user.hurt(user.damageSources().genericKill(), 4.99f);
                }
                voice(user, Sounds.KUROU);
            } else {user.displayClientMessage(Component.translatable("item.dabaosword.kurou.tip").withStyle(ChatFormatting.RED), true);}
        }
    }

    public static class Lianying extends SkillItem implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.lianying.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity entity = slotContext.entity();
            if (entity.level() instanceof ServerLevel world) {
                int cd = getCD(stack);
                if (world.getGameTime() % 20 == 0 && cd == 1) { //确保一秒内只触发一次
                    draw(entity, 1);
                    voice(entity, stack);
                }
            }
            super.curioTick(slotContext, stack);
        }

        @Override
        public void postCardUse(LivingEntity user, ItemStack card, LivingEntity target, ItemStack skill) {
            if (countCards(user) == 0) setCD(skill, 5);
        }

        @Override
        public void onCardDiscard(LivingEntity entity, ItemStack card, int count, boolean fromEquip, ItemStack skill) {
            if (entity.isAlive() && !fromEquip && countCards(entity) == 0) setCD(skill, 5);
        }

        @Override
        public void onCardMove(LivingEntity from, ItemStack skill, LivingEntity to, ItemStack card, int count, boolean fromEquip, boolean toEquip) {
            if (!fromEquip && countCards(from) == 0) setCD(skill, 5);
        }
    }

    public static class Liuli extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.liuli.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public Priority getPriority(LivingEntity target, DamageSource source, float amount) {return Priority.NORMAL;}

        @Override
        public boolean cancelDamage(LivingEntity target, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity attacker && target instanceof Player player) {
                if (hasTrinket(SkillCards.LIULI, player) && hasCard(player, isCard) && !player.hasEffect(ModItems.INVULNERABLE)) {
                    ItemStack stack = getCard(player, isCard);
                    LivingEntity nearEntity = LetMeCCItem.getClosestEntity(player, LivingEntity.class, 10, entity -> entity != player && entity != attacker);
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
    }

    public static class Pojun extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 10s"));
            tooltip.add(Component.translatable("item.dabaosword.pojun.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player player) {
            //破军：攻击命中盔甲槽有物品的生物后，会让其所有盔甲掉落，配合古锭刀特效使用，pvp神器
            if (!player.hasEffect(ModItems.COOLDOWN)) {
                for (var armor : target.getArmorSlots()) {
                    if (armor.isEmpty()) continue;
                    if (target instanceof Player pl) {give(pl, armor.copy()); armor.setCount(0);}
                    else {target.spawnAtLocation(armor.copy()); armor.setCount(0);}
                }
                voice(player, Sounds.POJUN);
                int i = target instanceof Player ? 200 : 40;
                player.addEffect(new MobEffectInstance(ModItems.COOLDOWN, i,0, false,false,true));
            }
        }
    }

    public static class Qixi extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.qixi.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            viewAs(slotContext.entity(), stack, 5, isBlackCard, ModItems.DISCARD);
            super.curioTick(slotContext, stack);
        }
    }

    public static class Xiaoji extends SkillItem.ActiveSkill implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.xiaoji.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void onCardDiscard(LivingEntity entity, ItemStack card, int count, boolean fromEquip, ItemStack skill) {
            if (entity.isAlive() && fromEquip) {draw(entity, 2); voice(entity, skill);}
        }

        @Override
        public void onCardMove(LivingEntity from, ItemStack skill, LivingEntity to, ItemStack card, int count, boolean fromEquip, boolean toEquip) {
            if (fromEquip) {draw(from, 2); voice(from, skill);}
        }
    }

    public static class Yingzi extends SkillItem {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.yingzi.tooltip").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public int onDrawPhase(Player player, ItemStack stack) {
            voice(player, stack);
            return 1;
        }
    }

    public static class Zhiheng extends SkillItem.ActiveSkill {
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
            if (z > 0) openInv(user, user, Component.translatable("zhiheng.title"), stack, true, true, false, 2);
            else user.displayClientMessage(Component.translatable("zhiheng.fail").withStyle(ChatFormatting.RED), true);
        }

        @Override
        public void onClickGUISlot(Player player, ItemStack stack, Player target, ItemStack selected, int slotIndex) {
            int z = getTag(stack);
            voice(player, Sounds.ZHIHENG);
            cardDiscard(target, selected, 1, slotIndex < 4);
            if (new Random().nextFloat() < 0.1) {
                draw(player, 2);
                player.displayClientMessage(Component.translatable("zhiheng.extra").withStyle(ChatFormatting.GREEN), true);
            } else draw(player);
            setTag(stack, z - 1);
            if (z - 1 == 0) closeGUI(player);
        }
    }

    public static class Zhijian extends SkillItem.ActiveSkillWithTarget {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.zhijian.tooltip1").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.zhijian.tooltip2").withStyle(ChatFormatting.GREEN));
        }

        @Override
        public void activeSkill(Player user, ItemStack stack, Player target) {
            ItemStack itemStack = user.getMainHandItem();
            if (isEquipment.test(itemStack)) {
                cardMove(user, target, itemStack, 1, false, true);
                Equipment.equipItem(target, itemStack);
                voice(user, Sounds.ZHIJIAN);
                draw(user);
            } else user.displayClientMessage(Component.translatable("zhijian.fail").withStyle(ChatFormatting.RED), true);
        }
    }
}
