package net.malfact.gamecore.compat.worldguard;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;

import java.util.Set;

public class GameRegionHandler extends Handler {

    public static Factory FACTORY = new Factory();

    public static class Factory extends Handler.Factory<GameRegionHandler> {

        @Override
        public GameRegionHandler create(Session session) {
            return new GameRegionHandler(session);
        }
    }

    private GameRegion lastRegion;

    protected GameRegionHandler(Session session) {
        super(session);
    }

    @Override
    public void initialize(LocalPlayer player, Location current, ApplicableRegionSet set) {
        lastRegion = GameRegion.get(player, set);

        if (lastRegion != null)
            lastRegion.onEnter(player);
    }

    @Override
    public void uninitialize(LocalPlayer player, Location current, ApplicableRegionSet set) {
        if (lastRegion != null)
            lastRegion.onExit(player);

        lastRegion = null;
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        if (entered.isEmpty() && exited.isEmpty() && from.getExtent().equals(to.getExtent()))
            return true;

        GameRegion currentRegion = GameRegion.get(player, toSet);
        // No Game Regions defined
        if (lastRegion == null && currentRegion == null)
            return true;

        boolean allowed = true;
        boolean sameRegion = false;

        if (lastRegion != null && currentRegion != null)
            sameRegion = lastRegion.equals(currentRegion);

        if (lastRegion != null && !sameRegion) {
            lastRegion.onExit(player);
        }

        if (currentRegion != null) {
            if (!sameRegion)
                currentRegion.onEnter(player);
            // onEnter (potentially) forces the player to join the game
            // entry check (could) require the player be in the game
            allowed = currentRegion.checkEntry(player);

            if (allowed)
                currentRegion.onTrigger(player);
        }

        if (allowed)
            lastRegion = currentRegion;

        return allowed;
    }
}
