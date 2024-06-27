package net.malfact.gamecore.api.event;

import com.destroystokyo.paper.event.player.*;
import io.papermc.paper.event.player.*;
import net.malfact.gamecore.api.event.parser.PlayerParser;
import net.malfact.gamecore.game.LuaGame;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.*;
import org.luaj.vm2.LuaTable;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class EventRegistry {

    private static final Map<String, Entry> entries = new HashMap<>();

    static {
        // Bukkit::PlayerEvent
        new Entry("player_bed_enter",           PlayerBedEnterEvent.class,          PlayerParser::onBedEnter);
        new Entry("player_bed_leave",           PlayerBedLeaveEvent.class,          PlayerParser::onBedLeave);
        new Entry("player_bucket_empty",        PlayerBucketEmptyEvent.class,       PlayerParser::onBucketEmpty);
        new Entry("player_bucket_entity",       PlayerBucketEntityEvent.class,      PlayerParser::onBucketEntity);
        new Entry("player_bucket_fill",         PlayerBucketFillEvent.class,        PlayerParser::onBucketFill);
        new Entry("player_changed_world",       PlayerChangedWorldEvent.class,      PlayerParser::onChangedWorld);
        new Entry("player_drop_item",           PlayerDropItemEvent.class,          PlayerParser::onDropItem);
        new Entry("player_edit_book",           PlayerEditBookEvent.class,          PlayerParser::onEditBook);
        new Entry("player_egg_throw",           PlayerEggThrowEvent.class,          PlayerParser::onEggThrow);
        new Entry("player_exp_change",          PlayerExpChangeEvent.class,         PlayerParser::onExpChange);
        new Entry("player_exp_cooldown_change", PlayerExpCooldownChangeEvent.class, PlayerParser::onExpCooldownChange);
        new Entry("player_fish",                PlayerFishEvent.class,              PlayerParser::onFishEvent);
        new Entry("player_gamemode_change",     PlayerGameModeChangeEvent.class,    PlayerParser::onGameModeChange);
        new Entry("player_harvest_block",       PlayerHarvestBlockEvent.class,      PlayerParser::onHarvestBlock);
        new Entry("player_interact_entity",     PlayerInteractEntityEvent.class,    PlayerParser::onInteractEntity);
        new Entry("player_interact_at_entity",  PlayerInteractAtEntityEvent.class,  PlayerParser::onInteractAtEntity);
        new Entry("player_interact",            PlayerInteractEvent.class,          PlayerParser::onInteract);
        new Entry("player_item_break",          PlayerItemBreakEvent.class,         PlayerParser::onItemBreak);
        new Entry("player_item_damage",         PlayerItemDamageEvent.class,        PlayerParser::onItemDamage);
        new Entry("player_item_held",           PlayerItemHeldEvent.class,          PlayerParser::onItemHeld);
        new Entry("player_item_mend",           PlayerItemMendEvent.class,          PlayerParser::onItemMend);
        new Entry("player_join",                PlayerJoinEvent.class,              PlayerParser::onJoin);
        new Entry("player_level_change",        PlayerLevelChangeEvent.class,       PlayerParser::onLevelChange);
        new Entry("player_attempt_pickup_item", PlayerAttemptPickupItemEvent.class, PlayerParser::onAttemptPickupItem);
        new Entry("player_pickup_arrow",        PlayerPickupArrowEvent.class,       PlayerParser::onPickupArrow);
        new Entry("player_quit",                PlayerQuitEvent.class,              PlayerParser::onQuit);
        new Entry("player_respawn",             PlayerRespawnEvent.class,           PlayerParser::onRespawn);
        new Entry("player_riptide",             PlayerRiptideEvent.class,           PlayerParser::onRiptide);
        new Entry("player_shear_entity",        PlayerShearEntityEvent.class,       PlayerParser::onShearEntity);
        new Entry("player_swap_hand_items",     PlayerSwapHandItemsEvent.class,     PlayerParser::onSwapHand);
        new Entry("player_teleport",            PlayerTeleportEvent.class,          PlayerParser::onTeleport);
        new Entry("player_toggle_flight",       PlayerToggleFlightEvent.class,      PlayerParser::onToggleFlight);
        new Entry("player_toggle_sneak",        PlayerToggleSneakEvent.class,       PlayerParser::onToggleSneak);
        new Entry("player_toggle_sprint",       PlayerToggleSprintEvent.class,      PlayerParser::onToggleSprint);
        new Entry("player_unleash_entity",      PlayerLeashEntityEvent.class,       PlayerParser::onLeashEntity);
        new Entry("player_unleash_entity",      PlayerUnleashEntityEvent.class,     PlayerParser::onUnleashEntity);
        new Entry("player_velocity",            PlayerVelocityEvent.class,          PlayerParser::onVelocity);

        // SpigotMC::PlayerEvent
        new Entry("player_spawn_location",      PlayerSpawnLocationEvent.class,     PlayerParser::onSpawnLocation);

        //DestroysTokyo::PlayerEvent
        new Entry("player_armor_change",                    PlayerArmorChangeEvent.class,               PlayerParser::onPlayerArmorChange);
        new Entry("player_attack_entity_cooldown_reset",    PlayerAttackEntityCooldownResetEvent.class, PlayerParser::onPlayerAttackEntityCooldownReset);
        new Entry("player_elytra_boost",                    PlayerElytraBoostEvent.class,               PlayerParser::onPlayerElytraBoost);
        new Entry("player_jump_event",                      PlayerJumpEvent.class,                      PlayerParser::onPlayerJump);
        new Entry("player_launch_projectile",               PlayerLaunchProjectileEvent.class,          PlayerParser::onPlayerLaunchProjectile);
        new Entry("player_pickup_experience",               PlayerPickupExperienceEvent.class,          PlayerParser::onPlayerPickupExperience);
        new Entry("player_post_respawn",                    PlayerPostRespawnEvent.class,               PlayerParser::onPlayerPostRespawn);
        new Entry("player_ready_arrow",                     PlayerReadyArrowEvent.class,                PlayerParser::onPlayerReadyArrow);
        new Entry("player_set_spawn",                       PlayerSetSpawnEvent.class,                  PlayerParser::onPlayerSetSpawn);
        new Entry("player_start_spectating_entity",         PlayerStartSpectatingEntityEvent.class,     PlayerParser::onPlayerStartSpectatingEntity);
        new Entry("player_stop_spectating_entity",          PlayerStopSpectatingEntityEvent.class,      PlayerParser::onPlayerStopSpectatingEntity);
        new Entry("player_teleport_end_gateway",            PlayerTeleportEndGatewayEvent.class,        PlayerParser::onPlayerTeleportEndGateway);
        new Entry("player_use_unknown_entity",              PlayerUseUnknownEntityEvent.class,          PlayerParser::onPlayerUseUnknownEntity);

        //PaperMC::PlayerEvent
        new Entry("player_arm_swing",                   PlayerArmSwingEvent.class,                  PlayerParser::onPlayerArmSwing);
        new Entry("player_bed_fail_enter",              PlayerBedFailEnterEvent.class,              PlayerParser::onPlayerBedFailEnter);
        new Entry("player_change_beacon_effect",        PlayerChangeBeaconEffectEvent.class,        PlayerParser::onPlayerChangeBeaconEffect);
        new Entry("player_deep_sleep",                  PlayerDeepSleepEvent.class,                 PlayerParser::onPlayerDeepSleep);
        new Entry("player_fail_move",                   PlayerFailMoveEvent.class,                  PlayerParser::onPlayerFailMove);
        new Entry("player_flower_pot_manipulate",       PlayerFlowerPotManipulateEvent.class,       PlayerParser::onPlayerFlowerPotManipulate);
        new Entry("player_inventory_slot_change",       PlayerInventorySlotChangeEvent.class,       PlayerParser::onPlayerInventorySlotChange);
        new Entry("player_item_cooldown",               PlayerItemCooldownEvent.class,              PlayerParser::onPlayerItemCooldown);
        new Entry("player_item_frame_change",           PlayerItemFrameChangeEvent.class,           PlayerParser::onPlayerItemFrameChange);
        new Entry("player_lectern_page_change",         PlayerLecternPageChangeEvent.class,         PlayerParser::onPlayerLecternPageChange);
        new Entry("player_loom_pattern_select",         PlayerLoomPatternSelectEvent.class,         PlayerParser::onPlayerLoomPatternSelect);
        new Entry("player_name_entity",                 PlayerNameEntityEvent.class,                PlayerParser::onPlayerNameEntity);
        new Entry("player_open_sign",                   PlayerOpenSignEvent.class,                  PlayerParser::onPlayerOpenSign);
        new Entry("player_pick_item",                   PlayerPickItemEvent.class,                  PlayerParser::onPlayerPickItem);
        new Entry("player_purchase",                    PlayerPurchaseEvent.class,                  PlayerParser::onPlayerPurchase);
        new Entry("player_shield_disable",              PlayerShieldDisableEvent.class,             PlayerParser::onPlayerShieldDisable);
        new Entry("player_stonecutter_recipe_select",   PlayerStonecutterRecipeSelectEvent.class,   PlayerParser::onPlayerStonecutterRecipeSelect);
        new Entry("player_stop_using_item",             PlayerStopUsingItemEvent.class,             PlayerParser::onPlayerStopUsingItem);
        new Entry("player_trade",                       PlayerTradeEvent.class,                     PlayerParser::onPlayerTrade);
        new Entry("player_pre_attack_entity",           PrePlayerAttackEntityEvent.class,           PlayerParser::onPrePlayerAttackEntity);
    }

    public static Entry getEvent(String key) {
        return entries.get(key);
    }

    public static class Entry {
        public final String key;
        public final Class<? extends Event> eventClass;
        public final BiConsumer<LuaTable, ?> parser;

        private final List<LuaGame> listeners;

        public <T extends Event> Entry(String key, Class<T> eventClass, BiConsumer<LuaTable, T> parser) {
            this.key = key;
            this.eventClass = eventClass;
            this.parser = parser;

            listeners = new ArrayList<>();

            entries.put(key, this);
        }

        public void registerListener(LuaGame game) {
            listeners.add(game);

            // ToDo Forward listener to game
        }

        public void unregisterListener(LuaGame game) {
            listeners.remove(game);
        }
    }
}
