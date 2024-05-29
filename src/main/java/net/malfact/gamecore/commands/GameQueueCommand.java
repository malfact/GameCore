package net.malfact.gamecore.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.players.GamePlayer;
import net.malfact.gamecore.queues.GameQueue;
import net.malfact.gamecore.teams.GameTeam;
import net.malfact.gamecore.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class GameQueueCommand {

    public static void Register(Commands commands) {
        commands.register(Commands.literal("gamequeue")
            .then(Commands.literal("create")
                .requires(Permission.playerHas("gamecore.queue.create"))
                .then(Commands.argument("queue", StringArgumentType.word())
                    .executes(context -> {
                        String queueName = StringArgumentType.getString(context, "queue");
                        if (GameCore.getQueueManager().create(queueName) == null) {
                            getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_COULD_NOT_CREATE_QUEUE, "queue", queueName));
                            return 0;
                        }

                        getSender(context).sendMessage(Messages.deserialize(Messages.INFO_QUEUE_CREATED, "queue", queueName));
                        return Command.SINGLE_SUCCESS;
                    }))
            )
            .then(Commands.literal("delete")
                .requires(Permission.playerHas("gamecore.queue.delete"))
                .then(Commands.argument("queue", StringArgumentType.word())
                    .suggests(SuggestionProviders.QUEUES)
                    .executes(context -> {
                        String queueName = StringArgumentType.getString(context, "queue");
                        if (!GameCore.getQueueManager().remove(queueName)) {
                            getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_QUEUE, "queue", queueName));
                            return 0;
                        }

                        getSender(context).sendMessage(Messages.deserialize(Messages.INFO_QUEUE_REMOVED, "queue", queueName));
                        return Command.SINGLE_SUCCESS;
                    })
                )
            )
            .then(Commands.literal("join")
                .requires(Permission.playerHas("gamecore.queue.join"))
                .then(Commands.argument("queue", StringArgumentType.word())
                    .suggests(SuggestionProviders.QUEUES)
                    .executes(context -> {
                        CommandSender sender = context.getSource().getSender();
                        Entity executor = context.getSource().getExecutor();

                        if (!(executor instanceof Player)) {
                            sender.sendMessage(Messages.deserialize(Messages.ERROR_NOT_PLAYER, "name", (executor == null) ? "NULL" : executor.getName()));
                            return 0;
                        }

                        String queue = StringArgumentType.getString(context, "queue");
                        GameQueue gameQueue = GameCore.getQueueManager().getQueue(queue);

                        if (gameQueue == null) {
                            sender.sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_QUEUE, "queue", queue));
                            return 0;
                        } else if (!gameQueue.getEnabled()) {
                            sender.sendMessage(Messages.deserialize(Messages.ERROR_QUEUE_DISABLED, "queue", queue));
                            return 0;
                        }

                        GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer((Player) executor);
                        gameQueue.addPlayer(gamePlayer);

                        if (!sender.equals(executor)) {
                            sender.sendMessage(
                                Messages.deserialize(
                                    Messages.INFO_PLAYER_JOINED_QUEUE,
                                    Placeholder.unparsed("name", executor.getName()),
                                    Placeholder.unparsed("queue", queue)
                                )
                            );
                        }
                        return Command.SINGLE_SUCCESS;
                    })
                    .then(Commands.argument("player", ArgumentTypes.players())
                        .requires(Permission.playerHas("gamecore.queue.join.other"))
                        .executes(context -> {
                            String queue = StringArgumentType.getString(context, "queue");
                            GameQueue gameQueue = GameCore.getQueueManager().getQueue(queue);

                            if (gameQueue == null) {
                                getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_QUEUE, "queue", queue));
                                return 0;
                            } else if (!gameQueue.getEnabled()) {
                                getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_QUEUE_DISABLED, "queue", queue));
                                return 0;
                            }

                            List<Player> players = context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource());

                            int success = 0;

                            for (Player player : players) {
                                GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer(player);
                                if (gameQueue.addPlayer(gamePlayer))
                                    success++;
                            }
                            getSender(context).sendMessage(Messages.deserialize(
                                Messages.INFO_COUNT_JOINED_QUEUE,
                                Placeholder.unparsed("count", String.valueOf(success)),
                                Placeholder.unparsed("queue", queue)
                            ));
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
            )
            .then(Commands.literal("leave")
                .requires(Permission.playerHas("gamecore.queue.leave"))
                .executes(context -> {
                    CommandSender sender = context.getSource().getSender();
                    Entity executor = context.getSource().getExecutor();

                    if (!(executor instanceof Player)) {
                        getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_NOT_PLAYER, "name", (executor == null) ? "NULL" : executor.getName()));
                        return 0;
                    }

                    GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer((Player) executor);
                    String queue = gamePlayer.getQueue();

                    GameQueue gameQueue = GameCore.getQueueManager().getQueue(queue);

                    if (gameQueue == null) {
                        sender.sendMessage(Messages.deserialize(Messages.ERROR_NOT_IN_QUEUE));
                        return 0;
                    }

                    gameQueue.removePlayer(gamePlayer);

                    if (!sender.equals(executor)) {
                        sender.sendMessage(
                            Messages.deserialize(
                                Messages.INFO_PLAYER_LEFT_QUEUE,
                                Placeholder.unparsed("name", executor.getName()),
                                Placeholder.unparsed("queue", queue)
                            )
                        );
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("player", ArgumentTypes.players())
                    .requires(Permission.playerHas("gamecore.queue.leave.other"))
                    .executes(context -> {

                        List<Player> players = context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource());

                        int success = 0;

                        for (Player player : players) {
                            GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer(player);
                            String queue = gamePlayer.getQueue();
                            GameQueue gameQueue = GameCore.getQueueManager().getQueue(queue);

                            if (gameQueue == null) {
                                continue;
                            }

                            if (gameQueue.removePlayer(gamePlayer))
                                success++;
                        }
                        getSender(context).sendMessage(Messages.deserialize(
                            Messages.INFO_COUNT_LEFT_QUEUE,
                            Placeholder.unparsed("count", String.valueOf(success))
                        ));
                        return Command.SINGLE_SUCCESS;
                    })
                )
            )
            .then(Commands.literal("list")
                .requires(Permission.playerHas("gamecore.queue.list"))
                .executes(context -> {
                    String[] queues = GameCore.getQueueManager().getQueueNames();
                    final Component component = MiniMessage.miniMessage().deserialize(
                            "<gray>Queues: [<white>" +
                                    String.join("</white>, <white>", queues) +
                                    "</white>]</gray>"
                    );
                    context.getSource().getSender().sendMessage(component);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("queue", StringArgumentType.word())
                    .suggests(SuggestionProviders.QUEUES)
                    .executes(context -> {
                        String queue = StringArgumentType.getString(context, "queue");
                        GameQueue gameQueue = GameCore.getQueueManager().getQueue(queue);

                        if (gameQueue == null) {
                            getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_QUEUE, "queue", queue));
                            return 0;
                        }

                        Set<GamePlayer> players = gameQueue.getPlayers();
                        String[] names = new String[players.size()];

                        int i = 0;
                        for (GamePlayer player : players) {
                            names[i] = player.handle().getName();
                            i++;
                        }

                        getSender(context).sendMessage(Messages.deserialize(
                            "Queue '<queue>' members: <gray><green>" +
                                String.join("</green>, <green>", names),
                            "queue", queue
                        ));

                        return Command.SINGLE_SUCCESS;
                    })
                )
            )
            .then(Commands.literal("pop")
                .requires(Permission.playerHas("gamecore.queue.pop"))
                .then(Commands.argument("queue", StringArgumentType.word())
                    .suggests(SuggestionProviders.QUEUES)
                    .then(Commands.literal("tag")
                        .then(Commands.argument("tag",StringArgumentType.word())
                            .executes(context -> {
                                String queue = StringArgumentType.getString(context, "queue");
                                String tag = StringArgumentType.getString(context, "tag");
                                GameQueue gameQueue = GameCore.getQueueManager().getQueue(queue);

                                if (gameQueue == null) {
                                    getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_QUEUE, "queue", queue));
                                    return 0;
                                } else if (!gameQueue.getEnabled()) {
                                    getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_QUEUE_DISABLED, "queue", queue));
                                    return 0;
                                }

                                gameQueue.popWithTag(tag);

                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                    .then(Commands.literal("team")
                        .then(Commands.argument("team",StringArgumentType.word())
                            .suggests(SuggestionProviders.TEAMS)
                            .executes(context -> {
                                String queue = StringArgumentType.getString(context, "queue");
                                GameQueue gameQueue = GameCore.getQueueManager().getQueue(queue);
                                if (gameQueue == null) {
                                    getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_QUEUE, "queue", queue));
                                    return 0;
                                }

                                String team = StringArgumentType.getString(context, "team");
                                GameTeam gameTeam = GameCore.getTeamManager().getTeam(team);

                                if (gameTeam == null) {
                                    getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_TEAM, "team", team));
                                    return 0;
                                }

                                gameQueue.popWithTeam(gameTeam);
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
            )
            .then(Commands.literal("enable")
                .requires(Permission.playerHas("gamecore.queue.set-enabled"))
                .then(Commands.argument("value", BoolArgumentType.bool())
                    .then(Commands.argument("queue", StringArgumentType.word())
                        .suggests(SuggestionProviders.QUEUES)
                        .executes(context -> {
                            String queue = StringArgumentType.getString(context, "queue");
                            boolean value = BoolArgumentType.getBool(context, "value");
                            GameQueue gameQueue = GameCore.getQueueManager().getQueue(queue);

                            if (gameQueue == null) {
                                getSender(context).sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_QUEUE, "queue", queue));
                                return 0;
                            } else if (gameQueue.getEnabled() && value) {
                                getSender(context).sendMessage(Messages.deserialize(Messages.WARNING_QUEUE_ENABLED, "queue", queue));
                            } else if (!gameQueue.getEnabled() && !value) {
                                getSender(context).sendMessage(Messages.deserialize(Messages.WARNING_QUEUE_DISABLED, "queue", queue));
                            } else if (value){
                                gameQueue.setEnabled(true);
                                getSender(context).sendMessage(Messages.deserialize(Messages.INFO_QUEUE_ENABLED, "queue", queue));
                            } else {
                                gameQueue.setEnabled(false);
                                getSender(context).sendMessage(Messages.deserialize(Messages.INFO_QUEUE_DISABLED, "queue", queue));
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
            )
            .then(Commands.literal("save")
                .requires(Permission.playerHas("gamecore.queue.save"))
                .executes(context -> {
                    GameCore.getQueueManager().save();
                    return Command.SINGLE_SUCCESS;
                })
            )
            .then(Commands.literal("load")
                .requires(Permission.playerHas("gamecore.queue.load"))
                .executes(context -> {
                    GameCore.getQueueManager().load();
                    return Command.SINGLE_SUCCESS;
                })
            )
            .build()
        ,"GameCore Queue Commands"
        , List.of("queue","gq","gqueue")
        );
    }

    private static CommandSender getSender(CommandContext<CommandSourceStack> context){
        return context.getSource().getSender();
    }
}
