package com.amotassic.dabaosword.pvpgame;

import com.amotassic.dabaosword.event.PVPGameEvents;
import com.amotassic.dabaosword.util.MODConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GameManager extends SavedData {
    private final Map<Integer, Game> games = new HashMap<>();
    private final ServerLevel world;
    private int nextAvailableId;

    public static SavedData.Factory<GameManager> factory(ServerLevel world) {
        return new SavedData.Factory<>(() -> new GameManager(world), (nbt, r) -> fromNbt(world, nbt), null);
    }

    public GameManager(ServerLevel world) {
        this.world = world;
        this.nextAvailableId = 1;
        setDirty();
    }

    public int getGameCount() {return games.size();}

    @Nullable
    public Game createGame(ServerPlayer player, int type) {
        AABB box = new AABB(player.getOnPos()).inflate(MODConfig.SearchRadius);
        List<Player> players = player.level().getEntitiesOfClass(Player.class, box, p -> !p.isSpectator() && !isPlayerInGame(p));
        if (players.size() < 2) {
            player.displayClientMessage(Component.literal("Not enough players to start a game!").withStyle(ChatFormatting.RED), false);
            return null;
        }
        Set<UUID> playerUuids = new HashSet<>();
        for (Player p : players) playerUuids.add(p.getUUID());
        Game game = new Game(nextId(), world, playerUuids, type);
        PVPGameEvents.onGameCreate(player, game, playerUuids);
        games.put(game.getGameId(), game);
        setDirty();
        return game;
    }

    @Nullable
    public Game getGameByPlayer(Player player) {
        for (Game game : games.values()) {
            if (game.isPlayerInThisGame(player)) return game;
        }
        return null;
    }

    /**判断玩家是否已经加入任意一场对战*/
    public boolean isPlayerInGame(Player player) {return getGameByPlayer(player) != null;}

    public void tick() {
        Iterator<Game> iterator = this.games.values().iterator();
        while (iterator.hasNext()) {
            Game game = iterator.next();
            if (!game.isActive()) { //移除游戏
                iterator.remove();
                setDirty();
                continue;
            }
            game.tick();
        }
        //if (world.getGameTime() % 200 == 0) System.out.println("GameManager tick: " + games.keySet());
        if (world.getGameTime() % 200 == 0) setDirty();
    }

    public static GameManager fromNbt(ServerLevel world, CompoundTag nbt) {
        GameManager gameManager = new GameManager(world);
        gameManager.nextAvailableId = nbt.getInt("NextAvailableID");
        ListTag nbtList = nbt.getList("Games", 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag nbtCompound = nbtList.getCompound(i);
            Game game = new Game(world, nbtCompound);
            gameManager.games.put(game.getGameId(), game);
        }
        return gameManager;
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
        nbt.putInt("NextAvailableID", this.nextAvailableId);
        ListTag nbtList = new ListTag();
        for (Game game : games.values()) {
            CompoundTag nbtCompound = new CompoundTag();
            game.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        nbt.put("Games", nbtList);
        return nbt;
    }

    private int nextId() {return ++nextAvailableId;}
}
