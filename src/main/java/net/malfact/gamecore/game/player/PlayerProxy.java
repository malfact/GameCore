package net.malfact.gamecore.game.player;

import net.malfact.gamecore.game.Game;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Wrapper class for {@link Player}.
 * <p>
 *     An instance of this class is stateful, in that it keeps track of the registered game, and that it tracks if
 *     the player is invalid and the cause of the invalidation.
 * </p>
 * <p>
 *     It is recommended to call {@link #isValid()} before getting the player, or using any methods that require
 *     a player instance.
 * </p>
 */
@SuppressWarnings("unused")
public abstract class PlayerProxy {

    /**
     * Gets the player that is registered to a game.
     * @return the Player or {@code null}
     */
    public abstract Player getPlayer();

    /**
     * Gets the game this player is registered to.
     * @return the Game or {@code null}
     */
    public abstract Game getGame();

    /**
     * Returns {@code true} if the following conditions are all met.
     * <ul>
     *     <li>The Player is Tracked.</li>
     *     <li>The Player exists.</li>
     *     <li>The Player is online.</li>
     * </ul>
     * @return {@code true} or {@code false}
     */
    public abstract boolean isValid();

    /**
     * Returns {@code true} if the player is tracked.
     * @return {@code true} or {@code false}
     */
    public abstract boolean isTracked();

    /**
     * Returns the UUID of the player.
     * @return the UUID of the player.
     */
    public abstract UUID getUniqueId();

    /**
     * Returns the name of the player.
     * @return the name of the player.
     */
    public abstract String getName();

    /**
     * <li>
     *     <b>If the player is valid</b><br>
     *     Returns {@link Player#isOnline()}
     * </li>
     * <li>
     *     <b>Otherwise</b><br>
     *     Returns {@code null}
     * </li>
     * @return {@code true}, {@code false}, or {@code null}
     */
    public abstract Boolean isOnline();

    /**
     * <li>
     *     <b>If the player is valid</b><br>
     *     Returns {@link Player#isDead()}
     * </li>
     * <li>
     *     <b>Otherwise</b><br>
     *     Returns {@code null}
     * </li>
     * @return {@code true}, {@code false}, or {@code null}
     */
    public abstract Boolean isDead();

    public abstract long getTimeOffline();

    public abstract void invalidate(InvalidReason reason);

    public abstract void revalidate(Player player);

    public enum InvalidReason {
        VALID,
        OFFLINE,
        RELEASED,
    }
}
