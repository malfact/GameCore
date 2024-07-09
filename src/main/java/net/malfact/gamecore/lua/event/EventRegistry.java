package net.malfact.gamecore.lua.event;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.destroystokyo.paper.event.entity.*;
import com.destroystokyo.paper.event.player.*;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import io.papermc.paper.event.entity.*;
import io.papermc.paper.event.player.*;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.event.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.*;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("unused")
public class EventRegistry {

    private static final Map<String, Map<String, EventEntry>> REGISTRY = new HashMap<>();

    // Game Events
    static {
        // gamecore.event
        registerEvent("game", "onGameStart", GameStartEvent.class);
        registerEvent("game", "onGameStop", GameStopEvent.class);
        registerEvent("game", "onGameTick", GameTickEvent.class);
        registerEvent("game", "onPlayerJoin", PlayerJoinGameEvent.class);
        registerEvent("game", "onPlayerLeave", PlayerLeaveGameEvent.class);
    }

    // Player Events
    static {
        // bukkit.event.player
        registerEvent("player", "onBedEnter", PlayerBedEnterEvent.class);
        registerEvent("player", "onBedLeave", PlayerBedLeaveEvent.class);
        registerEvent("player", "onBucketEmpty", PlayerBucketEmptyEvent.class);
        registerEvent("player", "onBucketEntity", PlayerBucketEntityEvent.class);
        registerEvent("player", "onBucketFill", PlayerBucketFillEvent.class);
        registerEvent("player", "onChangedWorld", PlayerChangedWorldEvent.class);
        registerEvent("player", "onDropItem", PlayerDropItemEvent.class);
        registerEvent("player", "onEditBook", PlayerEditBookEvent.class);
        registerEvent("player", "onEggThrow", PlayerEggThrowEvent.class);
        registerEvent("player", "onExpChange", PlayerExpChangeEvent.class);
        registerEvent("player", "onExpCooldownChange", PlayerExpCooldownChangeEvent.class);
        registerEvent("player", "onFish", PlayerFishEvent.class);
        registerEvent("player", "onGameModeChange", PlayerGameModeChangeEvent.class);
        registerEvent("player", "onInteractAtEntity", PlayerInteractAtEntityEvent.class);
        registerEvent("player", "onInteractEntity", PlayerInteractEntityEvent.class);
        registerEvent("player", "onInteract", PlayerInteractEvent.class);
        registerEvent("player", "onItemBreak", PlayerItemBreakEvent.class);
        registerEvent("player", "onItemConsume", PlayerItemConsumeEvent.class);
        registerEvent("player", "onItemDamage", PlayerItemDamageEvent.class);
        registerEvent("player", "onItemHeld", PlayerItemHeldEvent.class);
        registerEvent("player", "onItemMend", PlayerItemMendEvent.class);
        registerEvent("player", "onJoin", PlayerJoinEvent.class);
        registerEvent("player", "onKick", PlayerKickEvent.class);
        registerEvent("player", "onLevelChange", PlayerLevelChangeEvent.class);
        registerEvent("player", "onMove", PlayerMoveEvent.class);
        registerEvent("player", "onPickupArrow", PlayerPickupArrowEvent.class);
        registerEvent("player", "onPortal", PlayerPortalEvent.class);
        registerEvent("player", "onQuit", PlayerQuitEvent.class);
        registerEvent("player", "onRespawn", PlayerRespawnEvent.class);
        registerEvent("player", "onRiptide", PlayerRiptideEvent.class);
        registerEvent("player", "onShearEntity", PlayerShearEntityEvent.class);
        registerEvent("player", "onSwapHandItems", PlayerSwapHandItemsEvent.class);
        registerEvent("player", "onTakeLecternBook", PlayerTakeLecternBookEvent.class);
        registerEvent("player", "onTeleport", PlayerTeleportEvent.class);
        registerEvent("player", "onToggleFlight", PlayerToggleFlightEvent.class);
        registerEvent("player", "onToggleSneak", PlayerToggleSneakEvent.class);
        registerEvent("player", "onToggleSprint", PlayerToggleSprintEvent.class);
        registerEvent("player", "onUnleashEntity", PlayerUnleashEntityEvent.class);
        registerEvent("player", "onVelocity", PlayerVelocityEvent.class);

        registerEvent("player", "onDeath", PlayerDeathEvent.class);
        registerEvent("player", "onLeashEntity", PlayerLeashEntityEvent.class);

        // spigot.event.player
        registerEvent("player", "onSpawnLocation", PlayerSpawnLocationEvent.class);

        // destroystokyo.paper.event.player
        registerEvent("player", "onArmorChange", PlayerArmorChangeEvent.class);
        registerEvent("player", "onAttackEntityCooldownReset", PlayerAttackEntityCooldownResetEvent.class);
        registerEvent("player", "onElytraBoost", PlayerElytraBoostEvent.class);
        registerEvent("player", "onJump", PlayerJumpEvent.class);
        registerEvent("player", "onLaunchProjectile", PlayerLaunchProjectileEvent.class);
        registerEvent("player", "onPickupExperience", PlayerPickupExperienceEvent.class);
        registerEvent("player", "onPostRespawn", PlayerPostRespawnEvent.class);
        registerEvent("player", "onReadyArrow", PlayerReadyArrowEvent.class);
        registerEvent("player", "onSetSpawn", PlayerSetSpawnEvent.class);
        registerEvent("player", "onStartSpectatingEntity", PlayerStartSpectatingEntityEvent.class);
        registerEvent("player", "onStopSpectatingEntity", PlayerStopSpectatingEntityEvent.class);
        registerEvent("player", "onTeleportEndGateway", PlayerTeleportEndGatewayEvent.class);
        registerEvent("player", "onUseUnknownEntity", PlayerUseUnknownEntityEvent.class);

        // papermc.paper.event.player
        registerEvent("player", "onArmSwing", PlayerArmSwingEvent.class);
        registerEvent("player", "onBedFailEnter", PlayerBedFailEnterEvent.class);
        registerEvent("player", "onChangeBeaconEffect", PlayerChangeBeaconEffectEvent.class);
        registerEvent("player", "onDeepSleep", PlayerDeepSleepEvent.class);
        registerEvent("player", "onFailMove", PlayerFailMoveEvent.class);
        registerEvent("player", "onFlowerPotManipulate", PlayerFlowerPotManipulateEvent.class);
        registerEvent("player", "onInventorySlotChange", PlayerInventorySlotChangeEvent.class);
        registerEvent("player", "onItemCooldown", PlayerItemCooldownEvent.class);
        registerEvent("player", "onItemFrameChange", PlayerItemFrameChangeEvent.class);
        registerEvent("player", "onLecternPageChange", PlayerLecternPageChangeEvent.class);
        registerEvent("player", "onLoomPatternSelect", PlayerLoomPatternSelectEvent.class);
        registerEvent("player", "onNameEntity", PlayerNameEntityEvent.class);
        registerEvent("player", "onOpenSign", PlayerOpenSignEvent.class);
        registerEvent("player", "onPickItem", PlayerPickItemEvent.class);
        registerEvent("player", "onPurchase", PlayerPurchaseEvent.class);
        registerEvent("player", "onShieldDisable", PlayerShieldDisableEvent.class);
        registerEvent("player", "onStonecutterRecipeSelect", PlayerStonecutterRecipeSelectEvent.class);
        registerEvent("player", "onStopUsingItem", PlayerStopUsingItemEvent.class);
        registerEvent("player", "onTrade", PlayerTradeEvent.class);
        registerEvent("player", "onPreAttackEntity", PrePlayerAttackEntityEvent.class);
        registerEvent("player", "onShearBlock", PlayerShearBlockEvent.class);
    }

