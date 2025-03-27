package com.amotassic.dabaosword.pvpgame;


import com.amotassic.dabaosword.api.event.PVPGameTickEvent;
import com.amotassic.dabaosword.event.PVPGameEvents;
import com.amotassic.dabaosword.util.MODConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;
import java.util.function.Consumer;

import static com.amotassic.dabaosword.util.ModTools.title;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class Game {
    private final ServerLevel world;
    private final int id;
    private final Set<UUID> players = new HashSet<>();
    private final int type;
    private boolean active;
    private int countDown;
    private int gameTime;
    private int timeOut;
    //列举各项数据
    public int zhongLives, fanLives, neiLives;
    public int zhongScore, fanScore, neiScore;
    private Map<String, Integer> primaryDataCache;
    public static final String FANCOUNT = "fanCount", NEICOUNT = "neiCount", ZHONGCOUNT = "zhongCount", ZHONGLIVES = "zhongLives", FANLIVES = "fanLives", NEILIVES = "neiLives";

    public Game(int id, ServerLevel world, Set<UUID> players, int type) {
        this.world = world;
        this.id = id;
        this.players.addAll(players);
        this.type = type;
        this.active = true;
        this.countDown = MODConfig.WaitTime * 20;
        this.gameTime = 0;
        this.timeOut = MODConfig.TimeOut;
        initData();
    }

    public Game(ServerLevel world, CompoundTag nbt) {
        this.world = world;
        this.id = nbt.getInt("Id");
        ListTag nbtList = nbt.getList("Players", 10);
        for (var nbtElement : nbtList) {
            this.players.add(NbtUtils.loadUUID(nbtElement));
        }
        this.type = nbt.getInt("Type");
        this.active = nbt.getBoolean("Active");
        this.countDown = nbt.getInt("CountDown");
        this.gameTime = nbt.getInt("GameTime");
        this.timeOut = nbt.getInt("TimeOut");
        this.zhongLives = nbt.getInt("ZhongLives");
        this.fanLives = nbt.getInt("FanLives");
        this.neiLives = nbt.getInt("NeiLives");
        this.zhongScore = nbt.getInt("ZhongScore");
        this.fanScore = nbt.getInt("FanScore");
        this.neiScore = nbt.getInt("NeiScore");
    }

    public Map<String, Integer> getPrimaryData() {
        if (primaryDataCache != null) return primaryDataCache;

        Map<String, Integer> data = new HashMap<>();
        int playerCount = getPlayers().size();
        int fanCount = playerCount / 2;
        int neiCount = playerCount > 2 ? 1 : 0; //如果是无内奸模式，参与人数为偶数时，不设置内奸
        if (this.type == 1 && playerCount % 2 == 0) neiCount = 0;
        int zhongCount = playerCount - fanCount - neiCount;
        int zhongLives, fanLives, neiLives;
        zhongLives = fanLives = fanCount * 3;
        neiLives = neiCount > 0 ? fanCount * 3 : 0;
        data.put(FANCOUNT, fanCount); data.put(NEICOUNT, neiCount); data.put(ZHONGCOUNT, zhongCount);
        data.put(ZHONGLIVES, zhongLives); data.put(FANLIVES, fanLives); data.put(NEILIVES, neiLives);
        primaryDataCache = data;
        return data;
    }

    private void initData() {
        //根据人数随机分配身份
        var primaryData = getPrimaryData();
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < primaryData.get(ZHONGCOUNT); i++) ids.add(Identity.ZHONG.tag);
        for (int i = 0; i < primaryData.get(FANCOUNT); i++) ids.add(Identity.FAN.tag);
        for (int i = 0; i < primaryData.get(NEICOUNT); i++) ids.add(Identity.NEI.tag);
        Collections.shuffle(ids); // 随机打乱列表中的元素
        forEachPlayer(player -> { //确保移除所有的身份标签再添加新的身份标签
            player.getTags().remove("dabaosword.zhong");
            player.getTags().remove("dabaosword.fan");
            player.getTags().remove("dabaosword.nei");
            player.addTag(ids.removeFirst());
        });
        this.zhongLives = primaryData.get(ZHONGLIVES);
        this.fanLives = primaryData.get(FANLIVES);
        this.neiLives = primaryData.get(NEILIVES);
        this.zhongScore = this.fanScore = this.neiScore = 0;
    }

    public void writeNbt(CompoundTag nbt) {
        nbt.putInt("Id", this.id);
        ListTag nbtList = new ListTag();
        for (UUID uuid : this.players) nbtList.add(NbtUtils.createUUID(uuid));
        nbt.put("Players", nbtList);
        nbt.putInt("Type", this.type);
        nbt.putBoolean("Active", this.active);
        nbt.putInt("CountDown", this.countDown);
        nbt.putInt("GameTime", this.gameTime);
        nbt.putInt("TimeOut", this.timeOut);
        nbt.putInt("ZhongLives", this.zhongLives);
        nbt.putInt("FanLives", this.fanLives);
        nbt.putInt("NeiLives", this.neiLives);
        nbt.putInt("ZhongScore", this.zhongScore);
        nbt.putInt("FanScore", this.fanScore);
        nbt.putInt("NeiScore", this.neiScore);
    }

    public boolean isPlayerInThisGame(Player player) {
        return this.players.contains(player.getUUID());
    }

    public int getGameId() {return id;}

    /**游戏被加载，不论是等待中还是已经开始*/
    public boolean isActive() {return active;}

    public Set<UUID> getPlayers() {return players;}

    /**游戏处于准备阶段倒计时，此时玩家可以拒绝加入游戏*/
    public boolean isWaiting() {return countDown > 0;}

    public int getCountDown() {return countDown;}

    public int getGameTime() {return gameTime;}

    public int getTimeOut() {return timeOut;}

    /**游戏已经开始，且不处于准备阶段*/
    public boolean isOn() {return getGameTime() > 0;}

    public void refuseGame(Player player) {
        if (!isWaiting()) return;
        discardGame();
        forEachPlayer(p -> {
            p.displayClientMessage(Component.translatable("dabaosword.game.refuse", player.getDisplayName()).withStyle(ChatFormatting.RED), false);
            voice(p, SoundEvents.SHIELD_BREAK);
        });
    }

    public void win(Identity identity) {
        forEachPlayer(player -> {
            if (getIdentity(player) == identity) {
                voice(player, "win");
                title(player, Component.translatable("dabaosword.game.win").withStyle(ChatFormatting.GOLD));
            }
            player.displayClientMessage(Component.translatable("dabaosword.game.end", Component.translatable(identity.tag)).withStyle(getIdentityColor(identity)), false);
        });
        discardGame();
    }

    public void timeOut() {
        forEachPlayer(player -> player.displayClientMessage(Component.translatable("dabaosword.game.timeout").withStyle(ChatFormatting.RED), false));
        Integer max = findUniqueMax(zhongScore, fanScore, neiScore);
        if (max == null) discardGame();
        else if (zhongScore == max) win(Identity.ZHONG);
        else if (fanScore == max) win(Identity.FAN);
        else if (neiScore == max) win(Identity.NEI);
    }

    public void discardGame() {
        this.active = false;
        var scoreboard = world.getServer().getScoreboard();
        var obj = scoreboard.getObjectives().stream().filter(o -> o.getName().equals("dabaosword.death")).findFirst().orElse(null);
        if (obj != null && PVPGameEvents.getGameManager().getGameCount() <= 1) scoreboard.removeObjective(obj);
        forEachPlayer(player -> {
            player.getTags().remove("dabaosword.zhong");
            player.getTags().remove("dabaosword.fan");
            player.getTags().remove("dabaosword.nei");
            if (player.isSpectator()) {
                player.setGameMode(GameType.SURVIVAL); player.kill();
            }
        });
    }

    public void tick() {
        if (!this.active) return;
        NeoForge.EVENT_BUS.post(new PVPGameTickEvent(this, world));
        //倒计时为-1时，游戏开始计时
        if (this.countDown > -1) --this.countDown; else ++this.gameTime;
        if (isOn() && getGameTime() % 20 == 0) --this.timeOut;
    }

    public void forEachPlayer(Consumer<ServerPlayer> action) {
        for (UUID uuid : getPlayers()) {
            ServerPlayer player = world.getServer().getPlayerList().getPlayer(uuid);
            if (player == null) continue;
            action.accept(player);
        }
    }

    public int getRespawnChances(Identity identity) {
        var data = getPrimaryData();
        return switch (identity) {
            case ZHONG -> zhongLives - data.get(ZHONGCOUNT) + 1;
            case FAN -> fanLives - data.get(FANCOUNT) + 1;
            case NEI -> neiLives - data.get(NEICOUNT) + 1;
        };
    }

    public int getLives(Identity identity) {
        return switch (identity) {
            case ZHONG -> zhongLives;
            case FAN -> fanLives;
            case NEI -> neiLives;
        };
    }

    public void setLives(Identity identity, int lives) {
        switch (identity) {
            case ZHONG -> zhongLives = lives;
            case FAN -> fanLives = lives;
            case NEI -> neiLives = lives;
        }
    }

    /**减少该玩家所在队伍的剩余生命数（等于0不会减少），玩家死亡时调用*/
    public void decreaseLives(ServerPlayer player) {
        Identity identity = getIdentity(player);
        int lives = getLives(identity);
        if (lives > 0) setLives(identity, lives - 1);
    }

    public int getScore(Identity identity) {
        return switch (identity) {
            case ZHONG -> zhongScore;
            case FAN -> fanScore;
            case NEI -> neiScore;
        };
    }

    public void setScore(Identity identity, int score) {
        switch (identity) {
            case ZHONG -> zhongScore = score;
            case FAN -> fanScore = score;
            case NEI -> neiScore = score;
        }
    }

    /**增加该玩家所在队伍的分数，同时向所有玩家播报分数*/
    public void increaseScore(ServerPlayer player) {
        Identity identity = getIdentity(player);
        setScore(identity, getScore(identity) + 1);
        this.timeOut = MODConfig.TimeOut;
        forEachPlayer(p -> p.displayClientMessage(Component.translatable("dabaosword.score.add", player.getDisplayName()).withStyle(ChatFormatting.BOLD), false));
    }

    /**确保玩家在该对局中才可以调用本方法*/
    public Identity getIdentity(ServerPlayer player) {
        if (player.getTags().contains(Identity.ZHONG.tag)) return Identity.ZHONG;
        if (player.getTags().contains(Identity.FAN.tag)) return Identity.FAN;
        return Identity.NEI;
    }

    public static Integer findUniqueMax(int... numbers) {
        if (numbers.length == 0) return null;
        int max = numbers[0];
        boolean isUnique = true;
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
                isUnique = true;
            } else if (numbers[i] == max) {isUnique = false;}
        }
        return isUnique ? max : null;
    }

    public static ChatFormatting getIdentityColor(Identity identity) {
        return switch (identity) {
            case ZHONG -> ChatFormatting.YELLOW;
            case FAN -> ChatFormatting.GREEN;
            case NEI -> ChatFormatting.BLUE;
        };
    }

    public enum Identity {
        ZHONG("dabaosword.zhong"),
        FAN("dabaosword.fan"),
        NEI("dabaosword.nei");

        public final String tag;

        Identity(String tag) {
            this.tag = tag;
        }
    }
}
