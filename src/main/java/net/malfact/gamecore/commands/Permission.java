package net.malfact.gamecore.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class Permission {

    public static Predicate<CommandSourceStack> playerHas(String permission) {
        return source -> !(source.getSender() instanceof Player) || source.getSender().hasPermission(permission);
    }
}
