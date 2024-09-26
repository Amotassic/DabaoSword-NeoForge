package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.event.listener.CardDiscardListener;
import com.amotassic.dabaosword.event.listener.CardMoveListener;
import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CardEvents {

    @SubscribeEvent
    public static void cardUsePost(CardUsePostListener event) {
        Player user = event.getEntity(); LivingEntity target = event.getTarget();
        ItemStack stack = event.getStack(); ItemStack copy = event.getCopy();

        if (stack.getItem() == ModItems.WUXIE.get()) stack.shrink(1); //即使创造模式，无懈可击也会消耗，为什么呢？我也不知道
        else if (!user.isCreative()) stack.shrink(1);

        //集智技能触发
        if (hasTrinket(SkillCards.JIZHI, user) && copy.is(Tags.ARMOURY_CARD)) {
            draw(user);
            voice(user, Sounds.JIZHI);
        }

        //奔袭技能触发
        if (hasTrinket(SkillCards.BENXI, user)) {
            ItemStack trinketItem = trinketItem(SkillCards.BENXI, user);
            int benxi = getTag(trinketItem);
            if (benxi < 5) {
                benxi ++; setTag(trinketItem, benxi);
                voice(user, Sounds.BENXI);
            }
        }

        if (hasTrinket(SkillCards.LIANYING, user) && countCards(user) == 0) lianyingTrigger(user);
    }

    @SubscribeEvent
    public static void cardDiscard(CardDiscardListener event) {
        Player player = event.getEntity();
        ItemStack stack = event.getStack(); ItemStack copy = event.getCopy();
        int count = event.getCount(); boolean fromEquip = event.isFromEquip();

        //移除被弃置的牌
        stack.shrink(count);

        //弃置牌后，玩家的死亡判断是有必要的
        if (player.isAlive()) {
            if (hasTrinket(SkillCards.LIANYING, player) && !fromEquip && countCards(player) == 0) lianyingTrigger(player);

            if (hasTrinket(SkillCards.XIAOJI, player) && fromEquip) xiaojiTrigger(player);
        }
    }

    @SubscribeEvent
    public static void cardMove(CardMoveListener event) {
        LivingEntity from = event.getFrom(); Player to = event.getEntity();
        ItemStack stack = event.getStack(); ItemStack copy = event.getCopy();
        int count = event.getCount(); CardMoveListener.Type type = event.getType();

        //如果是移动到物品栏的类型，则减少from的物品，给to等量的物品（移动到装备区有专门的方法）
        if (type == CardMoveListener.Type.INV_TO_INV || type == CardMoveListener.Type.EQUIP_TO_INV) {
            give(to, copy);
            stack.shrink(count);
        }

        if (type == CardMoveListener.Type.INV_TO_EQUIP || type == CardMoveListener.Type.INV_TO_INV) {
            if (from instanceof Player player && hasTrinket(SkillCards.LIANYING, player) && countCards(player) == 0) lianyingTrigger(player);
        }

        if (type == CardMoveListener.Type.EQUIP_TO_INV || type == CardMoveListener.Type.EQUIP_TO_EQUIP) {
            if (from instanceof Player player && hasTrinket(SkillCards.XIAOJI, player)) xiaojiTrigger(player);
        }
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
