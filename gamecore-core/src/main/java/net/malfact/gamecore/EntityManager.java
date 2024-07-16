package net.malfact.gamecore;

import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.util.UUIDDataType;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;

public final class EntityManager implements Listener {

    private static final NamespacedKey GAME_UUID_KEY = new NamespacedKey(GameCore.instance(),"game_uuid");
    private static final NamespacedKey ENTITY_UUID_KEY = new NamespacedKey(GameCore.instance(),"entity_uuid");
    private static final NamespacedKey HAS_SPAWNED_KEY = new NamespacedKey(GameCore.instance(),"entity_has_spawned");
    public static final UUIDDataType UUID_DATA_TYPE = new UUIDDataType();


    EntityManager(){}

    public <T extends Entity> T createEntity(Game game, Location location, Class<T> entityClass) {
        T entity = location.getWorld().createEntity(location, entityClass);
//        var data = entity.getPersistentDataContainer();

//        UUID uuid = UUID.randomUUID();
//        data.set(GAME_UUID_KEY, UUID_DATA_TYPE, game.getUniqueId());
//        data.set(ENTITY_UUID_KEY, UUID_DATA_TYPE, UUID.randomUUID());
//        data.set(HAS_SPAWNED_KEY, PersistentDataType.BOOLEAN, false);

//        GameCore.logger().info("Created entity {}<{}> for game {}<{}> (Entity UUID: {})",
//            entity.getType(), uuid, game.getName(), game.getUniqueId(), entity.getUniqueId());

        return entity;
    }

//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onEntitySpawn(EntitySpawnEvent event) {
//
//    }
//
//    @EventHandler
//    private void onEntityAddToWorld(EntityAddToWorldEvent event) {
//        Entity entity = event.getEntity();
//        GameCore.logger().info("Entity {{}}{} added to world at {}", entity.getUniqueId(), entity.getType().getKey().asMinimalString(), entity.getLocation());
//    }
//
//    @EventHandler
//    private void onEntityRemoveFromWorld(EntityRemoveFromWorldEvent event) {
//        Entity entity = event.getEntity();
//        GameCore.logger().info("Entity {{}}{} removed from world at {}", entity.getUniqueId(), entity.getType().getKey().asMinimalString(), entity.getLocation());
//    }
}
