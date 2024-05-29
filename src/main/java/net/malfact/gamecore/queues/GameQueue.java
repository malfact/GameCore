package net.malfact.gamecore.queues;

import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.players.GamePlayer;
import net.malfact.gamecore.teams.GameTeam;
import net.malfact.gamecore.util.DataHolder;
import net.malfact.gamecore.util.Messages;
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

    void clean() {
        if (players.isEmpty())
            return;

        Iterator<GamePlayer> it = players.iterator();
        while (it.hasNext()) {
            GamePlayer player = it.next();

            player.setQueue("");
            player.handle().sendMessage(Messages.deserialize(Messages.INFO_LEFT_QUEUE, "queue", this.name));

            it.remove();
        }
    }

    /**
     * Set the enabled status of this Queue
     * @param enabled enabled status
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (!enabled)
            clean();
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
     * @param player the GamePlayer to add
     * @return <i>true</i> if the player was added successfully, <i>false</i> otherwise
     */
    public boolean addPlayer(GamePlayer player) {
        if (players.contains(player) || !enabled)
            return false;

        players.add(player);
        String oldQueue = player.getQueue();

        if (!oldQueue.isEmpty() && !oldQueue.equals(this.name)) {
            GameQueue oldGameQueue = GameCore.getQueueManager().getQueue(oldQueue);
            if (oldGameQueue != null)
                oldGameQueue.removePlayer(player);
        }

        player.setQueue(this.name);

        player.handle().sendMessage(Messages.deserialize(Messages.INFO_JOINED_QUEUE, "queue", this.name));

        return true;
    }

    /**
     * Removes a player from the Queue
     * @param player the GamePlayer to remove
     * @return <i>true</i> if the player was removed successfully, false otherwise
     */
    public boolean removePlayer(GamePlayer player) {
        if (!players.contains(player))
            return false;

        players.remove(player);
        player.setQueue("");
        player.handle().sendMessage(Messages.deserialize(Messages.INFO_LEFT_QUEUE, "queue", this.name));

        return true;
    }

    public void popWithTag(String tag) {
        Iterator<GamePlayer> iterator = players.iterator();

        while (iterator.hasNext()) {
            GamePlayer gamePlayer = iterator.next();

            gamePlayer.setQueue("");

            if (gamePlayer.exists())
                gamePlayer.handle().addScoreboardTag(tag);

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
