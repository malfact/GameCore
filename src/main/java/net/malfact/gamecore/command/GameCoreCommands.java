package net.malfact.gamecore.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.Brigadier;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.ExecutorType;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.Messages;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.LuaGameBuilder;
import net.malfact.gamecore.script.LuaScript;
import net.malfact.gamecore.team.GameTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.LuaConstant;
import org.luaj.vm2.lib.jse.coercion.CoerceJavaToLua;

import java.util.ArrayList;
import java.util.List;

public final class GameCoreCommands {

    public static void register(JavaPlugin plugin) {

        new CommandAPICommand("game")
            .withSubcommand(new CommandAPICommand("join")
                .withArguments(new StringArgument("game"))
                .executesPlayer((sender, args) -> {
                    String gameName = args.getUnchecked("game");
                    Game game = GameCore.getGameManager().getGame(gameName);
                    if (game == null)
                        return;
                    game.joinGame(sender);
                })
            )
            .register(plugin);

        /* "gamecore" Command */
        new CommandAPICommand("gamecore")
            .withAliases("gcore","gc")
            .withSubcommand(new CommandAPICommand("reload-config")
                .withPermission("gamecore.reload")
                .executes((sender, args) -> {
                    GameCore.ReloadConfig();
                    sender.sendMessage(Messages.get("CONFIG_RELOADED"));
                })
            )
            .withSubcommand(new CommandAPICommand("save")
                .withPermission("gamecore.save")
                .executes((sender, args) -> {
                    GameCore.getQueueManager().save();
                    sender.sendMessage(Messages.get("QUEUE_SAVED"));
                    GameCore.getTeamManager().save();
                    sender.sendMessage(Messages.get("TEAM_SAVED"));
                })
            )
            .withSubcommand(new CommandAPICommand("reload")
                .withArguments(new StringArgument("script"))
                .executes((sender, args) -> {
                    String scriptName = args.getOrDefaultRaw("script", "");
                    LuaScript script = GameCore.getScriptManager().getGameScript(scriptName);
                    if (script == null) {
                        sender.sendMessage("Invalid Script");
                        return;
                    }
                    sender.sendMessage("Reloading " + scriptName);
                    GameCore.getGameManager().unregisterGame(scriptName);
                    GameCore.getScriptManager().reloadGameScript(scriptName);
                    LuaGameBuilder builder = new LuaGameBuilder(script);
                    script.getEnvironment().set("GameBuilder", CoerceJavaToLua.coerce(builder));
                    script.run();
                    script.getEnvironment().set("GameBuilder", LuaConstant.NIL);
                    Game game = builder.build();
                    GameCore.getGameManager().registerGame(game);
                    game.start();
                })
            )
            .register(plugin);

        /* "gamequeue" Command */
        new CommandTree("gamequeue")
            .withAliases("queue","gqueue", "gq")
            .then(new LiteralArgument("add")
                .withPermission("gamecore.queue.add")
                .then(new StringArgument("queue").executes(GameQueueCommand::addQueue))
            )
            .then(new LiteralArgument("remove")
                .withPermission("gamecore.queue.remove")
                .then(Arguments.GameQueue("queue").executes(GameQueueCommand::removeQueue))
            )
            .then(new LiteralArgument("list")
                .withPermission("gamecore.queue.list")
                .executes(GameQueueCommand::listQueue)
                .then(Arguments.GameQueue("queue").executes(GameQueueCommand::listQueue))
            )
            .then(new LiteralArgument("enable")
                .withPermission("gamecore.queue.enable")
                .then(Arguments.GameQueue("queue").executes(GameQueueCommand::enableQueue))
            )
            .then(new LiteralArgument("disable")
                .withPermission("gamecore.queue.enable")
                .then(Arguments.GameQueue("queue").executes(GameQueueCommand::disableQueue))
            )
            .then(new LiteralArgument("join")
                .withPermission("gamecore.queue.join")
                .then(Arguments.GameQueue("queue")
                    .executes(GameQueueCommand::joinQueue, ExecutorType.PLAYER, ExecutorType.PROXY)
                    .then(new EntitySelectorArgument.ManyPlayers("player")
                        .withPermission("gamecore.queue.join.other")
                        .executes(GameQueueCommand::joinQueueOther)
                    )
                )
            )
            .then(new LiteralArgument("leave")
                .withPermission("gamecore.queue.leave")
                .executes(GameQueueCommand::leaveQueue, ExecutorType.PLAYER, ExecutorType.PROXY)
                .then(new EntitySelectorArgument.OnePlayer("player")
                    .withPermission("gamecore.queue.leave.other")
                    .executes(GameQueueCommand::leaveQueueOther)
                )
            )
            .then(new LiteralArgument("empty")
                .withPermission("gamecore.queue.empty")
                .then(Arguments.GameQueue("queue").executes(GameQueueCommand::emptyQueue))
            )
            .then(new LiteralArgument("pop")
                .withPermission("gamecore.queue.pop")
                .then(Arguments.GameQueue("queue")
                    .then(new LiteralArgument("tag")
                        .then(new StringArgument("tag").executes(GameQueueCommand::popQueueTag))
                    )
                    .then(new LiteralArgument("team")
                        .then(Arguments.GameTeam("team")
                            .executes(GameQueueCommand::popQueueTeam)
                            .then(new IntegerArgument("count", 1)
                                .executes(GameQueueCommand::popQueueTeam)
                            )
                        )
                    )
                )
            )
            .register(plugin);

        /* "gameteam" Command */
        new CommandTree("gameteam")
            .withAliases("gteam","gt")
            .then(new LiteralArgument("add")
                .withPermission("gamecore.team.add")
                .then(new StringArgument("team").executes(GameTeamCommand::addTeam))
            )
            .then(new LiteralArgument("remove")
                .withPermission("gamecore.team.remove")
                .then(Arguments.GameTeam("team").executes(GameTeamCommand::removeTeam))
            )
            .then(new LiteralArgument("list")
                .withPermission("gamecore.team.list")
                .executes(GameTeamCommand::listTeam)
                .then(Arguments.GameTeam("team").executes(GameTeamCommand::listTeam))
            )
            .then(new LiteralArgument("join")
                .withPermission("gamecore.team.join")
                .then(Arguments.GameTeam("team")
                    .executes(GameTeamCommand::joinTeam, ExecutorType.PLAYER, ExecutorType.PROXY)
                    .then(new EntitySelectorArgument.ManyPlayers("player")
                        .withPermission("gamecore.team.join.other")
                        .executes(GameTeamCommand::joinTeamOther)
                    )
                )
            )
            .then(new LiteralArgument("leave")
                .withPermission("gamecore.team.leave")
                .executes(GameTeamCommand::leaveTeam, ExecutorType.PLAYER, ExecutorType.PROXY)
                .then(new EntitySelectorArgument.OnePlayer("player")
                    .withPermission("gamecore.team.leave.other")
                    .executes(GameTeamCommand::leaveTeamOther)
                )
            )
            .then(new LiteralArgument("empty")
                .withPermission("gamecore.team.empty")
                .then(Arguments.GameTeam("team").executes(GameTeamCommand::emptyTeam))
            )
            .then(new LiteralArgument("modify")
                .withPermission("gamecore.team.modify")
                .then(Arguments.GameTeam("team")
                    .then(new LiteralArgument("spawn")
                        .then(new MultiLiteralArgument("value","RESET","HERE")
                            .executesNative(GameTeamCommand::setTeamSpawn)
                        )
                    )
                    .then(new LiteralArgument("exit")
                        .then(new MultiLiteralArgument("value","RESET","HERE")
                            .executesNative(GameTeamCommand::setTeamExit)
                        )
                    )
                    .then(new LiteralArgument("leaveOnDeath")
                        .then(new BooleanArgument("value")
                            .executes(GameTeamCommand::setTeamLeaveOnDeath)
                        )
                    )
                )
            )
            .register(plugin);

        registerSupportFunctions();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void registerSupportFunctions() {

        LiteralCommandNode ifGameTeam = Brigadier.fromLiteralArgument(new LiteralArgument("gameteam")).build();
        LiteralCommandNode unlessGameTeam = Brigadier.fromLiteralArgument(new LiteralArgument("gameteam")).build();

        Argument<Player> playerArgument = new EntitySelectorArgument.OnePlayer("player");
        Argument<GameTeam> teamArgument = Arguments.GameTeam("team");

        List<Argument> arguments = new ArrayList<>();
        arguments.add(playerArgument);
        arguments.add(teamArgument);

        ArgumentBuilder ifPlayer = Brigadier.fromArgument(playerArgument);
        ArgumentBuilder ifTeam = Brigadier.fromArgument(teamArgument)
            .fork(
                Brigadier.getRootNode().getChild("execute"),
                Brigadier.fromPredicate((s, args) -> GameTeamCommand.executeIfGameTeam((CommandSender) s, args), arguments)
            );

        ArgumentBuilder unlessPlayer = Brigadier.fromArgument(playerArgument);
        ArgumentBuilder unlessTeam = Brigadier.fromArgument(teamArgument)
            .fork(
                Brigadier.getRootNode().getChild("execute"),
                Brigadier.fromPredicate((s, args) -> !GameTeamCommand.executeIfGameTeam((CommandSender) s, args), arguments)
            );

        ifGameTeam.addChild(ifPlayer.then(ifTeam).build());
        unlessGameTeam.addChild(unlessPlayer.then(unlessTeam).build());


        Brigadier.getRootNode().getChild("execute").getChild("if").addChild(ifGameTeam);
        Brigadier.getRootNode().getChild("execute").getChild("unless").addChild(unlessGameTeam);
    }
}
