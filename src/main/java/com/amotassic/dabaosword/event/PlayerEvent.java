package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Gamerule;
import com.amotassic.dabaosword.util.Sounds;
import com.amotassic.dabaosword.util.Tags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Random;

import static com.amotassic.dabaosword.item.skillcard.SkillItem.changeSkill;
import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvent {

    @SubscribeEvent
    public static void PlayerDie(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && player.level() instanceof ServerLevel world) {

            boolean card = world.getGameRules().getBoolean(Gamerule.CLEAR_CARDS_AFTER_DEATH);
            if (card) {
                Inventory inv = player.getInventory();
                for (int i = 0; i < inv.getContainerSize(); ++i) {
                    ItemStack stack = inv.getItem(i);
                    if (stack.is(Tags.CARD) || stack.getItem() == ModItems.GAIN_CARD.get()) {
                        for (Player player1 : world.players()) {//行殇技能触发
                            if (hasTrinket(SkillCards.XINGSHANG.get(), player1) && player1.distanceTo(player) <= 25 && player1 != player) {
                                if (!player1.getTags().contains("xingshang")) {
                                    if (new Random().nextFloat() < 0.5) voice(player1, Sounds.XINGSHANG1.get()); else voice(player1, Sounds.XINGSHANG2.get());
                                }
                                player1.addTag("xingshang");
                                give(player1, stack); break;
                            }
                        }
                        inv.removeItemNoUpdate(i);
                    }
                }

                var component = CuriosApi.getCuriosInventory(player);
                if(component.isPresent()) {
                    var allEquipped = component.get().getEquippedCurios();
                    for(int i = 0; i < allEquipped.getSlots(); i++) {
                        ItemStack stack = allEquipped.getStackInSlot(i);
                        if(stack.is(Tags.CARD)) {
                            for (Player player1 : world.players()) {//行殇技能触发
                                if (hasTrinket(SkillCards.XINGSHANG.get(), player1) && player1.distanceTo(player) <= 25 && player1 != player) {
                                    if (!player1.getTags().contains("xingshang")) {
                                        if (new Random().nextFloat() < 0.5) voice(player1, Sounds.XINGSHANG1.get()); else voice(player1, Sounds.XINGSHANG2.get());
                                    }
                                    player1.addTag("xingshang");
                                    give(player1, stack); break;
                                }
                            }
                            stack.setCount(0);
                        }
                    }
                }
            }

            if (hasItem(player, ModItems.BBJI.get())) voice(player, Sounds.XUYOU.get());

            if (hasTrinket(SkillCards.BUQU.get(), player)) {
                ItemStack stack = trinketItem(SkillCards.BUQU.get(), player);
                int c = getTag(stack);
                if (c > 1) setTag(stack, (c+1)/2);
            }
        }
    }

    @SubscribeEvent
    public static void PlayerRespawn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel world) {

            boolean card = world.getGameRules().getBoolean(Gamerule.CLEAR_CARDS_AFTER_DEATH);
            if (card) {
                give(player, new ItemStack(ModItems.SHA.get()));
                give(player, new ItemStack(ModItems.SHAN.get()));
                give(player, new ItemStack(ModItems.PEACH.get()));
            }

        }
    }

    @SubscribeEvent
    public static void PlayerLogIn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getTags().contains("given_skill")) {
            changeSkill(player);
            player.addTag("given_skill");
        }
    }
}