    // Vehicle Events
    static {
        // bukkit.event.vehicle
        registerEvent("vehicle", "onBlockCollision", VehicleBlockCollisionEvent.class);
        registerEvent("vehicle", "onCreate", VehicleCreateEvent.class);
        registerEvent("vehicle", "onDamage", VehicleDamageEvent.class);
        registerEvent("vehicle", "onDestroy", VehicleDestroyEvent.class);
        registerEvent("vehicle", "onEnter", VehicleEnterEvent.class);
        registerEvent("vehicle", "onExit", VehicleExitEvent.class);
        registerEvent("vehicle", "onMove", VehicleMoveEvent.class);
        registerEvent("vehicle", "onUpdate", VehicleUpdateEvent.class);
    }

    // Entity Events
    static {
        // bukkit.event.entity
        registerEvent("entity", "onAreaEffectCloudApply",       AreaEffectCloudApplyEvent.class);
        registerEvent("entity", "onArrowBodyCountChange",       ArrowBodyCountChangeEvent.class);
        registerEvent("entity", "onBatToggleSleep",             BatToggleSleepEvent.class);
        registerEvent("entity", "onCreatureSpawn",              CreatureSpawnEvent.class);
        registerEvent("entity", "onCreeperPower",               CreeperPowerEvent.class);
        registerEvent("entity", "onEnderDragonChangePhase",     EnderDragonChangePhaseEvent.class);
        registerEvent("entity", "onAirChange",                  EntityAirChangeEvent.class);
        registerEvent("entity", "onBreakDoor",                  EntityBreakDoorEvent.class);
        registerEvent("entity", "onBreed",                      EntityBreedEvent.class);
        registerEvent("entity", "onChangeBlock",                EntityChangeBlockEvent.class);
        registerEvent("entity", "onCombustByBlock",             EntityCombustByBlockEvent.class);
        registerEvent("entity", "onCombustByEntity",            EntityCombustByEntityEvent.class);
        registerEvent("entity", "onCombust",                    EntityCombustEvent.class);
        registerEvent("entity", "onDamageByBlock",              EntityDamageByBlockEvent.class);
        registerEvent("entity", "onDamageByEntity",             EntityDamageByEntityEvent.class);
        registerEvent("entity", "onDamage",                     EntityDamageEvent.class);
        registerEvent("entity", "onDeath",                      EntityDeathEvent.class);
        registerEvent("entity", "onDismount",                   EntityDismountEvent.class);
        registerEvent("entity", "onDropItem",                   EntityDropItemEvent.class);
        registerEvent("entity", "onEnterBlock",                 EntityEnterBlockEvent.class);
        registerEvent("entity", "onEnterLoveMode",              EntityEnterLoveModeEvent.class);
        registerEvent("entity", "onExhaustion",                 EntityExhaustionEvent.class);
        registerEvent("entity", "onExplode",                    EntityExplodeEvent.class);
        registerEvent("entity", "onInteract",                   EntityInteractEvent.class);
        registerEvent("entity", "onMount",                      EntityMountEvent.class);
        registerEvent("entity", "onPickupItem",                 EntityPickupItemEvent.class);
        registerEvent("entity", "onPlace",                      EntityPlaceEvent.class);
        registerEvent("entity", "onPortalEnter",                EntityPortalEnterEvent.class);
        registerEvent("entity", "onPortal",                     EntityPortalEvent.class);
        registerEvent("entity", "onPortalExit",                 EntityPortalExitEvent.class);
        registerEvent("entity", "onPoseChange",                 EntityPoseChangeEvent.class);
        registerEvent("entity", "onPotionEffect",               EntityPotionEffectEvent.class);
        registerEvent("entity", "onRegainHealth",               EntityRegainHealthEvent.class);
        registerEvent("entity", "onResurrect",                  EntityResurrectEvent.class);
        registerEvent("entity", "onShootBow",                   EntityShootBowEvent.class);
        registerEvent("entity", "onSpawn",                      EntitySpawnEvent.class);
        registerEvent("entity", "onSpellCast",                  EntitySpellCastEvent.class);
        registerEvent("entity", "onTame",                       EntityTameEvent.class);
        registerEvent("entity", "onTarget",                     EntityTargetEvent.class);
        registerEvent("entity", "onTeleport",                   EntityTeleportEvent.class);
        registerEvent("entity", "onToggleGlide",                EntityToggleGlideEvent.class);
        registerEvent("entity", "onToggleSwim",                 EntityToggleSwimEvent.class);
        registerEvent("entity", "onTransform",                  EntityTransformEvent.class);
        registerEvent("entity", "onUnleash",                    EntityUnleashEvent.class);
        registerEvent("entity", "onExpBottle",                  ExpBottleEvent.class);
        registerEvent("entity", "onExplosionPrime",             ExplosionPrimeEvent.class);
        registerEvent("entity", "onFireworkExplode",            FireworkExplodeEvent.class);
        registerEvent("entity", "onFoodLevelChange",            FoodLevelChangeEvent.class);
        registerEvent("entity", "onHorseJump",                  HorseJumpEvent.class);
        registerEvent("entity", "onItemDespawn",                ItemDespawnEvent.class);
        registerEvent("entity", "onItemMerge",                  ItemMergeEvent.class);
        registerEvent("entity", "onItemSpawn",                  ItemSpawnEvent.class);
        registerEvent("entity", "onLingeringPotionSplash",      LingeringPotionSplashEvent.class);
        registerEvent("entity", "onPiglinBarter",               PiglinBarterEvent.class);
        registerEvent("entity", "onPigZap",                     PigZapEvent.class);
        registerEvent("entity", "onPigZombieAnger",             PigZombieAngerEvent.class);
        registerEvent("entity", "onPotionSplash",               PotionSplashEvent.class);
        registerEvent("entity", "onProjectileHit",              ProjectileHitEvent.class);
        registerEvent("entity", "onSheepDyeWool",               SheepDyeWoolEvent.class);
        registerEvent("entity", "onSheepRegrowWool",            SheepRegrowWoolEvent.class);
        registerEvent("entity", "onSlimeSplit",                 SlimeSplitEvent.class);
        registerEvent("entity", "onSpawnerSpawn",               SpawnerSpawnEvent.class);
        registerEvent("entity", "onStriderTemperatureChange",   StriderTemperatureChangeEvent.class);
        registerEvent("entity", "onVillagerAcquireTrade",       VillagerAcquireTradeEvent.class);
        registerEvent("entity", "onVillagerCareerChange",       VillagerCareerChangeEvent.class);
        registerEvent("entity", "onVillagerReplenishTrade",     VillagerReplenishTradeEvent.class);

        // destroystokyo.paper.event.entity
        registerEvent("entity", "onCreeperIgnite",              CreeperIgniteEvent.class);
        registerEvent("entity", "onEnderDragonFireballHit",     EnderDragonFireballHitEvent.class);
        registerEvent("entity", "onEnderDragonFlame",           EnderDragonFlameEvent.class);
        registerEvent("entity", "onEnderDragonShootFireball",   EnderDragonShootFireballEvent.class);
        registerEvent("entity", "onEndermanAttackPlayer",       EndermanAttackPlayerEvent.class);
        registerEvent("entity", "onEndermanEscape",             EndermanEscapeEvent.class);
        registerEvent("entity", "onAddToWorld",                 EntityAddToWorldEvent.class);
        registerEvent("entity", "onJump",                       EntityJumpEvent.class);
        registerEvent("entity", "onKnockbackByEntity",          EntityKnockbackByEntityEvent.class);
        registerEvent("entity", "onPathfind",                   EntityPathfindEvent.class);
        registerEvent("entity", "onRemoveFromWorld",            EntityRemoveFromWorldEvent.class);
        registerEvent("entity", "onTeleportEndGateway",         EntityTeleportEndGatewayEvent.class);
        registerEvent("entity", "onZap",                        EntityZapEvent.class);
        registerEvent("entity", "onExperienceOrbMerge",         ExperienceOrbMergeEvent.class);
        registerEvent("entity", "onPhantomPreSpawn",            PhantomPreSpawnEvent.class);
        registerEvent("entity", "onPreCreatureSpawn",           PreCreatureSpawnEvent.class);
        registerEvent("entity", "onSkeletonHorseTrap",          SkeletonHorseTrapEvent.class);
        registerEvent("entity", "onSlimeChangeDirection",       SlimeChangeDirectionEvent.class);
        registerEvent("entity", "onSlimePathfind",              SlimePathfindEvent.class);
        registerEvent("entity", "onSlimeSwim",                  SlimeSwimEvent.class);
        registerEvent("entity", "onSlimeTargetLivingEntity",    SlimeTargetLivingEntityEvent.class);
        registerEvent("entity", "onSlimeWander",                SlimeWanderEvent.class);
        registerEvent("entity", "onThrownEggHatch",             ThrownEggHatchEvent.class);
        registerEvent("entity", "onTurtleGoHome",               TurtleGoHomeEvent.class);
        registerEvent("entity", "onWitchConsumePotion",         WitchConsumePotionEvent.class);
        registerEvent("entity", "onWitchReadyPotion",           WitchReadyPotionEvent.class);
        registerEvent("entity", "onWitchThrowPotion",           WitchThrowPotionEvent.class);

        // papermc.paper.event.entity
        registerEvent("entity", "onElderGuardianAppearance",    ElderGuardianAppearanceEvent.class);
        registerEvent("entity", "onCompostItem",                EntityCompostItemEvent.class);
        registerEvent("entity", "onDamageItem",                 EntityDamageItemEvent.class);
        registerEvent("entity", "onDye",                        EntityDyeEvent.class);
        registerEvent("entity", "onFertilizeEgg",               EntityFertilizeEggEvent.class);
        registerEvent("entity", "onInsideBlock",                EntityInsideBlockEvent.class);
        registerEvent("entity", "onKnockback",                  EntityKnockbackEvent.class);
        registerEvent("entity", "onLoadCrossbow",               EntityLoadCrossbowEvent.class);
        registerEvent("entity", "onMove",                       EntityMoveEvent.class);
        registerEvent("entity", "onPortalReady",                EntityPortalReadyEvent.class);
        registerEvent("entity", "onPushedByEntityAttack",       EntityPushedByEntityAttackEvent.class);
        registerEvent("entity", "onToggleSit",                  EntityToggleSitEvent.class);
        registerEvent("entity", "onPufferFishStateChange",      PufferFishStateChangeEvent.class);
        registerEvent("entity", "onShulkerDuplicate",           ShulkerDuplicateEvent.class);
        registerEvent("entity", "onTameableDeathMessage",       TameableDeathMessageEvent.class);
        registerEvent("entity", "onWardenAngerChange",          WardenAngerChangeEvent.class);
        registerEvent("entity", "onWaterBottleSplash",          WaterBottleSplashEvent.class);
    }

