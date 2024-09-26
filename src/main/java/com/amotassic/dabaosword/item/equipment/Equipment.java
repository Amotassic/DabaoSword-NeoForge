package com.amotassic.dabaosword.item.equipment;

import com.amotassic.dabaosword.event.listener.CardDiscardListener;
import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
import com.amotassic.dabaosword.util.Skill;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Map;

import static com.amotassic.dabaosword.util.ModTools.*;

public class Equipment extends Item implements ICurioItem, Skill {
    public Equipment(Properties p_41383_) {super(p_41383_);}

    public static class FangtianWeapon extends Equipment {
        public FangtianWeapon(Properties p_41383_) {super(p_41383_);}

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
            tooltip.add(Component.translatable("item.dabaosword.fangtian.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.fangtian.tooltip2").withStyle(ChatFormatting.AQUA));
            super.appendHoverText(stack, context, tooltip, tooltipFlag);
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().level() instanceof ServerLevel world) {
                int cd = ModTools.getCD(stack);
                if (cd > 0 && world.getGameTime() % 20 == 0) cd--; stack.set(ModItems.CD, cd);
            }
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
            LivingEntity entity = slotContext.entity();
            if(entity.isShiftKeyDown()) return;

            BlockPos entityPos = entity.getOnPos();

            boolean water = entity.level().getFluidState(new BlockPos(entityPos.getX(),
                            (int) (entity.getBoundingBox().min(Direction.Axis.Y)), entityPos.getZ()))
                    .is(Fluids.WATER);

            if(water) {
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x, 0.0D, motion.z);
                entity.fallDistance = 0;
                entity.setOnGround(true);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {

        if (stack.getItem() == ModItems.GUDING_WEAPON.get()) {
            tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip2").withStyle(ChatFormatting.AQUA));
        }

        if (stack.getItem() == ModItems.BAGUA.get()) {
            tooltip.add(Component.translatable("item.dabaosword.bagua.tooltip"));
        }

        if (stack.getItem() == ModItems.BAIYIN.get()) {
            tooltip.add(Component.translatable("item.dabaosword.baiyin.tooltip"));
        }

        if (stack.getItem() == ModItems.CHITU.get()) {
            tooltip.add(Component.translatable("item.dabaosword.chitu.tooltip"));
        }

        if (stack.getItem() == ModItems.DILU.get()) {
            tooltip.add(Component.translatable("item.dabaosword.dilu.tooltip"));
        }

        if (stack.getItem() == ModItems.CARD_PILE.get()) {
            tooltip.add(Component.translatable("item.dabaosword.card_pile.tooltip"));
        }

        if (stack.getItem() != ModItems.CARD_PILE.get()) {
            if(Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("equipment.tip1").withStyle(ChatFormatting.BOLD));
                tooltip.add(Component.translatable("equipment.tip2").withStyle(ChatFormatting.BOLD));
            } else tooltip.add(Component.translatable("dabaosword.shifttooltip"));
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world) {
            world.players().forEach(player -> player.displayClientMessage(
                    Component.literal(slotContext.entity().getScoreboardName()).append(Component.literal(" equipped ").append(stack.getDisplayName())),false
            ));
        }
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity instanceof Player player && player.isCreative()) return true;
        if (stack.getItem() != ModItems.CARD_PILE.get()) return false;
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
                            NeoForge.EVENT_BUS.post(new CardUsePostListener(player, stack, player));
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
                NeoForge.EVENT_BUS.post(new CardDiscardListener(player, present, present.getCount(), true));
                stackHandler.setStackInSlot(i, stack.copy());
                NeoForge.EVENT_BUS.post(new CardUsePostListener(player, stack, player));
                return true;
            }
        }
        return false;
    }
}
