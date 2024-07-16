package net.malfact.gamecore.compat.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import net.malfact.gamecore.GameCore;

import java.util.List;

public final class WorldGuardFlags {

    static StringFlag GAME_REGION;
    static BooleanFlag JOIN_GAME;
    static BooleanFlag LEAVE_GAME;
    static BooleanFlag ALLOW_GAME_ONLY;
    static StringFlag GAME_TRIGGER;

    private WorldGuardFlags(){}

    public static void registerFlags() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StringFlag gameFlag = new StringFlag("game-region");
            BooleanFlag joinFlag = new BooleanFlag("join-game-on-enter");
            BooleanFlag leaveFlag = new BooleanFlag("leave-game-on-exit");
            BooleanFlag allowFlag = new BooleanFlag("allow-game-only");
            StringFlag triggerFlag = new StringFlag("game-trigger");

            registry.registerAll(List.of(gameFlag, joinFlag, leaveFlag, allowFlag, triggerFlag));

            GAME_REGION = gameFlag;
            JOIN_GAME = joinFlag;
            LEAVE_GAME = leaveFlag;
            ALLOW_GAME_ONLY = allowFlag;
            GAME_TRIGGER = triggerFlag;

        } catch (FlagConflictException e) {
            GameCore.logger().error(e.getMessage());
        }
    }
}
