package net.malfact.gamecore.commands;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.util.Messages;

@SuppressWarnings("UnstableApiUsage")
public class GameCoreCommand {

    public static void Register(Commands commands) {
        commands.register(
            Commands.literal("gamecore")
                .then(Commands.literal("reload-config")
                    .executes(context -> {
                        context.getSource().getSender().sendMessage(Messages.deserialize(Messages.INFO_RELOAD_CONFIG));
                        GameCore.ReloadConfig();
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .build()
        );
    }
}
