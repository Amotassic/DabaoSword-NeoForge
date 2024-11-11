package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.event.CardCBs;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.ModTools;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CardEvents {
    @SubscribeEvent
    public static void cardUsePre(CardCBs.UsePre event) {
        LivingEntity user = event.getEntity(); LivingEntity target = event.target;
        ItemStack stack = event.stack;

        if (target != null) {
            if (isBlackCard.test(stack) && stack.is(Tags.ARMOURY_CARD) && hasTrinket(SkillCards.WEIMU, target)) {
                voice(target, Sounds.WEIMU);
                ModTools.cardUsePost(user, stack, target);
                event.setCanceled(true); return;
            }
            if (stack.is(Tags.TRIGGER_WUXIE) && hasCard(target, s -> s.is(ModItems.WUXIE))) {
                ModTools.cardUsePre(target, new ItemStack(ModItems.WUXIE), null); //递归触发无懈，因此不用再写消耗和执行效果
                ModTools.cardUsePost(user, stack, target);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void cardUsePost(CardCBs.UsePost event) {
        LivingEntity user = event.getEntity(); LivingEntity target = event.target;
        ItemStack stack = event.stack;

        if (user instanceof Player player) {
            //集智技能触发
            if (hasTrinket(SkillCards.JIZHI, player) && stack.is(Tags.ARMOURY_CARD)) {
                draw(player);
                voice(player, Sounds.JIZHI);
            }

            //奔袭技能触发
            if (hasTrinket(SkillCards.BENXI, player)) {
                ItemStack trinketItem = trinketItem(SkillCards.BENXI, player);
                int benxi = getTag(trinketItem);
                if (benxi < 5) {
                    benxi ++; setTag(trinketItem, benxi);
                    voice(player, Sounds.BENXI);
                }
            }

            if (hasTrinket(SkillCards.LIANYING, player) && countCards(player) == 0) lianyingTrigger(player);
        }
    }

    @SubscribeEvent
    public static void cardDiscard(CardCBs.Discard event) {
        LivingEntity entity = event.getEntity();
        ItemStack stack = event.stack;
        boolean fromEquip = event.fromEquip;

        if (XingshangTrigger(entity, stack)) return; //todo 卡牌弃置后并被他人获得后，与其他技能的交互处理

        //弃置牌后，玩家的死亡判断是有必要的
        if (entity instanceof Player player && player.isAlive()) {
            if (hasTrinket(SkillCards.LIANYING, player) && !fromEquip && countCards(player) == 0) lianyingTrigger(player);

            if (hasTrinket(SkillCards.XIAOJI, player) && fromEquip) xiaojiTrigger(player);
        }
    }

    @SubscribeEvent
    public static void cardMove(CardCBs.Move event) {
        LivingEntity from = event.from; Player to = event.getEntity();
        ItemStack stack = event.stack;
        CardCBs.T type = event.type;

        if (type == CardCBs.T.INV_TO_EQUIP || type == CardCBs.T.INV_TO_INV) {
            if (from instanceof Player player && hasTrinket(SkillCards.LIANYING, player) && countCards(player) == 0) lianyingTrigger(player);
        }

        if (type == CardCBs.T.EQUIP_TO_INV || type == CardCBs.T.EQUIP_TO_EQUIP) {
            if (from instanceof Player player && hasTrinket(SkillCards.XIAOJI, player)) xiaojiTrigger(player);
        }
    }

    private static boolean XingshangTrigger(LivingEntity entity, ItemStack stack) {
        if (entity.isAlive()) return false;
        for (Player player : entity.level().players()) {
            if (hasTrinket(SkillCards.XINGSHANG, player) && player.distanceTo(entity) <= 25 && player != entity) {
                if (!player.getTags().contains("xingshang")) voice(player, Sounds.XINGSHANG);
                player.addTag("xingshang"); //防止同时触发大量语音播放
                give(player, stack.copy());
                return true;
            }
        }
        return false;
    }

    private static void lianyingTrigger(Player player) {
        ItemStack stack = trinketItem(SkillCards.LIANYING, player);
        if (stack != null) setCD(stack, 5);
    }

    private static void xiaojiTrigger(Player player) {
        draw(player, 2);
        voice(player, Sounds.XIAOJI);
    }
}
