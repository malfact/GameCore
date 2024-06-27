package net.malfact.gamecore.api.event.parser;

import com.destroystokyo.paper.event.player.*;
import io.papermc.paper.event.player.*;
import net.malfact.gamecore.api.entity.PlayerLib;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.*;
import org.luaj.vm2.LuaTable;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public final class PlayerParser {


    public static void onPlayer(LuaTable table, PlayerEvent event) {
        table.set("player", PlayerLib.toUserdata(event.getPlayer()));
    }

    public static void onBedEnter(LuaTable table, PlayerBedEnterEvent event) {
        onPlayer(table, event);
        // ToDo event.getBed()
        table.set("useBed", event.useBed().toString());
    }

    public static void onBedLeave(LuaTable table, PlayerBedLeaveEvent event) {
        onPlayer(table, event);
        // ToDo event.GetBed()
    }

    public static void onBucketEntity(LuaTable table, PlayerBucketEntityEvent event) {
        onPlayer(table, event);
    }

    public static void onBucket(LuaTable table, PlayerBucketEvent event) {
        onPlayer(table, event);
        table.get()
    }

    public static void onBucketEmpty(LuaTable table, PlayerBucketEmptyEvent event) {
        onBucket(table, event);
    }

    public static void onBucketFill(LuaTable table, PlayerBucketFillEvent event) {
        onBucket(table, event);
    }

    public static void onChangedWorld(LuaTable table, PlayerChangedWorldEvent playerChangedWorldEvent) {
    }

    public static void onDropItem(LuaTable table, PlayerDropItemEvent playerDropItemEvent) {
    }

    public static void onEditBook(LuaTable table, PlayerEditBookEvent playerEditBookEvent) {
    }

    public static void onEggThrow(LuaTable table, PlayerEggThrowEvent playerEggThrowEvent) {
    }

    public static void onExpChange(LuaTable table, PlayerExpChangeEvent playerExpChangeEvent) {
    }

    public static void onExpCooldownChange(LuaTable table, PlayerExpCooldownChangeEvent playerExpCooldownChangeEvent) {
    }

    public static void onFishEvent(LuaTable table, PlayerFishEvent playerFishEvent) {
    }

    public static void onGameModeChange(LuaTable table, PlayerGameModeChangeEvent playerGameModeChangeEvent) {
    }

    public static void onHarvestBlock(LuaTable table, PlayerHarvestBlockEvent playerHarvestBlockEvent) {
    }

    public static void onInteractEntity(LuaTable table, PlayerInteractEntityEvent playerInteractEntityEvent) {
    }

    public static void onInteractAtEntity(LuaTable table, PlayerInteractAtEntityEvent playerInteractAtEntityEvent) {
    }

    public static void onInteract(LuaTable table, PlayerInteractEvent playerInteractEvent) {
    }

    public static void onItemBreak(LuaTable table, PlayerItemBreakEvent playerItemBreakEvent) {
    }

    public static void onItemDamage(LuaTable table, PlayerItemDamageEvent playerItemDamageEvent) {
    }

    public static void onItemHeld(LuaTable table, PlayerItemHeldEvent playerItemHeldEvent) {
    }

    public static void onItemMend(LuaTable table, PlayerItemMendEvent playerItemMendEvent) {
    }

    public static void onJoin(LuaTable table, PlayerJoinEvent event) {
    }

    public static void onLevelChange(LuaTable table, PlayerLevelChangeEvent playerLevelChangeEvent) {
    }

    public static void onAttemptPickupItem(LuaTable table, PlayerAttemptPickupItemEvent playerAttemptPickupItemEvent) {
    }

    public static void onPickupArrow(LuaTable table, PlayerPickupArrowEvent playerPickupArrowEvent) {
    }

    public static void onQuit(LuaTable table, PlayerQuitEvent playerQuitEvent) {
    }

    public static void onRespawn(LuaTable table, PlayerRespawnEvent event) {
    }

    public static void onRiptide(LuaTable table, PlayerRiptideEvent playerRiptideEvent) {
    }

    public static void onShearEntity(LuaTable table, PlayerShearEntityEvent playerShearEntityEvent) {
    }

    public static void onSwapHand(LuaTable table, PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
    }

    public static void onTeleport(LuaTable table, PlayerTeleportEvent playerTeleportEvent) {
    }

    public static void onToggleFlight(LuaTable table, PlayerToggleFlightEvent playerToggleFlightEvent) {
    }

    public static void onToggleSneak(LuaTable table, PlayerToggleSneakEvent playerToggleSneakEvent) {
    }

    public static void onToggleSprint(LuaTable table, PlayerToggleSprintEvent playerToggleSprintEvent) {
    }

    public static void onLeashEntity(LuaTable table, PlayerLeashEntityEvent playerLeashEntityEvent) {
    }

    public static void onUnleashEntity(LuaTable table, PlayerUnleashEntityEvent playerUnleashEntityEvent) {
    }

    public static void onVelocity(LuaTable table, PlayerVelocityEvent playerVelocityEvent) {
    }

    // Spigot

    public static void onSpawnLocation(LuaTable table, PlayerSpawnLocationEvent playerSpawnLocationEvent) {
    }

    // DestroysTokyo:Paper

    public static void onPlayerArmorChange(LuaTable table, PlayerArmorChangeEvent playerArmorChangeEvent) {
    }

    public static void onPlayerAttackEntityCooldownReset(LuaTable table, PlayerAttackEntityCooldownResetEvent playerAttackEntityCooldownResetEvent) {
    }

    public static void onPlayerElytraBoost(LuaTable table, PlayerElytraBoostEvent playerElytraBoostEvent) {
    }

    public static void onPlayerJump(LuaTable table, PlayerJumpEvent event) {
    }

    public static void onPlayerLaunchProjectile(LuaTable table, PlayerLaunchProjectileEvent playerLaunchProjectileEvent) {
    }

    public static void onPlayerPickupExperience(LuaTable table, PlayerPickupExperienceEvent playerPickupExperienceEvent) {
    }

    public static void onPlayerPostRespawn(LuaTable table, PlayerPostRespawnEvent playerPostRespawnEvent) {
    }

    public static void onPlayerReadyArrow(LuaTable table, PlayerReadyArrowEvent playerReadyArrowEvent) {
    }

    public static void onPlayerSetSpawn(LuaTable table, PlayerSetSpawnEvent playerSetSpawnEvent) {
    }

    public static void onPlayerStartSpectatingEntity(LuaTable table, PlayerStartSpectatingEntityEvent playerStartSpectatingEntityEvent) {
    }

    public static void onPlayerStopSpectatingEntity(LuaTable table, PlayerStopSpectatingEntityEvent playerStopSpectatingEntityEvent) {
    }

    public static void onPlayerTeleportEndGateway(LuaTable table, PlayerTeleportEndGatewayEvent playerTeleportEndGatewayEvent) {
    }

    public static void onPlayerUseUnknownEntity(LuaTable table, PlayerUseUnknownEntityEvent playerUseUnknownEntityEvent) {
    }

    public static void onPlayerArmSwing(LuaTable table, PlayerArmSwingEvent playerArmSwingEvent) {
    }

    public static void onPlayerBedFailEnter(LuaTable table, PlayerBedFailEnterEvent playerBedFailEnterEvent) {
    }

    public static void onPlayerChangeBeaconEffect(LuaTable table, PlayerChangeBeaconEffectEvent playerChangeBeaconEffectEvent) {
    }

    public static void onPlayerDeepSleep(LuaTable table, PlayerDeepSleepEvent playerDeepSleepEvent) {
    }

    public static void onPlayerFailMove(LuaTable table, PlayerFailMoveEvent playerFailMoveEvent) {
    }

    public static void onPlayerFlowerPotManipulate(LuaTable table, PlayerFlowerPotManipulateEvent playerFlowerPotManipulateEvent) {
    }

    public static void onPlayerInventorySlotChange(LuaTable table, PlayerInventorySlotChangeEvent playerInventorySlotChangeEvent) {
    }

    public static void onPlayerItemCooldown(LuaTable table, PlayerItemCooldownEvent playerItemCooldownEvent) {
    }

    public static void onPlayerItemFrameChange(LuaTable table, PlayerItemFrameChangeEvent playerItemFrameChangeEvent) {
    }

    public static void onPlayerLecternPageChange(LuaTable table, PlayerLecternPageChangeEvent playerLecternPageChangeEvent) {
    }

    public static void onPlayerLoomPatternSelect(LuaTable table, PlayerLoomPatternSelectEvent playerLoomPatternSelectEvent) {
    }

    public static void onPlayerNameEntity(LuaTable table, PlayerNameEntityEvent playerNameEntityEvent) {
    }

    public static void onPlayerOpenSign(LuaTable table, PlayerOpenSignEvent playerOpenSignEvent) {
    }

    public static void onPlayerPickItem(LuaTable table, PlayerPickItemEvent playerPickItemEvent) {
    }

    public static void onPlayerPurchase(LuaTable table, PlayerPurchaseEvent playerPurchaseEvent) {
    }

    public static void onPlayerShieldDisable(LuaTable table, PlayerShieldDisableEvent playerShieldDisableEvent) {
    }

    public static void onPlayerStonecutterRecipeSelect(LuaTable table, PlayerStonecutterRecipeSelectEvent playerStonecutterRecipeSelectEvent) {
    }

    public static void onPlayerStopUsingItem(LuaTable table, PlayerStopUsingItemEvent playerStopUsingItemEvent) {
    }

    public static void onPlayerTrade(LuaTable table, PlayerTradeEvent playerTradeEvent) {
    }

    public static void onPrePlayerAttackEntity(LuaTable table, PrePlayerAttackEntityEvent prePlayerAttackEntityEvent) {
    }
}