package net.malfact.gamecore.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.players.GamePlayer;
import net.malfact.gamecore.teams.GameTeam;
import net.malfact.gamecore.teams.TeamManager;
import net.malfact.gamecore.util.Messages;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

@SuppressWarnings("UnstableApiUsage")
public class GameTeamCommand {

    public static void Register(Commands commands) {
        commands.register(
            Commands.literal("gameteam")

                .then(Commands.literal("list")
                    .requires(Permission.playerHas("gamecore.team.list"))
                    .executes(context -> {
                        Set<GameTeam> teams = GameCore.getTeamManager().getTeams();
                        String[] names = new String[teams.size()];
                        int i = 0;
                        for (GameTeam team : teams) {
                            names[i] = team.name;
                            i++;
                        }

                        final Component component = MiniMessage.miniMessage().deserialize(
                            "<gray>Teams: [<white>" +
                                String.join("</white>, <white>", names) +
                                "</white>]</gray>"
                        );
                        context.getSource().getSender().sendMessage(component);
                        return Command.SINGLE_SUCCESS;
                    })
                )

                .then(Commands.literal("add")
                    .requires(Permission.playerHas("gamecore.team.add"))
                    .then(Commands.argument("team", StringArgumentType.word())
                        .executes(context -> {
                            String team = StringArgumentType.getString(context, "team");

                            TeamManager teamManager = GameCore.getTeamManager();

                           CommandSender sender = context.getSource().getSender();
                            if (teamManager.hasTeam(team)) {
                                sender.sendMessage(Messages.deserialize(Messages.ERROR_TEAM_ALREADY_EXISTS,"team", team));
                                return 0;
                            }

                            GameTeam gameTeam = teamManager.addTeam(team);

                            if (gameTeam == null) {
                                sender.sendMessage(Messages.deserialize(Messages.ERROR_TEAM_NAME_IN_USE, "team", team));
                                return 0;
                            }

                            sender.sendMessage(Messages.deserialize(Messages.INFO_TEAM_CREATED, "team", team));
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )

                .then(Commands.literal("remove")
                    .requires(Permission.playerHas("gamecore.team.remove"))
                    .then(Commands.argument("team", StringArgumentType.word())
                        .suggests(SuggestionProviders.TEAMS)
                        .executes(context -> {
                            String team = StringArgumentType.getString(context, "team");

                            TeamManager teamManager = GameCore.getTeamManager();
                            CommandSender sender = context.getSource().getSender();
                            if (!teamManager.hasTeam(team)) {
                                sender.sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_TEAM, "team", team));
                                return 0;
                            }

                            teamManager.removeTeam(team);
                            sender.sendMessage(Messages.deserialize(Messages.INFO_TEAM_REMOVED, "team", team));

                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )

                .then(Commands.literal("join")
                    .requires(Permission.playerHas("gamecore.team.join"))
                    .then(Commands.argument("team", StringArgumentType.word())
                        .suggests(SuggestionProviders.TEAMS)
                        .executes(context -> {
                            CommandSender sender = context.getSource().getSender();
                            Entity executor = context.getSource().getExecutor();

                            if (!(executor instanceof Player)) {
                                sender.sendMessage(Messages.deserialize(Messages.ERROR_NOT_PLAYER, "name", (executor == null) ? "NULL" : executor.getName()));
                                return 0;
                            }

                            String team = StringArgumentType.getString(context, "team");
                            GameTeam gameTeam = GameCore.getTeamManager().getTeam(team);

                            if (gameTeam == null) {
                                sender.sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_TEAM, "team", team));
                                return 0;
                            }

                            GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer((Player) executor);
                            if (!gameTeam.addPlayer(gamePlayer)) {
                                sender.sendMessage(
                                    Messages.deserialize(
                                        Messages.ERROR_PLAYER_ALREADY_IN_TEAM,
                                        Placeholder.unparsed("name", gamePlayer.getName()),
                                        Placeholder.unparsed("team", team)
                                    )
                                );
                                return 0;
                            }

                            sender.sendMessage(
                                Messages.deserialize(
                                    Messages.INFO_PLAYER_JOINED_TEAM,
                                    Placeholder.unparsed("name", sender.getName()),
                                    Placeholder.unparsed("team", team)
                                )
                            );

                            return Command.SINGLE_SUCCESS;
                        })

                        .then(Commands.argument("player", ArgumentTypes.players())
                            .requires(Permission.playerHas("gamecore.team.join.other"))
                            .executes(context -> {
                                String team = StringArgumentType.getString(context, "team");
                                GameTeam gameTeam = GameCore.getTeamManager().getTeam(team);

                                CommandSender sender = context.getSource().getSender();

                                if (gameTeam == null) {
                                    sender.sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_TEAM, "team", team));
                                    return 0;
                                }

                                List<GamePlayer> gamePlayers = GameCore.getPlayerManager().getPlayers(
                                    context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource())
                                );

                                if (gamePlayers.size() == 1) {
                                    GamePlayer gamePlayer = gamePlayers.getFirst();
                                    gameTeam.addPlayer(gamePlayer);

                                    sender.sendMessage(
                                        Messages.deserialize(
                                            Messages.INFO_PLAYER_JOINED_TEAM,
                                            Placeholder.unparsed("name", gamePlayer.getName()),
                                            Placeholder.unparsed("team", team)
                                        )
                                    );

                                    return Command.SINGLE_SUCCESS;
                                } else if (gamePlayers.size() > 1) {
                                    gameTeam.addPlayers(gamePlayers);
                                    sender.sendMessage(Messages.deserialize(Messages.INFO_PLAYERS_JOINED_TEAM, "team", team));
                                    return Command.SINGLE_SUCCESS;
                                }

                                sender.sendMessage(Messages.deserialize(Messages.INFO_NO_PLAYERS_JOINED_TEAM, "team", team));
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                .then(Commands.literal("leave")
                    .requires(Permission.playerHas("gamecore.team.leave"))
                    .executes(context -> {
                        CommandSender sender = context.getSource().getSender();
                        Entity executor = context.getSource().getExecutor();

                        if (!(executor instanceof Player)) {
                            sender.sendMessage(Messages.deserialize(Messages.ERROR_NOT_PLAYER, "name", (executor == null) ? "NULL" : executor.getName()));
                            return 0;
                        }

                        GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer((Player) executor);
                        return playerLeaveTeam(sender, gamePlayer);
                    })
                    .then(Commands.argument("player", ArgumentTypes.players())
                        .requires(Permission.playerHas("gamecore.team.leave.other"))
                        .executes(context -> {
                            CommandSender sender = context.getSource().getSender();

                            List<GamePlayer> gamePlayers = GameCore.getPlayerManager().getPlayers(
                                context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource())
                            );

                            if (gamePlayers.size() == 1) {
                                return playerLeaveTeam(sender, gamePlayers.getFirst());
                            } else if (gamePlayers.size() > 1) {
                                for (GamePlayer gamePlayer : gamePlayers) {
                                    GameTeam team = GameCore.getTeamManager().getTeam(gamePlayer);
                                    if (team == null)
                                        continue;

                                    team.removePlayer(gamePlayer);
                                }

                                sender.sendMessage(Messages.deserialize(Messages.INFO_PLAYERS_LEFT_TEAMS));
                                return Command.SINGLE_SUCCESS;
                            }

                            sender.sendMessage(Messages.deserialize(Messages.INFO_NO_PLAYERS_LEFT_TEAMS));
                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )

                .then(Commands.literal("empty")
                    .requires(Permission.playerHas("gamecore.team.empty"))
                        .then(Commands.argument("team", StringArgumentType.word())
                            .suggests(SuggestionProviders.TEAMS)
                            .executes(context -> {
                                String team = StringArgumentType.getString(context, "team");
                                GameTeam gameTeam = GameCore.getTeamManager().getTeam(team);

                                CommandSender sender = context.getSource().getSender();

                                if (gameTeam == null) {
                                    sender.sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_TEAM, "team", team));
                                    return 0;
                                }

                                gameTeam.empty();
                                sender.sendMessage(Messages.deserialize(Messages.INFO_TEAM_EMPTIED, "team", team));

                                return Command.SINGLE_SUCCESS;
                            })
                        )
                )
                .then(Commands.literal("modify")
                    .requires(Permission.playerHas("gamecore.team.modify"))
                    .then(Commands.argument("team", StringArgumentType.word())
                        .suggests(SuggestionProviders.TEAMS)
                        .then(Commands.literal("spawn")
                            .then(Commands.literal("RESET").executes(setTeamLocation(LocationType.RESET, GameTeam::setSpawn)))
                            .then(Commands.literal("HERE").executes(setTeamLocation(LocationType.HERE, GameTeam::setSpawn)))
                        )
                        .then(Commands.literal("exit")
                            .then(Commands.literal("RESET").executes(setTeamLocation(LocationType.RESET, GameTeam::setExit)))
                            .then(Commands.literal("HERE").executes(setTeamLocation(LocationType.HERE, GameTeam::setExit)))
                        )
                    )
                )

                .then(Commands.literal("save")
                    .executes(context -> {
                        GameCore.getTeamManager().save();
                        return Command.SINGLE_SUCCESS;
                    })
                )
                .build(),
            "GameCore Team Commands",
            List.of("gt", "gteam"));
    }


    private enum LocationType {RESET, HERE}

    private static Command<CommandSourceStack> setTeamLocation(LocationType type, BiConsumer<GameTeam,Location> consumer) {
        return context -> {
            String team = StringArgumentType.getString(context, "team");
            GameTeam gameTeam = GameCore.getTeamManager().getTeam(team);

            CommandSender sender = context.getSource().getSender();

            if (gameTeam == null) {
                sender.sendMessage(Messages.deserialize(Messages.ERROR_UNKNOWN_TEAM, "team", team));
                return 0;
            }

            if (type == LocationType.RESET) {
                consumer.accept(gameTeam, null);
                sender.sendMessage(Messages.deserialize(Messages.INFO_TEAM_TP_RESET, "team", team));
                return Command.SINGLE_SUCCESS;
            }

            Location location = context.getSource().getLocation();

            consumer.accept(gameTeam, location);

            sender.sendMessage(Messages.deserialize(Messages.INFO_TEAM_TP_HERE,"team",team));

            return Command.SINGLE_SUCCESS;
        };
    }

    private static int playerLeaveTeam(CommandSender sender, GamePlayer player) {
        GameTeam gameTeam = GameCore.getTeamManager().getTeam(player);

        if (gameTeam == null) {
            sender.sendMessage(Messages.deserialize(Messages.ERROR_PLAYER_NOT_IN_TEAM, "name", player.getName()));
            return 0;
        }

        gameTeam.removePlayer(player);
        sender.sendMessage(
            Messages.deserialize(
                Messages.INFO_PLAYER_LEFT_TEAM,
                Placeholder.unparsed("name", player.getName()),
                Placeholder.unparsed("team", gameTeam.name)
            )
        );

        return Command.SINGLE_SUCCESS;
    }
}
