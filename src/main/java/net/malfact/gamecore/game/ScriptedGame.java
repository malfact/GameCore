package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.api.event.EventRegistry;
import net.malfact.gamecore.api.event.GameListener;
import net.malfact.gamecore.script.Instance;
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
import java.util.function.Supplier;

public class ScriptedGame extends Game {

    private final Supplier<Instance> instance;
    private String displayName;

    private List<FunctionCallback> callbacks;
    private Map<String, SavedBlockData> savedBlockDataMap;

    public ScriptedGame(Supplier<Instance> instance) {
        this.instance = instance;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public GameListener getFunctionCallback(LuaFunction function) {
        if (callbacks == null)
            callbacks = new ArrayList<>();

        FunctionCallback callback = new FunctionCallback(instance.get(), function);
        callbacks.add(callback);

        return callback;
    }

    @Override
    public String getName() {
        return instance.get().getScript().getName();
    }

    @Override
    public String getDisplayName() {
        if (this.displayName == null || this.displayName.isEmpty())
            return getName();

        return displayName;
    }

    @Override
    protected void onStart() {
        if (!instance.get().getScript().run()){
            GameCore.getInstance().logError("Problem in " + getName() + ": Stopping.");
            stop();
        }
    }

    @Override
    public void onReload() {
        GameCore.getScriptManager().reloadScript(instance.get().getScript(), this);
    }

    @Override
    public void clean() {
        if (callbacks != null && !callbacks.isEmpty()) {
            for (var callback : callbacks) {
                EventRegistry.unregisterListener(callback);
            }
        }

        if (savedBlockDataMap != null && !savedBlockDataMap.isEmpty()) {
            for (var change : savedBlockDataMap.values()) {
                World world = change.location().getWorld();
                world.setBlockData(change.location, change.blockData);
            }
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

    private static class FunctionCallback extends GameListener {

        private final LuaFunction function;

        private FunctionCallback(Instance instance, LuaFunction function) {
            super(instance);
            this.function = function;
        }

        @Override
        public void onEvent(Event event) {
            try {
                function.call(instance.getValueOf(event));
            } catch (LuaError error) {
                GameCore.getInstance().logError("Error in " + instance.getScript() + ":\n" + error.getMessage());
            }
        }
    }
}
