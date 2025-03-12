package com.amotassic.dabaosword.event;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.pvpgame.Game;
import com.amotassic.dabaosword.util.Gamerule;
import com.amotassic.dabaosword.util.MODConfig;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.amotassic.dabaosword.api.event.CardEvents.cardDiscard;
import static com.amotassic.dabaosword.event.PVPGameEvents.getGameManager;
import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvents {
    private static final Map<UUID, KillStreakData> playerKillData = new HashMap<>();
    private record KillStreakData(int streak, long lastKillTime) {}
    private static SoundEvent getKillSound(int streak) {
        return switch (streak) {
            case 1, 2, 3, 4, 5, 6, 7 -> getSound("kill" + streak);
            default -> getSound("diankuang");
        };
    }

    @SubscribeEvent
    public static void PlayerDie(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (event.getEntity() instanceof Player player && player.level() instanceof ServerLevel world) {
            Entity attacker = source.getEntity();
            if (!(attacker instanceof Player)) attacker = player.getKillCredit();

            if (MODConfig.KillStreak && attacker instanceof ServerPlayer killer) { //紫砂也算连上了
                UUID id = killer.getUUID(); long time = world.getGameTime();

                var data = playerKillData.getOrDefault(id, new KillStreakData(0, 0));
                long timeDiff = time - data.lastKillTime();
                int newStreak = (0 <= timeDiff && timeDiff <= 1200) ? data.streak() + 1 : 1;
                data = new KillStreakData(newStreak, time);
                playerKillData.put(id, data);

                if (data.streak() >= 2) voice(killer, getKillSound(data.streak()));
            }

            if (attacker instanceof ServerPlayer killer && player instanceof ServerPlayer dead && killer != dead) {
                Game game = getGameManager().getGameByPlayer(killer);
                if (game != null && game.isOn() && game.isPlayerInThisGame(dead)) {
                    var primaryData = game.getPrimaryData();
                    if (MODConfig.KillStreak && game.zhongLives + game.fanLives + game.neiLives == primaryData.get(Game.ZHONGLIVES) + primaryData.get(Game.FANLIVES) + primaryData.get(Game.NEILIVES)) voice(killer, getKillSound(1));

                    var killerTeam = game.getIdentity(killer); var deadTeam = game.getIdentity(dead);
                    if (deadTeam != Game.Identity.NEI && killerTeam != deadTeam) game.increaseScore(killer);
                }
            }

            //玩家死亡时，若处于对战中，减少该玩家所在队伍的剩余生命数
            Game game = getGameManager().getGameByPlayer(player);
            if (game != null && game.isOn() && player instanceof ServerPlayer sp) {
                game.decreaseLives(sp);
                var identity = game.getIdentity(sp);
                int re = game.getRespawnChances(identity);
                if (re <= 0) { //如果玩家所在阵营剩余生命值为0，公布玩家身份
                    sp.setGameMode(GameType.SPECTATOR);
                    game.forEachPlayer(p -> p.displayClientMessage(Component.translatable("dabaosword.game.view_id.tip", player.getDisplayName(), Component.translatable(identity.tag)).withStyle(Game.getIdentityColor(identity), ChatFormatting.BOLD), false));
                }
            }

            boolean card = world.getGameRules().getBoolean(Gamerule.CLEAR_CARDS_AFTER_DEATH);
            if (card) {
                var inventory = getCardPack(player); //移除牌堆背包的牌
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
    public static void PlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel world) {

            boolean card = world.getGameRules().getBoolean(Gamerule.CLEAR_CARDS_AFTER_DEATH);
            if (card && hasTrinket(ModItems.CARD_PILE, player)) {
                give(player, newCard(ModItems.SHA));
                give(player, newCard(ModItems.SHAN));
                give(player, newCard(ModItems.PEACH));
                draw(player);
            }

        }
    }
}
