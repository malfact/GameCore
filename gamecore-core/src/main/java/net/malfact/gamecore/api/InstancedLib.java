package net.malfact.gamecore.api;

import net.malfact.gamecore.game.Game;

public abstract class InstancedLib implements LuaLib {

    protected final Game instance;

    public InstancedLib(Game game) {
        this.instance = game;
    }

}
