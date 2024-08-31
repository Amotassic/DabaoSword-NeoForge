package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.event.listener.CardDiscardListener;
import com.amotassic.dabaosword.event.listener.CardMoveListener;
import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.card.GainCardItem;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CardEvents {

    @SubscribeEvent
    public static void cardUsePost(CardUsePostListener event) {
        Player user = event.getEntity();
        ItemStack stack = event.getStack();
        LivingEntity target = event.getTarget();
        if (user.level() instanceof ServerLevel) {

            if (stack.getItem() == ModItems.WUXIE.get()) stack.shrink(1); //即使创造模式，无懈可击也会消耗，为什么呢？我也不知道
            else if (!user.isCreative()) stack.shrink(1);

            //集智技能触发
            if (hasTrinket(SkillCards.JIZHI.get(), user) && stack.is(Tags.ARMOURY_CARD)) {
                GainCardItem.draw(user, 1);
                if (new Random().nextFloat() < 0.5) {voice(user, Sounds.JIZHI1.get());} else {voice(user, Sounds.JIZHI2.get());}
            }

            //奔袭技能触发
            if (hasTrinket(SkillCards.BENXI.get(), user)) {
                ItemStack trinketItem = trinketItem(SkillCards.BENXI.get(), user);
                int benxi = getTag(trinketItem);
                if (benxi < 5) {
                    benxi ++; setTag(trinketItem, benxi);
                    if (new Random().nextFloat() < 0.5) {voice(user, Sounds.BENXI1.get());} else {voice(user, Sounds.BENXI2.get());}
                }
            }

        }
    }

    @SubscribeEvent
    public static void cardDiscard(CardDiscardListener event) {
        Player player = event.getEntity();
        ItemStack stack = event.getStack();
        int count = event.getCount();
        boolean fromEquip = event.isFromEquip();
        if (player.level() instanceof ServerLevel) {
            //移除被弃置的牌
            stack.shrink(count);

        }
    }

    @SubscribeEvent
    public static void cardMove(CardMoveListener event) {
        LivingEntity from = event.getFrom();
        Player to = event.getEntity();
        ItemStack stack = event.getStack();
        int count = event.getCount();
        CardMoveListener.Type type = event.getType();
        if (from.level() instanceof ServerLevel) {
            //如果是移动到物品栏的类型，则减少from的物品，给to等量的物品（移动到装备区有专门的方法）
            if (type == CardMoveListener.Type.INV_TO_INV || type == CardMoveListener.Type.EQUIP_TO_INV) {
                give(to, stack.copyWithCount(count));
                stack.shrink(count);
            }

        }
    }
}
