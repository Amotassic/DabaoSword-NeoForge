package com.amotassic.dabaosword.item.equipment;

import com.amotassic.dabaosword.api.Card;
import com.amotassic.dabaosword.api.ICardEvent;
import com.amotassic.dabaosword.api.ReachDefend;
import com.amotassic.dabaosword.api.Skill;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.amotassic.dabaosword.api.event.CardEvents.cardDiscard;
import static com.amotassic.dabaosword.api.event.CardEvents.cardUsePre;
import static com.amotassic.dabaosword.item.card.CardItem.addSRTip;
import static com.amotassic.dabaosword.item.skillcard.SkillItem.equipped;
import static com.amotassic.dabaosword.item.skillcard.SkillItem.setEquipped;
import static com.amotassic.dabaosword.util.ModTools.*;
import static com.amotassic.dabaosword.util.ModifyDamage.shan;

public class Equipment extends Item implements ICurioItem, Skill, Card {
    public Equipment() {super(new Properties().stacksTo(1));}

    @Override public Type getType() {return Type.EQUIPMENT;}

    public static class BaguaArmor extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.bagua.tooltip").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public Priority getPriority(LivingEntity target, DamageSource source, float amount) {return Priority.HIGH;}

        @Override
        public boolean cancelDamage(LivingEntity target, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity) {
                if (!target.hasEffect(ModItems.COOLDOWN2) && !target.getTags().contains("juedou")) {
                    if (hasTrinket(ModItems.BAGUA, target) && new Random().nextFloat() < 0.5 && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                        shan(target, true, source, amount);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class BaiyinArmor extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.baiyin.tooltip").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public Tuple<Float, Float> modifyDamage(LivingEntity target, DamageSource source, float amount) {
            if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && source.getEntity() instanceof LivingEntity && hasTrinket(ModItems.BAIYIN, target)) {
                voice(target, Sounds.BAIYIN);
                return new Tuple<>(-0.4f, 0f);
            }
            return null;
        }
    }

