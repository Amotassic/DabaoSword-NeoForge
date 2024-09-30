package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CardItem extends Item {
    public CardItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (stack.getItem() == ModItems.WUXIE) {
            tooltip.add(Component.translatable("item.dabaosword.wuxie.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.wuxie.tooltip2"));
        }

        if (stack.getItem() == ModItems.ARROW_RAIN || stack.getItem() == ModItems.WANJIAN) {//有大病的工具提示
            if(Screen.hasShiftDown()){
                int i = (int) (System.currentTimeMillis() / 1000) % 7;
                if (i==0) {tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip7").withStyle(ChatFormatting.BLUE));}
                if (i==1) {tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip1").withStyle(ChatFormatting.AQUA));}
                if (i==2) {tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip2").withStyle(ChatFormatting.RED));}
                if (i==3) {tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip3").withStyle(ChatFormatting.GOLD));}
                if (i==4) {tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip4").withStyle(ChatFormatting.GREEN));}
                if (i==5) {tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip5").withStyle(ChatFormatting.DARK_PURPLE));}
                if (i==6) {tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip6").withStyle(ChatFormatting.YELLOW));}
            }else{
                tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip").withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("item.dabaosword.arrowrain.shift").withStyle(ChatFormatting.ITALIC));
            }
        }

        if (stack.getItem() == ModItems.BINGLIANG_ITEM) {
            if(Screen.hasShiftDown()){
                tooltip.add(Component.translatable("item.dabaosword.bingliang.tooltip1"));
                tooltip.add(Component.translatable("item.dabaosword.bingliang.tooltip2"));
            }else{
                tooltip.add(Component.translatable("item.dabaosword.bingliang.tooltip").withStyle(ChatFormatting.BLUE));
                tooltip.add(Component.translatable("dabaosword.shifttooltip"));
            }
        }

        if (stack.getItem() == ModItems.DISCARD) {
            tooltip.add(Component.translatable("item.dabaosword.discard.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.discard.tooltip2"));
            tooltip.add(Component.translatable("item.dabaosword.long_hand").withStyle(ChatFormatting.BOLD));
        }

        if (stack.getItem() == ModItems.FIRE_ATTACK) {
            tooltip.add(Component.translatable("item.dabaosword.huogong.tooltip"));
        }

        if (stack.getItem() == ModItems.JIEDAO) {
            tooltip.add(Component.translatable("item.dabaosword.jiedao.tooltip"));
        }

        if (stack.getItem() == ModItems.JIU) {
            tooltip.add(Component.translatable("item.dabaosword.jiu.tooltip"));
            tooltip.add(Component.translatable("item.dabaosword.recover.tip").withStyle(ChatFormatting.BOLD));
        }

        if (stack.getItem() == ModItems.JUEDOU) {
            tooltip.add(Component.translatable("item.dabaosword.juedou.tooltip"));
            tooltip.add(Component.translatable("item.dabaosword.long_hand").withStyle(ChatFormatting.BOLD));
        }

        if (stack.getItem() == ModItems.NANMAN) {
            tooltip.add(Component.translatable("item.dabaosword.nanman.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.nanman.tooltip2"));
        }

        if (stack.getItem() == ModItems.PEACH) {
            tooltip.add(Component.translatable("item.dabaosword.peach.tooltip1").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("item.dabaosword.peach.tooltip2").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("item.dabaosword.recover.tip").withStyle(ChatFormatting.BOLD));
        }

        if (stack.getItem() == ModItems.SHAN) {
            tooltip.add(Component.translatable("item.dabaosword.sha&shan.tooltip").withStyle(ChatFormatting.BOLD));
            tooltip.add(Component.translatable("item.dabaosword.shan.tooltip"));
        }

        if (stack.getItem() == ModItems.STEAL) {
            tooltip.add(Component.translatable("item.dabaosword.steal.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.steal.tooltip2"));
        }

        if (stack.getItem() == ModItems.TAOYUAN) {
            tooltip.add(Component.translatable("item.dabaosword.taoyuan.tooltip"));
        }

        if (stack.getItem() == ModItems.TIESUO) {
            tooltip.add(Component.translatable("item.dabaosword.tiesuo.tooltip"));
        }

        if (stack.getItem() == ModItems.TOO_HAPPY_ITEM) {
            if(Screen.hasShiftDown()){
                tooltip.add(Component.translatable("item.dabaosword.too_happy.tooltip1"));
                tooltip.add(Component.translatable("item.dabaosword.too_happy.tooltip2"));
            }else{
                tooltip.add(Component.translatable("item.dabaosword.too_happy.tooltip").withStyle(ChatFormatting.RED));
                tooltip.add(Component.translatable("dabaosword.shifttooltip"));
            }
        }

        if (stack.getItem() == ModItems.WUZHONG) {
            tooltip.add(Component.translatable("item.dabaosword.wuzhong.tooltip1"));
            tooltip.add(Component.translatable("item.dabaosword.wuzhong.tooltip2"));
        }

        if (stack.getItem() == ModItems.GAIN_CARD) {
            tooltip.add(Component.translatable("item.dabaosword.gain_card.tooltip"));
        }

        if (stack.getItem() == ModItems.SHA) {
            tooltip.add(Component.translatable("item.dabaosword.sha&shan.tooltip").withStyle(ChatFormatting.BOLD));
            tooltip.add(Component.translatable("item.dabaosword.sha.tooltip"));
        }

        if (stack.getItem() == ModItems.FIRE_SHA) {
            tooltip.add(Component.translatable("item.dabaosword.sha&shan.tooltip").withStyle(ChatFormatting.BOLD));
            tooltip.add(Component.translatable("item.dabaosword.fire_sha.tooltip").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == ModItems.THUNDER_SHA) {
            tooltip.add(Component.translatable("item.dabaosword.sha&shan.tooltip").withStyle(ChatFormatting.BOLD));
            tooltip.add(Component.translatable("item.dabaosword.thunder_sha.tooltip").withStyle(ChatFormatting.BLUE));
        }
    }
}
