package net.malfact.gamecore;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.malfact.gamecore.commands.GameQueueCommand;
import net.malfact.gamecore.commands.GameTeamCommand;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class GameCoreBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();

        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            GameQueueCommand.Register(commands);
            GameTeamCommand.Register(commands);
//            commands.getDispatcher().getRoot().getChildren().forEach(c -> {System.out.println(c.getName() + " || " + c.getUsageText());});
        });
    }
}