    /**
     * Registers a Bukkit event that can be used with the lua {@link EventLib Event API}.
     * @param type the type key of the event. <i>(i.e. "Player")</i>
     * @param name the name key on the event. <i>(i.e. "onInteract")</i>
     * @param eventClass the class of the event
     */
    public static void registerEvent(String type, String name, Class<? extends Event> eventClass) {
        Map<String, EventEntry> entries = REGISTRY.computeIfAbsent(
            type.toLowerCase(Locale.ROOT),
            k -> new HashMap<>()
        );

        entries.put(name, new EventEntry(name, eventClass));
    }

    public static boolean isTypeValid(String type) {
        return REGISTRY.containsKey(type.toLowerCase(Locale.ROOT));
    }

    public static boolean isEventValid(String type, String name) {
        Map<String, EventEntry> entries = REGISTRY.get(type.toLowerCase(Locale.ROOT));
        if (entries == null)
            return false;

        return entries.containsKey(name);
    }

    public static String[] getTypeNames() {
        return REGISTRY.keySet().toArray(new String[0]);
    }

    public static String[] getEventNames(String type) {
        Map<String, EventEntry> entries = REGISTRY.get(type.toLowerCase(Locale.ROOT));
        if (entries.isEmpty())
            return new String[0];

        return entries.keySet().toArray(new String[0]);
    }

