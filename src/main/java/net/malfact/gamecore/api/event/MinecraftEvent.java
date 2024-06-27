package net.malfact.gamecore.api.event;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.*;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class MinecraftEvent {

    private static final Map<String, EventType<?>> NAME_EVENTS = new HashMap<>();
    private static final Map<Class<? extends Event>, EventType<?>> CLASS_EVENTS = new HashMap<>();

    // Bukkit::Player Events
    public static final EventType<?> PLAYER_BED_ENTER =     registerEvent("player_bed_enter",     PlayerBedEnterEvent.class);
    public static final EventType<?> PLAYER_BED_LEAVE =     registerEvent("player_bed_leave",     PlayerBedLeaveEvent.class);
    public static final EventType<?> PLAYER_BUCKET_EMPTY =  registerEvent("player_bucket_empty",  PlayerBucketEmptyEvent.class);
    public static final EventType<?> PLAYER_BUCKET_ENTITY = registerEvent("player_bucket_entity", PlayerBucketEntityEvent.class);
    public static final EventType<?> PLAYER_BUCKET_FILL =   registerEvent("player_bucket_fill",   PlayerBucketFillEvent.class);
    public static final EventType<?> PLAYER_DROP_ITEM =     registerEvent("player_drop_item",     PlayerDropItemEvent.class);
    public static final EventType<?> PLAYER_EGG_THROW =     registerEvent("player_egg_throw",     PlayerEggThrowEvent.class);
    public static final EventType<?> PLAYER_FISH =          registerEvent("player_fish",          PlayerFishEvent.class);
//    public static final EventType<?> PLAYER_HARVEST_BLOCK;
//    public static final EventType<?> PLAYER_INTERACT_AT_ENTITY;
//    public static final EventType<?> PLAYER_INTERACT_ENTITY;
    public static final EventType<?> PLAYER_INTERACT =      registerEvent("player_interact",      PlayerInteractEvent.class);
//    public static final EventType<?> PLAYER_ITEM_BREAK;
//    public static final EventType<?> PLAYER_ITEM_CONSUME;
//    public static final EventType<?> PLAYER_ITEM_DAMAGE;
//    public static final EventType<?> PLAYER_ITEM_HELD;
//    public static final EventType<?> PLAYER_ITEM_MEND;
//    public static final EventType<?> PLAYER_JOIN;
//    public static final EventType<?> PLAYER_QUIT;
//    public static final EventType<?> PLAYER_SHEAR_ENTITY;
//    public static final EventType<?> PLAYER_SWAP_HAND_ITEMS;
//    public static final EventType<?> PLAYER_TELEPORT;
//    public static final EventType<?> PLAYER_TOGGLE_FLIGHT;
//    public static final EventType<?> PLAYER_TOGGLE_SNEAK;
//    public static final EventType<?> PLAYER_TOGGLE_SPRINT;
//    public static final EventType<?> PLAYER_UNLEASH_ENTITY;
//    public static final EventType<?> PLAYER_VELOCITY;

    // Spigot::Player Events
//    public static final EventType<?> PLAYER_SPAWN_LOCATION;

    // destroystokyo.Paper::Player Events
//    public static final EventType<?> PLAYER_ARMOR_CHANGE;
//    public static final EventType<?> PLAYER_ELYTRA_BOOST;
    public static final EventType<?> PLAYER_JUMP =          registerEvent("player_jump", PlayerJumpEvent.class);
//    public static final EventType<?> PLAYER_LAUNCH_PROJECTILE;
//    public static final EventType<?> PLAYER_READY_ARROW;

    // papermc.Paper::Player Events
//    public static final EventType<?> PLAYER_ARM_SWING;
//    public static final EventType<?> PLAYER_FLOWER_POT_MANIPULATE;
//    public static final EventType<?> PLAYER_INVENTORY_SLOT_CHANGE;
//    public static final EventType<?> PLAYER_ITEM_COOLDOWN;
//    public static final EventType<?> PLAYER_ITEM_FRAME_CHANGE;
//    public static final EventType<?> PLAYER_LECTERN_PAGE_CHANGE;
//    public static final EventType<?> PLAYER_NAME_ENTITY;
//    public static final EventType<?> PLAYER_OPEN_SIGN;
//    public static final EventType<?> PLAYER_PICK_ITEM;
//    public static final EventType<?> PLAYER_PURCHASE_EVENT;
//    public static final EventType<?> PLAYER_SHIELD_DISABLE;
//    public static final EventType<?> PLAYER_STOP_USING_ITEM;
//    public static final EventType<?> PLAYER_TRADE;
//    public static final EventType<?> PLAYER_PRE_ATTACK_ENTITY;

    public static <E extends Event> EventType<E> registerEvent(String name, Class<E> clazz) {

        EventType<E> e;
        try {
            var a = clazz.getMethod("getHandlerList");
            HandlerList b = (HandlerList) a.invoke(null);
            e = new EventType<>(clazz, () -> b);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        NAME_EVENTS.put(name, e);
        CLASS_EVENTS.put(clazz, e);
        return e;
    }

    public static EventType<?> get(String name) {
        return NAME_EVENTS.get(name.toLowerCase(Locale.ROOT));
    }

    public static EventType<? extends Event> get(Class<? extends Event> clazz) {
        return CLASS_EVENTS.get(clazz);
    }

    public static class EventType<E extends Event> {

        public final Class<E> eventClass;
        public final Supplier<HandlerList> handlerListSupplier;

        private EventType(Class<E> eventClass, Supplier<HandlerList> handlerListSupplier) {
            this.eventClass = eventClass;
            this.handlerListSupplier = handlerListSupplier;
        }

        public HandlerList getHandlers() {
            return this.handlerListSupplier.get();
        }
    }

    public static <T extends Event> LuaValue parseEvent(T event) {
        return null;
    }
}