    public static class CixiongWeapon extends Equipment implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.cixiong.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.cixiong.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public void postCardUse(LivingEntity user, ItemStack card, LivingEntity target, ItemStack skill) {
            if (isSha.test(card) && target != null && new Random().nextFloat() < 0.5) {
                draw(user); voice(user, skill);
            }
        }
    }

    public static class FangtianWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.fangtian.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.fangtian.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
            super.onEquip(slotContext, prevStack, stack);
            setEquipped(stack, true);
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player player) {
            //方天画戟：打中生物后触发特效，给予CD和持续时间
            int cd = getCD(stack);
            if (cd == 0) {
                setCD(stack, 20);
                voice(player, Sounds.FANGTIAN);
                player.displayClientMessage(Component.translatable("dabaosword.fangtian").withStyle(ChatFormatting.RED), true);
            }
        }
    }

    public static class GuanshiWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.guanshi.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.guanshi.tooltip2").withStyle(ChatFormatting.AQUA));
        }
    }

    public static class GudingWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public Tuple<Float, Float> modifyDamage(LivingEntity target, DamageSource source, float amount) {
            if (source.getDirectEntity() instanceof LivingEntity attacker && hasTrinket(ModItems.GUDING_WEAPON, attacker)) {
                int i = 0;
                for (var s : target.getArmorSlots()) {if (s.isEmpty()) i++;}
                if (i == 4) {
                    voice(attacker, Sounds.GUDING);
                    return new Tuple<>(0f, 5f);
                }
            }
            return null;
        }
    }

    public static class HanbingWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.hanbing.tooltip").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public void postAttack(ItemStack stack, LivingEntity entity, LivingEntity attacker, float amount) {
            voice(attacker, Sounds.HANBING);
            entity.invulnerableTime = 0;
            entity.setTicksFrozen(500);
        }
    }

    public static class LiannuWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.liannu.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.liannu.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player attacker) {
            int i = 2;
            if (hasTrinket(ModItems.CHITU, attacker)) i++;
            if (hasTrinket(SkillCards.MASHU, attacker)) i++;
            if (hasTrinket(ModItems.DILU, target)) i--;
            if (hasTrinket(SkillCards.FEIYING, target)) i--;
            if (attacker.distanceTo(target) <= i) {
                attacker.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 3, 255,false, false, false));
                voice(attacker, stack);
            }
        }
    }

    public static class QilinWeapon extends Equipment implements ReachDefend {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.qilin.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.qilin.tooltip2").withStyle(ChatFormatting.AQUA));
            tooltip.add(Component.translatable("item.dabaosword.qilin.tooltip3").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public int getExtraReach(Player player, ItemStack stack) {return 1;}

        @Override
        public void postDamage(ItemStack stack, LivingEntity target, LivingEntity attacker, float amount) {
            if (getCD(stack) != 0) return;
            ItemStack chitu = trinketItem(ModItems.CHITU, target);
            ItemStack dilu = trinketItem(ModItems.DILU, target);
            List<ItemStack> horse = new ArrayList<>();
            if (!chitu.isEmpty()) horse.add(chitu); if (!dilu.isEmpty()) horse.add(dilu);
            if (horse.isEmpty()) return;
            ItemStack selected = horse.get(new Random().nextInt(horse.size()));
            Component message = Component.translatable("dabaosword.discard", attacker.getDisplayName(), target.getDisplayName(), selected.getDisplayName());
            if (attacker instanceof Player player) player.displayClientMessage(message, false);
            if (target instanceof Player player) player.displayClientMessage(message, false);
            cardDiscard(target, selected, 1, true);
            voice(attacker, stack);
            setCD(stack, 30);
        }
    }

    public static class QinggangWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.qinggang.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.qinggang.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player player) {
            //青釭剑额外伤害
            float extraDamage = Math.min(20, 0.2f * target.getMaxHealth());
            target.hurt(player.damageSources().genericKill(), extraDamage); target.invulnerableTime = 0;
            voice(player, Sounds.QINGGANG);
        }
    }

    public static class QinglongWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.qinglong.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.qinglong.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public void preAttack(ItemStack stack, LivingEntity target, Player player) {
            voice(player, Sounds.QINGLONG);
            player.addEffect(new MobEffectInstance(ModItems.INVULNERABLE,10,0,false,false,false));
            player.teleportTo(target.getX(), target.getY(), target.getZ());
            Vec3 momentum = player.getForward().scale(2);
            target.hurtMarked = true; target.setDeltaMovement(momentum.x(), 0, momentum.z());
        }
    }

    public static class RenwangArmor extends Equipment implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.renwang.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.renwang.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public boolean canHurtByCard(LivingEntity entity, ItemStack skill, ItemStack card) {
            if (isSha.test(card) && isBlackCard.test(card)) {voice(entity, skill); return false;}
            return true;
        }
    }

    public static class RattanArmor extends Equipment implements ICardEvent {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.rattanarmor.tooltip"));
        }

        //实现渡江不沉的效果，代码来自https://github.com/focamacho/RingsOfAscension/中的水上行走戒指
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            super.curioTick(slotContext, stack);
            LivingEntity entity = slotContext.entity();
            if(entity.isShiftKeyDown()) return;
            BlockPos pos = entity.getOnPos();
            boolean water = entity.level().getFluidState(new BlockPos(pos.getX(),
                            (int) (entity.getBoundingBox().min(Direction.Axis.Y)), pos.getZ())).is(Fluids.WATER);
            if(water) {
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x, 0.0D, motion.z);
                entity.fallDistance = 0;
                entity.setOnGround(true);
            }
        }

        @Override
        public Tuple<Float, Float> modifyDamage(LivingEntity target, DamageSource source, float amount) {
            //穿藤甲时，若承受火焰伤害，则 战火燃尽，嘤熊胆！（伤害大于5就只加5）
            if (source.is(DamageTypeTags.IS_FIRE) && hasTrinket(ModItems.RATTAN_ARMOR, target)) {
                voice(target, Sounds.TENGJIA2);
                return new Tuple<>(0f, Math.min(amount, 5f));
            }
            return null;
        }

        @Override
        public boolean canHurtByCard(LivingEntity entity, ItemStack skill, ItemStack card) {
            if (card.is(ModItems.WANJIAN) || card.is(ModItems.NANMAN) || card.is(ModItems.SHA)) {
                voice(entity, Sounds.TENGJIA1); return false;
            } return true;
        }

        @Override
        public Priority getPriority(LivingEntity target, DamageSource source, float amount) {return Priority.HIGH;}

        @Override
        public boolean cancelDamage(LivingEntity target, DamageSource source, float amount) {
            ItemStack stack = trinketItem(ModItems.RATTAN_ARMOR, target);
            //弹射物对藤甲无效
            if (source.is(DamageTypeTags.IS_PROJECTILE) && inrattan(target)) {
                Entity projectile = source.getDirectEntity();
                if (projectile instanceof Arrow) { //即使处于CD中，箭也对藤甲无效
                    projectile.discard();
                    voice(target, Sounds.TENGJIA1);
                    return true;
                }
                if (getCD(stack) == 0) {
                    if (projectile != null) projectile.discard();
                    setCD(stack, 5);
                    target.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 10,0,false,false,false));
                    voice(target, Sounds.TENGJIA1);
                    return true;
                }
            }
            //若攻击者主手没有物品，则无法击穿藤甲
            if (source.getDirectEntity() instanceof LivingEntity s && inrattan(target) && s.getMainHandItem().isEmpty()) {
                if (getCD(stack) == 0) {
                    setCD(stack, 5);
                    target.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 10,0,false,false,false));
                    voice(target, Sounds.TENGJIA1);
                    return true;
                }
            }
            return false;
        }

        private static boolean inrattan(LivingEntity entity) {return hasTrinket(ModItems.RATTAN_ARMOR, entity);}
    }

    public static class ZhangbaWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.zhangba.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.zhangba.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            super.curioTick(slotContext, stack);
            LivingEntity entity = slotContext.entity();
            if (!entity.level().isClientSide && entity instanceof Player player && getCD(stack) == 0) {
                ItemStack off = player.getOffhandItem();
                CompoundTag nbt = getOrCreateNbt(stack);
                boolean one = nbt.contains("has_one");
                if (isCard(off)) {
                    if (one) {
                        nbt.remove("has_one");
                        setCD(stack, 5);
                        give(player, new ItemStack(ModItems.SHA));
                        voice(player, Sounds.ZHANGBA);
                    } else {nbt.putBoolean("has_one", true);}
                    setNbt(stack, nbt);
                    off.shrink(1);
                }
            }
        }
    }

    public static class ZhuqueWeapon extends Equipment {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.zhuque.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.zhuque.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        @Override
        public void postDamage(ItemStack stack, LivingEntity target, LivingEntity attacker, float amount) {
            voice(attacker, stack);
            target.setRemainingFireTicks(80);
        }
    }

    public static class AttackHorse extends Equipment implements ReachDefend {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.chitu.tooltip"));
        }

        @Override
        public int getExtraReach(Player player, ItemStack stack) {return 1;}
    }

    public static class DefendHorse extends Equipment implements ReachDefend {
        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
            tooltip.add(Component.translatable("item.dabaosword.dilu.tooltip"));
        }

        @Override
        public int getDefend(Player player, ItemStack stack) {return 1;}
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        addSRTip(stack, tooltip);

        if(Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("equipment.tip1").withStyle(ChatFormatting.BOLD));
            tooltip.add(Component.translatable("equipment.tip2").withStyle(ChatFormatting.BOLD));
        } else tooltip.add(Component.translatable("dabaosword.shift_tip", Component.keybind("key.sneak")));
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world && !equipped(stack)) {
            world.players().forEach(player -> player.displayClientMessage(
                    Component.translatable("dabaosword.entity.equip", slotContext.entity().getDisplayName(), stack.getDisplayName()), false
            ));
            setEquipped(stack, true);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world) {
            int cd = getCD(stack); //世界时间除以20取余为0时，技能内置CD减一秒
            if (cd > 0 && world.getGameTime() % 20 == 0) setCD(stack, cd - 1);
        }
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity instanceof Player player && !player.isCreative()) return false;
        return ICurioItem.super.canUnequip(slotContext, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide && usedHand == InteractionHand.MAIN_HAND) {
            if (cardUsePre(player, player.getMainHandItem(), player)) return InteractionResultHolder.success(player.getMainHandItem());
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {
        equipItem(user, stack);
    }

    public static void equipItem(LivingEntity entity, ItemStack stack) {
        var optional = CuriosApi.getCuriosInventory(entity);
        if (optional.isPresent()) {
            Map<String, ICurioStacksHandler> curios = optional.get().getCurios();
            Tuple<IDynamicStackHandler, SlotContext> firstSlot = null;

            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                IDynamicStackHandler stackHandler = entry.getValue().getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    String id = entry.getKey();
                    NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                    SlotContext slotContext = new SlotContext(id, entity, i, false, renderStates.size() > i && renderStates.get(i));

                    if (stackHandler.isItemValid(i, stack)) {
                        ItemStack present = stackHandler.getStackInSlot(i);

                        if (present.isEmpty()) {
                            stackHandler.setStackInSlot(i, stack.copy());
                            return;
                        } else if (firstSlot == null) firstSlot = new Tuple<>(stackHandler, slotContext);
                    }
                }
            }

            if (firstSlot != null) {
                IDynamicStackHandler stackHandler = firstSlot.getA();
                SlotContext slotContext = firstSlot.getB();
                int i = slotContext.index();
                ItemStack present = stackHandler.getStackInSlot(i);
                cardDiscard(entity, present, present.getCount(), true);
                stackHandler.setStackInSlot(i, stack.copy());
            }
        }
    }
}