    public static EventEntry getEvent(String type, String name) {
        Map<String, EventEntry> entries = REGISTRY.get(type.toLowerCase(Locale.ROOT));

        if (entries == null)
            return null;

        return entries.get(name);
    }

    public static void unregisterListener(GameListener listener) {
        for (var type : REGISTRY.values()) {
            for (var event : type.values()) {
                event.unregisterListener(listener);
            }
        }
    }

    public static class EventEntry {

        public final String name;
        public final Class<? extends Event> eventClass;
        private final List<GameListener> listeners;

        private EventEntry(String name, Class<? extends Event> eventClass) {
            this.name = name;
            this.eventClass = eventClass;

            listeners = new ArrayList<>();
        }

        public void registerListener(GameListener listener) {
            if (listeners.contains(listener))
                return;

            listeners.add(listener);
            Bukkit.getPluginManager().registerEvent(eventClass, listener, EventPriority.NORMAL, listener, GameCore.getInstance());
            GameCore.logger().debug("Registered Listener for {}", eventClass.getSimpleName());
        }

        public void unregisterListener(GameListener listener) {
            if (!listeners.contains(listener))
                return;

            listeners.remove(listener);

            try {
                var method = eventClass.getMethod("getHandlerList");
                var handlerList = (HandlerList) method.invoke(null);
                handlerList.unregister(listener);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            GameCore.logger().debug("Unregistered Listener for {}", eventClass.getSimpleName());
        }
    }
}