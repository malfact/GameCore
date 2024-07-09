package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.LuaScript;
import net.malfact.gamecore.api.LuaApi;
import net.malfact.gamecore.lua.event.EventRegistry;
import net.malfact.gamecore.lua.event.GameListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptedGame extends Game {

    private final LuaScript script;
    private String displayName;

    private List<FunctionCallback> callbacks;
    private Map<String, SavedBlockData> savedBlockDataMap;

    public ScriptedGame(LuaScript script) {
        this.script = script;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public GameListener getFunctionCallback(LuaFunction function) {
        if (callbacks == null)
            callbacks = new ArrayList<>();

        FunctionCallback callback = new FunctionCallback(this, function);
        callbacks.add(callback);

        return callback;
    }

    @Override
    public String getName() {
        return script.getName();
    }

    @Override
    public String getDisplayName() {
        if (this.displayName == null || this.displayName.isEmpty())
            return getName();

        return displayName;
    }

    @Override
    protected void onStart() {
        if (!script.run()){
            GameCore.getInstance().logError("Problem in " + getName() + ": Stopping.");
            stop();
        }
    }

    @Override
    public void onReload() {
        GameCore.scriptApi().loadScript(script, this);
    }

    @Override
    public void onClean() {
        if (callbacks != null && !callbacks.isEmpty()) {
            for (var callback : callbacks) {
                EventRegistry.unregisterListener(callback);
            }
            callbacks.clear();
        }

        if (savedBlockDataMap != null && !savedBlockDataMap.isEmpty()) {
            for (var change : savedBlockDataMap.values()) {
                World world = change.location().getWorld();
                world.setBlockData(change.location, change.blockData);
            }
            savedBlockDataMap.clear();
        }
    }

    public void registerBlockChange(Location loc)
    {
        if (!isRunning())
            return;

        World world = loc.getWorld();
        BlockData data = world.getBlockData(loc);
        String key = loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        if (savedBlockDataMap == null)
            savedBlockDataMap = new HashMap<>();
        else if (savedBlockDataMap.containsKey(key))
            return;

        savedBlockDataMap.put(key, new SavedBlockData(loc.toBlockLocation(), data));
    }

    private static record SavedBlockData(Location location, BlockData blockData){};

    private class FunctionCallback extends GameListener {

        private final LuaFunction function;

        private FunctionCallback(ScriptedGame instance, LuaFunction function) {
            super(instance);
            this.function = function;
        }

        @Override
        public void onEvent(Event event) {
            try {
                function.call(LuaApi.userdataOf(event, instance));
            } catch (LuaError error) {
                GameCore.logger().error("Error in {}:\n\t{}", script, error.getMessage());
            }
        }
    }
}
