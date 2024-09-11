package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.card.GainCardItem;
import com.amotassic.dabaosword.util.LootEntry;
import com.amotassic.dabaosword.util.LootTableParser;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Objects;

import static com.amotassic.dabaosword.util.ModTools.*;

public class SkillItem extends Item implements ICurioItem {
    public SkillItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {

        if (stack.getItem() == SkillCards.SHENSU.get()) {
            tooltip.add(Component.translatable("item.dabaosword.shensu.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.shensu.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.XIAOJI.get()) {
            tooltip.add(Component.translatable("item.dabaosword.xiaoji.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.LIANYING.get()) {
            tooltip.add(Component.translatable("item.dabaosword.lianying.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.LONGDAN.get()) {
            tooltip.add(Component.translatable("item.dabaosword.longdan.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.longdan.tooltip2").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.SHANZHUAN.get()) {
            tooltip.add(Component.literal("CD: 8s"));
            tooltip.add(Component.translatable("item.dabaosword.shanzhuan.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.shanzhuan.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.ZHIJIAN.get()) {
            tooltip.add(Component.translatable("item.dabaosword.zhijian.tooltip1").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.zhijian.tooltip2").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.GONGXIN.get()) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 30s" : "CD: 30s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.gongxin.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.RENDE.get()) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 30s" : "CD: 30s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.rende.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.rende.tooltip2").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.ZHIHENG.get()) {
            int z = getTag(stack);
            tooltip.add(Component.literal("uses: " + z));
            tooltip.add(Component.translatable("item.dabaosword.zhiheng.tooltip1").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.dabaosword.zhiheng.tooltip2").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.BUQU.get()) {
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

        if (stack.getItem() == SkillCards.TIEJI.get()) {
            tooltip.add(Component.translatable("item.dabaosword.tieji.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.tieji.tooltip2").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.GANGLIE.get()) {
            tooltip.add(Component.translatable("item.dabaosword.ganglie.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.ganglie.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.FANGZHU.get()) {
            tooltip.add(Component.translatable("item.dabaosword.fangzhu.tooltip").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.XINGSHANG.get()) {
            tooltip.add(Component.translatable("item.dabaosword.xingshang.tooltip").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.DUANLIANG.get()) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.duanliang.tooltip").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.LUOSHEN.get()) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 30s" : "CD: 30s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.luoshen.tooltip").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.QINGGUO.get()) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.qingguo.tooltip").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.QIXI.get()) {
            tooltip.add(Component.literal("CD: 5s"));
            tooltip.add(Component.translatable("item.dabaosword.qixi.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.HUOJI.get()) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.huoji.tooltip").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.LUANJI.get()) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.luanji.tooltip"));
        }

        if (stack.getItem() == SkillCards.QICE.get()) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 20s" : "CD: 20s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.qice.tooltip").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.KANPO.get()) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 10s" : "CD: 10s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.kanpo.tooltip").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.GUOSE.get()) {
            int cd = getCD(stack);
            tooltip.add(Component.literal(cd == 0 ? "CD: 15s" : "CD: 15s   left: "+ cd +"s"));
            tooltip.add(Component.translatable("item.dabaosword.guose.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.BENXI.get()) {
            int benxi = getTag(stack);
            tooltip.add(Component.literal("tag: " + benxi));
            tooltip.add(Component.translatable("item.dabaosword.benxi.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.benxi.tooltip2").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.QUANJI.get()) {
            int quan = getTag(stack);
            tooltip.add(Component.literal("quan: "+quan));
            tooltip.add(Component.translatable("item.dabaosword.quanji.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.quanji.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.GONGAO.get()) {
            tooltip.add(Component.translatable("item.dabaosword.gongao.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.gongao.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.LIEGONG.get()) {
            tooltip.add(Component.translatable("item.dabaosword.liegong.tooltip1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.dabaosword.liegong.tooltip2").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.LEIJI.get()) {
            tooltip.add(Component.translatable("item.dabaosword.leiji.tooltip"));
        }

        if (stack.getItem() == SkillCards.YIJI.get()) {
            tooltip.add(Component.literal("CD: 20s"));
            tooltip.add(Component.translatable("item.dabaosword.yiji.tooltip").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.yiji.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.JIZHI.get()) {
            tooltip.add(Component.translatable("item.dabaosword.jizhi.tooltip").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.KUROU.get()) {
            tooltip.add(Component.translatable("item.dabaosword.kurou.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.LUOYI.get()) {
            tooltip.add(Component.translatable("item.dabaosword.luoyi.tooltip").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.TAOLUAN.get()) {
            tooltip.add(Component.translatable("item.dabaosword.taoluan.tooltip"));
        }

        if (stack.getItem() == SkillCards.JUEQING.get()) {
            tooltip.add(Component.translatable("item.dabaosword.jueqing.tooltip1").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("item.dabaosword.jueqing.tooltip2").withStyle(ChatFormatting.BLUE));
        }

        if (stack.getItem() == SkillCards.POJUN.get()) {
            tooltip.add(Component.literal("CD: 10s"));
            tooltip.add(Component.translatable("item.dabaosword.pojun.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.KUANGGU.get()) {
            tooltip.add(Component.literal("CD: 8s"));
            tooltip.add(Component.translatable("item.dabaosword.kuanggu.tooltip").withStyle(ChatFormatting.RED));
        }

        if (stack.getItem() == SkillCards.LIULI.get()) {
            tooltip.add(Component.translatable("item.dabaosword.liuli.tooltip").withStyle(ChatFormatting.GREEN));
        }

        if (stack.getItem() == SkillCards.MASHU.get()) {
            tooltip.add(Component.translatable("item.dabaosword.chitu.tooltip"));
        }

        if (stack.getItem() == SkillCards.FEIYING.get()) {
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
            if (stack.get(ModItems.CD) != null) {
                int cd = Objects.requireNonNull(stack.get(ModItems.CD));
                if (world.getGameTime() % 20 == 0) {//世界时间除以20取余为0时，技能内置CD减一秒
                    if (cd > 0) {cd--; stack.set(ModItems.CD, cd);}
                }
            }
        }
        ICurioItem.super.curioTick(slotContext, stack);
    }

    public static void changeSkill(Player player) {
        List<LootEntry> lootEntries = LootTableParser.parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/change_skill.json"));
        LootEntry selectedEntry = GainCardItem.selectRandomEntry(lootEntries);

        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(selectedEntry.item()));
        if (stack.getItem() != Items.AIR) voice(player, Sounds.GIFTBOX.get(),3);
        give(player, stack);
    }

    public static class ActiveSkill extends SkillItem {
        public ActiveSkill(Properties p_41383_) {super(p_41383_);}
    }

    public static class ActiveSkillWithTarget extends SkillItem {
        public ActiveSkillWithTarget(Properties p_41383_) {super(p_41383_);}
    }
}
