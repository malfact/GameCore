package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.event.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class Game {

    private int timer;
    private GameTask gameTask;
    private State state = State.STOPPED;
    private boolean stop = false;

    protected final List<UUID> players;
    protected final List<Entity> entities;

    protected Game() {
        this.players = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    public String getState() {
        return this.state.toString();
    }

    /**
     * Gets the game's name
     * @return name
     */
    public abstract String getName();

    /**
     * Get the game's display name
     * @return display name
     */
    public abstract String getDisplayName();

    /**
     * Add a player to this game.
     * <p>
     *     Fires {@link PlayerJoinGameEvent}
     * </p>
     * @param player the player
     * @return {@code true} if successful, otherwise {@code false}
     */
    public boolean joinGame(Player player) {
        if (state != State.RUNNING)
            return false;

        if (players.contains(player.getUniqueId()))
            return false;

        GameCore.logDebug("Registered player " + player.getName() + " with game: " + getName());

        players.add(player.getUniqueId());
        Bukkit.getPluginManager().callEvent(new PlayerJoinGameEvent(this, player));

        return true;
    }

    /**
     * Remove a player from this game.
     * <p>
     *     Fires {@link PlayerLeaveGameEvent}
     * </p>
     * @param player the player
     * @return {@code true} if successful, otherwise {@code false}
     */
    public boolean leaveGame(Player player) {
        if (!players.contains(player.getUniqueId()))
            return false;

        players.remove(player.getUniqueId());
        Bukkit.getPluginManager().callEvent(new PlayerLeaveGameEvent(this, player));
        return true;
    }

    /**
     * Gets if a player is currently in this game.
     * @param player the player
     * @return {@code true} if the player in this game, otherwise {@code false}
     */
    public boolean hasPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }

    /**
     * Gets if an entity is currently registered to this game.
     * <p><i>
     *     <b>Note:</b><br>
     *     Automatically passed to {@link #hasPlayer(Player)} if the entity is a player.
     * </i></p>
     * @param entity the entity
     * @return {@code true} if the entity is registered, otherwise {@code false}
     */
    public boolean hasEntity(Entity entity) {
        if (entity instanceof Player player)
            return hasPlayer(player);

        return entities.contains(entity);
    }

    /**
     * Registers an entity to this game.
     * <p><i>
     *     <b>Note:</b><br>
     *     Automatically passed to {@link #joinGame(Player)} if the entity is a player.
     * </i></p>
     * @param entity the entity
     * @return {@code true} if successful, otherwise {@code false}
     */
    public final boolean registerEntity(Entity entity) {
        if (entity instanceof Player player)
            return joinGame(player);

        if (entities.contains(entity))
            return false;

        GameCore.logDebug("Registered entity " + entity.getName() + " with game: " + getName());

        entities.add(entity);
        return true;
    }

    /**
     * Unregisters an entity to this game.
     * <p><i>
     *     <b>Note:</b><br>
     *     Automatically passed to {@link #leaveGame(Player)} if the entity is a player.
     * </i></p>
     * @param entity the entity
     * @return {@code true} if successful, otherwise {@code false}
     */
    public final boolean unregisterEntity(Entity entity) {
        if (entity instanceof Player player)
            return leaveGame(player);

        if (!entities.contains(entity))
            return false;

        entities.remove(entity);
        return true;
    }

    public final Player[] getPlayers() {
        Player[] players = new Player[this.players.size()];
        for (int i = 0; i < this.players.size(); i++) {
            players[i] = Bukkit.getPlayer(this.players.get(i));
        }

        return players;
    }

    public final Entity[] getEntities() {
        return entities.toArray(new Entity[0]);
    }

    /* ---- Game Loop ---- */

    /**
     * Start the game.
     * <p>
     *     Fires {@link GameStartEvent}
     * </p>
     */
    public final void start() {
        if (state != State.STOPPED)
            return;

        state = State.STARTING;

        GameCore.logDebug("Starting Game: " + getName());

        timer = 0;

        if (gameTask != null && !gameTask.isCancelled())
            gameTask.cancel();

        this.onStart();
        Bukkit.getPluginManager().callEvent(new GameStartEvent(this));

        state = State.RUNNING;
        gameTask = new GameTask(this);
        gameTask.runTaskTimer(GameCore.getInstance(), 0, 1);
    }

    /**
     * Tick the game.
     * <p>
     *     Fires {@link GameTickEvent}
     * </p>
     */
    private void tick() {
        this.timer++;
        this.onTick();
        Bukkit.getPluginManager().callEvent(new GameTickEvent(this));
    }

    /**
     * Stop the game.
     * <p>
     *     Fires {@link GameStopEvent}
     * </p>
     */
    public final void stop() {
        if (state == State.STARTING || state == State.RUNNING) {
            GameCore.logDebug("Stop Called for " + getName());
            stop = true;
        }
    }


    void forceStop() {
        state = State.STOPPING;

        GameCore.logDebug("Stopping Game: " + getName());

        this.onStop();
        Bukkit.getPluginManager().callEvent(new GameStopEvent(this));
        players.clear();

        for (var entity : entities) {
            entity.remove();
        }

        this.clean();

        if (gameTask != null && !gameTask.isCancelled())
            gameTask.cancel();

        stop = false;
        state = State.STOPPED;
    }

    /**
     * Runs in this order:
     * <ol>
     *     <li>{@code stop()}</li>
     *     <li>{@code onReload()}</li>
     *     <li>{@code start()} <i>(Only if it was previously running)</i></li>
     * </ol>
     */
    public final void reload() {
        boolean wasRunning = state == State.RUNNING;

        GameCore.logDebug("Reloading Game: " + getName());

        if (wasRunning)
            this.forceStop();

        this.onReload();

        if (wasRunning)
            this.start();
    }

    /* ---- Event Checking ---- */

    /**
     * Helper method for event dispatching
     */
    public final boolean acceptsEvent(Event event) {
        if (event instanceof PlayerEvent playerEvent) {
            return hasPlayer(playerEvent.getPlayer());
        } else if (event instanceof EntityEvent entityEvent) {
            return hasEntity(entityEvent.getEntity());
        } else if (event instanceof VehicleEvent vehicleEvent) {
            return hasEntity(vehicleEvent.getVehicle());
        }

        return true;
    }

    /* ---- --------- ---- */

    /**
     * Fired during {@link #stop()}, after {@link #onStop()}
     */
    public abstract void clean();

    /**
     * Fired during {@link #start()}
     */
    protected void onStart() {}

    /**
     * Fired during {@link #tick()}
     */
    protected void onTick() {}

    /**
     * Fired during {@link #stop()}
     */
    protected void onStop() {}

    /**
     * Fired during {@link #reload()}, after {@link #stop()} and before {@link #start()}
     */
    protected void onReload() {}

    /* ---- --------- ---- */

    /**
     * Get the game's current time in ticks
     * @return time in ticks
     */
    public final int time() {
        return timer;
    }

    /**
     * Return the running status of the game
     * @return running
     */
    public final boolean isRunning() {
        return state == State.RUNNING || state == State.STARTING;
    }

    public final boolean isActive() {
        return state != State.STOPPED;
    }

    private static class GameTask extends BukkitRunnable {

        private final Game game;

        private GameTask(Game game) {
            this.game = game;
        }

        @Override
        public void run() {
            if (game.state == State.STOPPED) {
                this.cancel();
                return;
            }

            if (game.state != State.RUNNING)
                return;

            if (game.stop) {
                GameCore.logDebug(game.getName() + "::GameTask # Stopping Game");
                this.cancel();
                game.forceStop();
                return;
            }

            game.timer++;
            game.tick();
        }
    }

    private enum State {
        STOPPED,
        STARTING,
        RUNNING,
        STOPPING
    }
}
