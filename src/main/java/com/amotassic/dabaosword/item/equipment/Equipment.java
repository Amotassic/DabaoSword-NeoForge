package com.amotassic.dabaosword.item.equipment;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Skill;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.amotassic.dabaosword.item.skillcard.SkillItem.equipped;
import static com.amotassic.dabaosword.item.skillcard.SkillItem.setEquipped;
import static com.amotassic.dabaosword.util.ModTools.*;
import static com.amotassic.dabaosword.util.ModifyDamage.shan;

public class Equipment extends Item implements ICurioItem, Skill {
    public Equipment(Properties p_41383_) {super(p_41383_);}

    public static class BaguaArmor extends Equipment {
        public BaguaArmor(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.bagua.tooltip"));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
        }

        @Override
        public Priority getPriority(LivingEntity target, DamageSource source, float amount) {return Priority.HIGH;}

        @Override
        public boolean cancelDamage(LivingEntity target, DamageSource source, float amount) {
            if (source.getEntity() instanceof LivingEntity) {
                if (!target.hasEffect(ModItems.COOLDOWN2) && !target.getTags().contains("juedou")) {
                    if (hasTrinket(ModItems.BAGUA, target) && new Random().nextFloat() < 0.5 && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                        shan(target, true, source);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class BaiyinArmor extends Equipment {
        public BaiyinArmor(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.baiyin.tooltip"));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
        }

        @Override
        public Tuple<Float, Float> modifyDamage(LivingEntity target, DamageSource source, float amount) {
            if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && source.getEntity() instanceof LivingEntity && hasTrinket(ModItems.BAIYIN, target)) {
                voice(target, Sounds.BAIYIN);
                return new Tuple<>(-0.4f, 0f);
            }
            return super.modifyDamage(target, source, amount);
        }
    }

    public static class FangtianWeapon extends Equipment {
        public FangtianWeapon(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.fangtian.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.fangtian.tooltip2").withStyle(ChatFormatting.AQUA));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
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

    public static class GudingWeapon extends Equipment {
        public GudingWeapon(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip2").withStyle(ChatFormatting.AQUA));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
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
            return super.modifyDamage(target, source, amount);
        }
    }

    public static class HanbingWeapon extends Equipment {
        public HanbingWeapon(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.hanbing.tooltip").withStyle(ChatFormatting.AQUA));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
        }

        @Override
        public void postAttack(ItemStack stack, LivingEntity entity, LivingEntity attacker, float amount) {
            voice(attacker, Sounds.HANBING);
            entity.invulnerableTime = 0;
            entity.setTicksFrozen(500);
        }
    }

    public static class QinggangWeapon extends Equipment {
        public QinggangWeapon(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.qinggang.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.qinggang.tooltip2").withStyle(ChatFormatting.AQUA));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
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
        public QinglongWeapon(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.qinglong.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.qinglong.tooltip2").withStyle(ChatFormatting.AQUA));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
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

    public static class RattanArmor extends Equipment {
        public RattanArmor(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.rattanarmor.tooltip"));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
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
            return super.modifyDamage(target, source, amount);
        }

        @Override
        public Priority getPriority(LivingEntity target, DamageSource source, float amount) {return Priority.HIGH;}

        @Override
        public boolean cancelDamage(LivingEntity target, DamageSource source, float amount) {
            //弹射物对藤甲无效
            if (source.is(DamageTypeTags.IS_PROJECTILE) && inrattan(target)) {
                voice(target, Sounds.TENGJIA1);
                if (source.getDirectEntity() != null) source.getDirectEntity().discard();
                return true;
            }
            //若攻击者主手没有物品，则无法击穿藤甲
            if (source.getDirectEntity() instanceof LivingEntity s && inrattan(target) && s.getMainHandItem().isEmpty()) {
                ItemStack stack = trinketItem(ModItems.RATTAN_ARMOR, target);
                if (getCD(stack) == 0) {
                    setCD(stack, 3);
                    voice(target, Sounds.TENGJIA1);
                    return true;
                }
            }
            return false;
        }

        private static boolean inrattan(LivingEntity entity) {return hasTrinket(ModItems.RATTAN_ARMOR, entity);}
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {

        if (stack.getItem() == ModItems.CHITU) {
            tooltip.add(Component.translatable("item.dabaosword.chitu.tooltip"));
        }

        if (stack.getItem() == ModItems.DILU) {
            tooltip.add(Component.translatable("item.dabaosword.dilu.tooltip"));
        }

        if (stack.getItem() == ModItems.CARD_PILE) {
            tooltip.add(Component.translatable("item.dabaosword.card_pile.tooltip"));
        }

        if (stack.getItem() != ModItems.CARD_PILE) {
            if(Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("equipment.tip1").withStyle(ChatFormatting.BOLD));
                tooltip.add(Component.translatable("equipment.tip2").withStyle(ChatFormatting.BOLD));
            } else tooltip.add(Component.translatable("dabaosword.shifttooltip"));
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
        if (entity instanceof Player player && player.isCreative()) return true;
        if (stack.getItem() != ModItems.CARD_PILE) return false;
        return ICurioItem.super.canUnequip(slotContext, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (equipItem(player, stack)) return InteractionResultHolder.success(stack);
        return super.use(level, player, usedHand);
    }

    public static boolean equipItem(Player player, ItemStack stack) {
        var optional = CuriosApi.getCuriosInventory(player);
        if (optional.isPresent()) {
            Map<String, ICurioStacksHandler> curios = optional.get().getCurios();
            Tuple<IDynamicStackHandler, SlotContext> firstSlot = null;

            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                IDynamicStackHandler stackHandler = entry.getValue().getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    String id = entry.getKey();
                    NonNullList<Boolean> renderStates = entry.getValue().getRenders();
                    SlotContext slotContext = new SlotContext(id, player, i, false, renderStates.size() > i && renderStates.get(i));

                    if (stackHandler.isItemValid(i, stack)) {
                        ItemStack present = stackHandler.getStackInSlot(i);

                        if (present.isEmpty()) {
                            stackHandler.setStackInSlot(i, stack.copy());
                            cardUsePost(player, stack, player);
                            return true;
                        } else if (firstSlot == null) firstSlot = new Tuple<>(stackHandler, slotContext);
                    }
                }
            }

            if (firstSlot != null) {
                IDynamicStackHandler stackHandler = firstSlot.getA();
                SlotContext slotContext = firstSlot.getB();
                int i = slotContext.index();
                ItemStack present = stackHandler.getStackInSlot(i);
                cardDiscard(player, present, present.getCount(), true);
                stackHandler.setStackInSlot(i, stack.copy());
                cardUsePost(player, stack, player);
                return true;
            }
        }
        return false;
    }
}
