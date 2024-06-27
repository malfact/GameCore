package net.malfact.gamecore.game;

import net.malfact.gamecore.GameCore;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class Game {

    private int timer;

    private GameTask gameTask;

    private boolean running = false;

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

    public abstract boolean joinGame(Player player);

    public abstract boolean leaveGame(Player player);

    /* ---- Game Loop ---- */

    /**
     * Start the game and fire the {@code ON_GAME_START} event.
     */
    public abstract void start();

    /**
     * Tick the game and fire the {@code ON_GAME_TICK} event.
     */
    public abstract void tick();

    /**
     * Stop the game and fire the {@code ON_GAME_END} event
     */
    public abstract void stop();

    public abstract void clean();

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
        return running;
    }

    /**
     * Starts game's tick updates
     */
    protected final void startRunning() {
        if (gameTask != null && !gameTask.isCancelled())
            return;

        timer = 0;

        gameTask = new GameTask(this);
        gameTask.runTaskTimer(GameCore.getInstance(), 0, 1);
        running = true;
    }

    /**
     * Stops game's tick updates
     */
    protected final void stopRunning() {
        if (gameTask == null || gameTask.isCancelled())
            return;

        gameTask.cancel();
        running = false;
    }

    private static class GameTask extends BukkitRunnable {

        private final Game game;

        private GameTask(Game game) {
            this.game = game;
        }

        @Override
        public void run() {
            game.timer++;
            game.tick();
        }
    }
}
