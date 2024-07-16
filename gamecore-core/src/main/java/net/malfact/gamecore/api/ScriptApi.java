package net.malfact.gamecore.api;

import net.malfact.gamecore.LuaScript;
import net.malfact.gamecore.game.ScriptedGame;

import java.io.File;

public interface ScriptApi {

    LuaScript getScript(File file);

    void loadScript(LuaScript script, ScriptedGame game);
}
