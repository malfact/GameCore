package net.malfact.gamecore.game;

import io.papermc.paper.event.player.PlayerNameEntityEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.event.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"unused", "UnusedReturnValue", "EmptyMethod"})
public abstract class Game {

    private int timer;
    private GameTask gameTask;
    private State state = State.STOPPED;
    private boolean stop = false;

    protected final List<UUID> players;
    protected final List<Entity> entities;
    protected final List<Cleanable<?>> cleanables;

    protected Game() {
        this.players = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.cleanables = new ArrayList<>();
    }

    public State getState() {
        return this.state;
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

    public abstract void setDisplayName(String name);

    /**
     * Add a player to this game.
     * <p>
     *     Fires {@link PlayerJoinGameEvent}
     * </p>
     * @param player the player
     */
    final void joinGame(Player player) {
        if (players.contains(player.getUniqueId()))
            return;

        GameCore.logger().debug("Registered player {} with game: {}", player.getName(), getName());
        players.add(player.getUniqueId());
        Bukkit.getPluginManager().callEvent(new PlayerJoinGameEvent(this, player));
    }

    /**
     * Remove a player from this game.
     * <p>
     *     Fires {@link PlayerLeaveGameEvent}
     * </p>
     * @param player the player
     */
    final void leaveGame(Player player) {
        if (!players.contains(player.getUniqueId()))
            return;

        GameCore.logger().debug("Unregistered player {} with game: {}", player.getName(), getName());
        Bukkit.getPluginManager().callEvent(new PlayerLeaveGameEvent(this, player));
        players.remove(player.getUniqueId());
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
            return GameCore.gameManager().joinGame(player, this);

        if (entities.contains(entity))
            return false;

        entities.add(entity);
        GameCore.logDebug("Registered entity " + entity.getName() + " with game: " + getName());
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
            return GameCore.gameManager().leaveGame(player);

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

        if (gameTask != null && !gameTask.isCancelled())
            gameTask.cancel();

        stop = false;
        state = State.STOPPED;

        this.clean();
    }

    void clean() {
        for (var entity : entities) {
            entity.remove();
        }

        entities.clear();

        for (var player : getPlayers()) {
            leaveGame(player);
        }

        cleanables.forEach(Cleanable::clean);
        cleanables.clear();

        this.onClean();
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
        return switch (event) {
            case PlayerInteractEntityEvent e -> hasPlayer(e.getPlayer()) || hasEntity(e.getRightClicked());
            case PlayerShearEntityEvent e ->    hasPlayer(e.getPlayer()) || hasEntity(e.getEntity());
            case PlayerLeashEntityEvent e ->    hasPlayer(e.getPlayer()) || hasEntity(e.getEntity());
            case PlayerNameEntityEvent e ->     hasPlayer(e.getPlayer()) || hasEntity(e.getEntity());
            case PrePlayerAttackEntityEvent e -> hasPlayer(e.getPlayer()) || hasEntity(e.getAttacked());
            case EntityDamageByEntityEvent e -> hasEntity(e.getEntity()) || hasEntity(e.getDamager());
            case PlayerEvent e ->               hasPlayer(e.getPlayer());
            case EntityEvent e ->               hasEntity(e.getEntity());
            case VehicleEvent e ->              hasEntity(e.getVehicle());
            default -> true;
        };
    }

    /* ---- --------- ---- */

    /**
     * Fired during {@link #stop()}, after {@link #onStop()}
     */
    public abstract void onClean();

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

    public void registerCleanable(Cleanable<BossBar> cleanable) {
        cleanables.add(cleanable);
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

    public enum State {
        STOPPED,
        STARTING,
        RUNNING,
        STOPPING
    }

    public abstract void registerBlockChange(Location loc);
}
