package net.malfact.gamecore.queue;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.Messages;
import net.malfact.gamecore.player.GamePlayer;
import net.malfact.gamecore.team.GameTeam;
import net.malfact.gamecore.util.DataHolder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameQueue implements DataHolder<QueueData> {
    public final String name;

    private boolean enabled = false;

    private final List<GamePlayer> players;


    GameQueue(String name) {
        this.name = name;

        players = new ArrayList<>();
    }

    /**
     * Set the enabled status of this Queue
     * @param enabled enabled status
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (!enabled)
            empty();
    }

    /**
     * Get the enabled status of this Queue
     * @return <i>true</i> if enabled, <i>false</i> otherwise
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     * Get the GamePlayers currently in this Queue
     * @return a set of GamePlayers in this Queue
     */
    public Set<GamePlayer> getPlayers() {
        return new HashSet<>(players);
    }

    /**
     * Adds a GamePlayer to the Queue
     *
     * @param player the GamePlayer to add
     */
    public void addPlayer(GamePlayer player) {
        if (players.contains(player) || !enabled)
            return;

        players.add(player);
        String oldQueue = player.getQueueName();

        if (!oldQueue.isEmpty() && !oldQueue.equals(this.name)) {
            GameQueue oldGameQueue = GameCore.getQueueManager().getQueue(oldQueue);
            if (oldGameQueue != null)
                oldGameQueue.removePlayer(player);
        }

        player.setQueue(this.name);

        player.sendMessage(Messages.get("SELF_JOIN_QUEUE",this.name));

    }

    /**
     * Removes a player from the Queue
     *
     * @param player the GamePlayer to remove
     */
    public void removePlayer(GamePlayer player) {
        if (!players.contains(player))
            return;

        players.remove(player);
        player.setQueue("");
        player.sendMessage(Messages.get("SELF_LEFT_QUEUE",this.name));

    }

    /**
     * Gets if a player is in the Queue
     * @param player the player to look for
     * @return <i>true</i> if the player is found, <i>false</i> otherwise
     */
    public boolean hasPlayer(GamePlayer player) {
        if (!enabled || players.isEmpty())
            return false;

        return players.contains(player);
    }

    /**
     * Empties the queue
     */
    public void empty() {
        if (players.isEmpty())
            return;

        Iterator<GamePlayer> it = players.iterator();
        while (it.hasNext()) {
            GamePlayer player = it.next();

            player.setQueue("");
            player.sendMessage(Messages.get("SELF_LEFT_QUEUE", this.name));

            it.remove();
        }
    }

    /**
     * Returns if the Queue is empty
     * @return <i>true</i> if it is empty, <i>false</i> otherwise
     */
    public boolean isEmpty() {
        return players.isEmpty();
    }

    /**
     * Returns the number of players in the Queue
     * @return the number of players in queue
     */
    public int getPlayerCount() {
        return players.size();
    }

    public void popWithTag(String tag) {
        Iterator<GamePlayer> iterator = players.iterator();

        while (iterator.hasNext()) {
            GamePlayer gamePlayer = iterator.next();

            gamePlayer.setQueue("");

            gamePlayer.getPlayer().addScoreboardTag(tag);

            iterator.remove();
        }
    }

    public void popWithTeam(@NotNull GameTeam team) {
        Iterator<GamePlayer> iterator = players.iterator();

        while (iterator.hasNext()) {
            GamePlayer player = iterator.next();
            team.addPlayer(player);
            player.setQueue("");
            iterator.remove();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public int popWithTeam(@NotNull GameTeam team, int count) {
        Objects.requireNonNull(team);

        Iterator<GamePlayer> iterator = players.iterator();

        int popped = 0;
        while (popped < count && iterator.hasNext()) {
            GamePlayer player = iterator.next();
            team.addPlayer(player);
            player.setQueue("");
            iterator.remove();

            popped++;
        }

        return popped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameQueue gameQueue)) return false;
        return Objects.equals(name, gameQueue.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public QueueData getDataObject() {
        return new QueueData(name, enabled);
    }

    @Override
    public void setDataObject(QueueData data) {
        setEnabled(data.enabled);
    }
}
