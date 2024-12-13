package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.CardPileInventory;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.util.Gamerule;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import static com.amotassic.dabaosword.api.event.CardEvents.cardDiscard;
import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvents {
    @SubscribeEvent
    public static void PlayerLogIn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getTags().contains("given_skill")) {
            SkillItem.changeSkill(player);
            player.addTag("given_skill");
        }
    }

    @SubscribeEvent
    public static void PlayerDie(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && player.level() instanceof ServerLevel world) {

            boolean card = world.getGameRules().getBoolean(Gamerule.CLEAR_CARDS_AFTER_DEATH);
            if (card) {
                CardPileInventory inventory = new CardPileInventory(player); //移除牌堆背包的牌
                for (var stack : inventory.cards) {cardDiscard(player, stack, stack.getCount(), false);}

                Inventory inv = player.getInventory();
                for (int i = 0; i < inv.getContainerSize(); ++i) { //移除玩家物品栏的牌
                    ItemStack stack = inv.getItem(i);
                    if (isCard(stack)) cardDiscard(player, stack, stack.getCount(), false);
                }

                for(var stack : allTrinkets(player)) { //移除玩家装备区的牌
                    if(isCard(stack)) cardDiscard(player, stack, stack.getCount(), true);
                }
            }

            if (hasItem(player, p(ModItems.BBJI))) voice(player, Sounds.XUYOU);

            if (hasTrinket(SkillCards.TAOLUAN, player)) {
                ItemStack stack = trinketItem(SkillCards.TAOLUAN, player);
                CompoundTag nbt = getOrCreateNbt(stack);
                nbt.remove("used"); setNbt(stack, nbt);
            }

            if (hasTrinket(SkillCards.BUQU, player)) {
                ItemStack stack = trinketItem(SkillCards.BUQU, player);
                int c = getTag(stack);
                if (c > 1) setTag(stack, (c+1)/2);
            }

            if (hasTrinket(SkillCards.LIANYING, player)) setCD(trinketItem(SkillCards.LIANYING, player), 0);
        }
    }

    @SubscribeEvent
    public static void PlayerRespawn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel world) {

            boolean card = world.getGameRules().getBoolean(Gamerule.CLEAR_CARDS_AFTER_DEATH);
            if (card && hasTrinket(ModItems.CARD_PILE, player)) {
                give(player, new ItemStack(ModItems.SHA));
                give(player, new ItemStack(ModItems.SHAN));
                give(player, new ItemStack(ModItems.PEACH));
                draw(player);
            }

        }
    }
}
