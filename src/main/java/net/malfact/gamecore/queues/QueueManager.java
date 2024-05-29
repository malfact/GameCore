package net.malfact.gamecore.queues;

import com.google.gson.reflect.TypeToken;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.GameManager;
import net.malfact.gamecore.util.Json;

import java.util.*;

public class QueueManager extends GameManager {
    private final Map<String, GameQueue> QUEUES = new HashMap<>();

    public QueueManager(GameCore plugin) {
        super(plugin);
    }

    /**
     * Creates a Queue referenced by name
     * @param name the name of the Queue
     * @return the Queue that was created,
     * <i>null</i> if a queue by that name already exists or the name is invalid.
     */
    public GameQueue create(String name) {
        if (name == null || name.isEmpty() || QUEUES.containsKey(name))
            return null;

        GameQueue queue = new GameQueue(name);
        QUEUES.put(name, queue);

        return queue;
    }

    /**
     * Deletes a Queue by its name
     * @param name the name of the Queue
     * @return <i>true</i> if the Queue was deleted, <i>false</i> otherwise
     */
    public boolean remove(String name) {
        if (name == null || name.isEmpty())
            return false;

        GameQueue deleted = QUEUES.remove(name);

        // Safely cleans up deleted queues
        if (deleted != null)
            deleted.clean();

        return deleted != null;
    }

    /**
     * Gets a Queue by its name
     * @param name the name of the Queue
     * @return the Queue, otherwise <i>null<i/>.
     */
    public GameQueue getQueue(String name) {
        return QUEUES.get(name);
    }

    /**
     * Gets if the Queue with name exists
     * @param name the name of the Queue
     * @return <i>true</i> if it exists, <i>false</i> otherwise
     */
    public boolean hasQueue(String name) {
        return QUEUES.containsKey(name);
    }

    /**
     * Gets all currently registered Queues
     * @return a set containing all registered Queues
     */
    public Set<GameQueue> getQueues() {
        return new HashSet<>(QUEUES.values());
    }

    /**
     * Gets the names of all currently registered Queues
     * @return an array of Queue names
     */
    public String[] getQueueNames() {
        return QUEUES.keySet().toArray(new String[0]);
    }

    /**
     * Cleans all registered Queues in preparation for shutdown
     */
    public void clean() {
        for (GameQueue queue : QUEUES.values()) {
            queue.clean();
        }
    }

    /**
     * Saves Queues to 'queues.json'
     */
    @Override
    public void save() {
        List<QueueData> data = new ArrayList<>();

        for (GameQueue queue : QUEUES.values()) {
            data.add(queue.getDataObject());
        }

        Json.write(plugin, "data/queues", data);
        plugin.logInfo("Saved Queues to queues.json");
    }

    /**
     * Loads Queues from 'queues.json'
     */
    @Override
    public void load() {
        List<QueueData> data = Json.read(plugin, "data/queues", new TypeToken<List<QueueData>>() {}.getType());
        if (data == null || data.isEmpty())
            return;

        for (QueueData queueData : data) {
            if (QUEUES.containsKey(queueData.name)) {
                QUEUES.get(queueData.name).setEnabled(queueData.enabled);
            } else {
                GameQueue queue = new GameQueue(queueData.name);
                queue.setEnabled(queueData.enabled);
                QUEUES.put(queueData.name, queue);
            }
        }
        plugin.logInfo("Loaded Queues from 'queues.json'");
    }
}
