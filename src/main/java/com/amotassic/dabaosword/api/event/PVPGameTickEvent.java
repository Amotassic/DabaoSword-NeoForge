package com.amotassic.dabaosword.api.event;

import com.amotassic.dabaosword.pvpgame.Game;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;

public class PVPGameTickEvent extends Event {
    private final Game game;
    private final ServerLevel level;

    public PVPGameTickEvent(Game game, ServerLevel level) {
        this.game = game;
        this.level = level;
    }

    public ServerLevel getLevel() {return level;}

    public Game getGame() {return game;}
}
