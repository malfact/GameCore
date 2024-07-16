package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.event.GameStartEvent;
import net.malfact.gamecore.event.GameStopEvent;
import net.malfact.gamecore.event.GameTickEvent;
import net.malfact.gamecore.event.player.PlayerTriggerEvent;
import net.malfact.gamecore.game.player.PlayerProxy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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

    private final List<PlayerTrigger> triggerQueue;

    private UUID uuid;

    protected Game() {
        this.players = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.cleanables = new ArrayList<>();
        this.triggerQueue = new ArrayList<>();
    }

    public State getState() {
        return this.state;
    }

    public UUID getUniqueId() {
        return uuid;
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
     * Gets if an entity is currently registered to this game.
     * <p><i>
     *     <b>Note:</b><br>
     *     Automatically passed to {@link GameManager#isPlayerInGame(Player, Game)} if the entity is a player.
     * </i></p>
     * @param entity the entity
     * @return {@code true} if the entity is registered, otherwise {@code false}
     */
    public boolean hasEntity(Entity entity) {
        if (entity instanceof Player player)
            return GameCore.gameManager().isPlayerInGame(player, this);

        return entities.contains(entity);
    }

    /**
     * Registers an entity to this game.
     * <p><i>
     *     <b>Note:</b><br>
     *     Automatically passed to {@link GameManager#joinGame(Player, Game)} if the entity is a player.
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

        return true;
    }

    /**
     * Unregisters an entity to this game.
     * <p><i>
     *     <b>Note:</b><br>
     *     Automatically passed to {@link GameManager#leaveGame(Player)} if the entity is a player.
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

    public final void sendTrigger(PlayerProxy player, String trigger) {
        if (trigger == null || !player.isTracked() || !player.getGame().equals(this))
            return;

        triggerQueue.add(new PlayerTrigger(player, trigger));
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

        this.uuid = UUID.randomUUID();

        state = State.STARTING;

        timer = 0;

        if (gameTask != null && !gameTask.isCancelled())
            gameTask.cancel();

        this.onStart();
        Bukkit.getPluginManager().callEvent(new GameStartEvent(this));

        state = State.RUNNING;
        gameTask = new GameTask(this);
        gameTask.runTaskTimer(GameCore.instance(), 0, 1);
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

        if (!triggerQueue.isEmpty()) {
            for (var trigger : triggerQueue) {
                Bukkit.getPluginManager().callEvent(new PlayerTriggerEvent(this, trigger.player, trigger.trigger));
            }
            triggerQueue.clear();
        }
    }

    /**
     * Stop the game.
     * <p>
     *     Fires {@link GameStopEvent}
     * </p>
     */
    public final void stop() {
        if (state == State.STARTING || state == State.RUNNING) {
            stop = true;
        }
    }


    void forceStop() {
        state = State.STOPPING;

        try {
            this.onStop();
        } catch (Exception ignored) {}

        Bukkit.getPluginManager().callEvent(new GameStopEvent(this));

        if (gameTask != null && !gameTask.isCancelled())
            gameTask.cancel();

        stop = false;
        state = State.STOPPED;

        for (var entity : entities) {
            entity.remove();
        }

        entities.clear();

        cleanables.forEach(Cleanable::clean);
        cleanables.clear();

        try {
            this.onClean();
        } catch (Exception ignored) {}

        GameCore.gameManager().onGameStopped(this);
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

        if (wasRunning)
            this.forceStop();

        try {
            this.onReload();
        } catch (Exception ignored) {}

        if (wasRunning)
            this.start();
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
                this.cancel();
                game.forceStop();
                return;
            }

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Game other) {
            // If both names do not match -> false
            if (!this.getName().equals(other.getName()))
                return false;

            // If both are active -> Compare instance uuid
            // Otherwise:
            //   If both active value match -> true
            //   Otherwise: -> false
            return (this.isActive() && other.isActive())
                ? this.uuid.equals(other.uuid)
                : this.isActive() == other.isActive();
        }

        return false;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    private record PlayerTrigger(PlayerProxy player, String trigger) {}
}
