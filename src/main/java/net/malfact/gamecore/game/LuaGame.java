package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.api.entity.PlayerLib;
import net.malfact.gamecore.api.event.MinecraftEvent;
import net.malfact.gamecore.script.LuaScript;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.coercion.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.coercion.JavaOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class LuaGame extends Game implements EventExecutor, Listener {

    HashMap<String, List<LuaFunction>> callbacks;
    Map<Class<? extends Event>, NamedReference> listeners;

    private final LuaValue userdata;
    private final LuaScript script;

    private final String name;
    private final String displayName;

    @JavaOnly
    LuaGame(LuaScript script, String displayName) {
        callbacks = new HashMap<>();
        listeners = new HashMap<>();
        userdata = CoerceJavaToLua.coerce(this);

        this.script = script;
        this.name = script.getName();
        this.displayName = displayName;
    }

    public boolean holdsPlayer(Player player) {
        return true;
    }

    public boolean holdsEntity(Entity entity) {
        return true;
    }

    @JavaOnly
    public void registerListener(String eventType, LuaFunction luaFunction) {
        var minecraftEvent = MinecraftEvent.get(eventType);

        if (minecraftEvent == null)
            throw new RuntimeException("Invalid Event Type \"" + eventType + "\"");

        listeners.put(minecraftEvent.eventClass, new NamedReference(eventType.toLowerCase(), luaFunction));

        Bukkit.getPluginManager().registerEvent(minecraftEvent.eventClass, this, EventPriority.NORMAL, this, GameCore.getInstance());
    }

    @Override
    @JavaOnly
    public boolean joinGame(Player player) {
        fireEvent("ON_PLAYER_JOIN_GAME", function -> function.call(userdata, PlayerLib.toUserdata(player)));
        return true;
    }

    @Override
    public boolean leaveGame(Player player) {
        fireEvent("ON_PLAYER_LEAVE_GAME", function -> function.call(userdata, PlayerLib.toUserdata(player)));
        return true;
    }

    @JavaOnly
    @Override
    public void start() {
        if (isRunning())
            return;

        this.startRunning();

        fireEvent("ON_GAME_START", function -> function.call(userdata));
    }

    @JavaOnly
    @Override
    public void tick() {
        fireEvent("ON_GAME_TICK", function -> function.call(userdata));
    }

    @JavaOnly
    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        var a = listeners.get(event.getClass());
        if (a == null) return;

        a.function.call(userdata, MinecraftEvent.parseEvent(event));
    }

    @JavaOnly
    @Override
    public void clean() {
        World world = Bukkit.getWorlds().getFirst();
        for (BlockState ps : blockChanges.values()) {
            world.setBlockData(ps.x, ps.y, ps.z, ps.data);
        }
        blockChanges.clear();

        for (var a : listeners.values()) {
            var b = MinecraftEvent.get(a.name);
            if (b == null) continue;
            b.getHandlers().unregister(this);
        }
        listeners.clear();
    }

    private void fireEvent(String event, Consumer<LuaFunction> consumer) {
        List<LuaFunction> functions = callbacks.get(event);
        if (functions == null || functions.isEmpty())
            return;

        for (LuaFunction f : functions) {
            try {
                consumer.accept(f);
            } catch (Error e) {
                System.out.println("Error running " + script + ":\n\t" + e.getMessage());
                stopRunning();
                World world = Bukkit.getWorlds().getFirst();
                for (BlockState ps : blockChanges.values()) {
                    world.setBlockData(ps.x, ps.y, ps.z, ps.data);
                }
                blockChanges.clear();
            }
        }
    }

    private final HashMap<String, BlockState> blockChanges = new HashMap<>();
    private final List<Entity> spawnedEntities = new ArrayList<>();

    private record BlockState(int x, int y, int z, BlockData data) {}

    private record NamedReference(String name, LuaFunction function) {}

    /* -------------------------------- */
    /* Lua API                          */
    /* -------------------------------- */

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void stop() {
        if (!isRunning())
            return;

        this.stopRunning();
        fireEvent("ON_GAME_END", function -> function.call(userdata));

        clean();
    }

    public void setBlockData(int x, int y, int z, String data) {
        World world = Bukkit.getWorlds().getFirst();

        BlockData blockData;
        try {
            blockData = Bukkit.createBlockData(data);
        } catch (IllegalArgumentException ignored) {
            return;
        }

        String key = x + "_" + y + "_" + z;

        if (!blockChanges.containsKey(key)) {
            blockChanges.put(key, new BlockState(x, y, z, world.getBlockData(x, y, z)));
        }

        world.setBlockData(x, y, z, blockData);
    }

    public String getBlockData(int x, int y, int z) {
        World world = Bukkit.getWorlds().getFirst();

        return world.getBlockData(x, y, z).getMaterial().toString();
    }


}
