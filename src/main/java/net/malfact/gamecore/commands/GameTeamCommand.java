package net.malfact.gamecore.commands;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.players.GamePlayer;
import net.malfact.gamecore.teams.GameTeam;
import net.malfact.gamecore.Messages;
import net.malfact.gamecore.util.Validate;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class GameTeamCommand {
    public static void addTeam(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        String team = (String) arguments.get("team");

        if (GameCore.getTeamManager().hasTeam(team)) {
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("TEAM_ALREADY_EXISTS", team));
        }

        GameTeam gameTeam = GameCore.getTeamManager().addTeam(team);

        if (gameTeam == null) {
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("TEAM_ADD_FAILED", team));
        }

        sender.sendMessage(Messages.get("TEAM_ADD_SUCCESS", team));
    }

    public static void removeTeam(CommandSender sender, CommandArguments arguments) {
        GameTeam team = (GameTeam) arguments.get("team");

        GameCore.getTeamManager().removeTeam(team.name);

        sender.sendMessage(Messages.get("TEAM_REMOVED", team.name));
    }

    public static int listTeam(CommandSender sender, CommandArguments arguments) {
        Optional<Object> optionalTeam = arguments.getOptional("team");

        if (optionalTeam.isPresent()) {
            GameTeam team = (GameTeam) optionalTeam.get();
            Set<GamePlayer> players = team.getPlayers();

            String[] names = players.stream().map(player -> {
                boolean online = player.isOnline();
                return (online ? "<green>" + player.getName() + "</green>" : "<red>" + player.getName() + "<red>");
            }).toArray(String[]::new);

            int online = 0;

            for (GamePlayer player : players) {
                if (player.isOnline()) online++;
            }

            sender.sendMessage(Messages.get("TEAM_LIST_MEMBERS", team.name, names.length, String.join(", ", names)));

            return online;
        } else {

            String[] names = GameCore.getTeamManager().getTeamNames();

            names = Arrays.stream(names).map(s -> "<white>" + s + "</white>").toArray(String[]::new);

            sender.sendMessage(Messages.get("TEAM_LIST", names.length, String.join(", ", names)));

            return names.length;
        }
    }

    public static void joinTeam(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        GameTeam team = (GameTeam) arguments.get("team");

        Player player;
        CommandSender proxiedCaller = null;

        if (sender instanceof ProxiedCommandSender proxy) {
            proxiedCaller = proxy.getCaller();

            if (!(proxy.getCallee() instanceof Player))
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("TARGET_NOT_PLAYER", proxy.getCallee().getName()));

            player = (Player) proxy.getCallee();
        } else {
            player = (Player) sender;
        }

        GamePlayer gamePlayer = Validate.isGamePlayer(player);

        if (team.hasPlayer(gamePlayer)) {
            if (proxiedCaller != null)
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("PLAYER_ALREADY_IN_TEAM", gamePlayer.getName(), team.name));
            else
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("SELF_ALREADY_IN_TEAM", team.name));

        }

        team.addPlayer(gamePlayer);

        if (proxiedCaller != null)
            proxiedCaller.sendMessage(Messages.get("PLAYER_JOINED_QUEUE", gamePlayer.getName(), team.name));
        else
            sender.sendMessage(Messages.get("SELF_JOIN_TEAM", team.name));
    }

    public static void joinTeamOther(CommandSender sender, CommandArguments arguments) {
        @SuppressWarnings("unchecked")
        Collection<Player> players = (Collection<Player>) arguments.get("player");

        GameTeam team = (GameTeam) arguments.get("team");
        GamePlayer[] gamePlayers = players.stream().map(Validate::isGamePlayer).toArray(GamePlayer[]::new);

        int count = 0;

        for (GamePlayer gamePlayer : gamePlayers) {
            if (team.hasPlayer(gamePlayer))
                continue;

            team.addPlayer(gamePlayer);
            count++;
        }

        sender.sendMessage(Messages.get("COUNT_JOINED_TEAM", count, team.name));
    }

    public static void leaveTeam(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        Player player;
        CommandSender proxiedCaller = null;

        if (sender instanceof ProxiedCommandSender proxy) {
            proxiedCaller = proxy.getCaller();

            if (!(proxy.getCallee() instanceof Player))
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("TARGET_NOT_PLAYER", proxy.getCallee().getName()));

            player = (Player) proxy.getCallee();
        } else {
            player = (Player) sender;
        }

        GamePlayer gamePlayer = Validate.isGamePlayer(player);

        if (GameCore.getTeamManager().getTeam(gamePlayer) == null) {
            if (proxiedCaller != null)
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("PLAYER_NOT_IN_TEAM", gamePlayer.getName()));
            else
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("SELF_NOT_IN_TEAM"));
        }

        GameTeam team = GameCore.getTeamManager().getTeam(gamePlayer);
        team.removePlayer(gamePlayer);

        if (proxiedCaller != null)
            proxiedCaller.sendMessage(Messages.get("PLAYER_LEFT_QUEUE", gamePlayer.getName(), team.name));
        else
            sender.sendMessage(Messages.get("SELF_LEFT_TEAM", team.name));
    }

    public static void leaveTeamOther(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        GamePlayer player = Validate.isGamePlayer((Player) arguments.get("player"));

        GameTeam team = GameCore.getTeamManager().getTeam(player);

        if (team == null)
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("PLAYER_NOT_IN_TEAM", player.getName()));

        team.removePlayer(player);

        sender.sendMessage(Messages.get("PLAYER_LEFT_TEAM", player.getName(), team.name));
    }

    public static int emptyTeam(CommandSender sender, CommandArguments arguments) {
        GameTeam team = (GameTeam) arguments.get("team");

        if (team.isEmpty()) {
            sender.sendMessage(Messages.get("TEAM_ALREADY_EMPTY", team.name));
            return 0;
        }

        int count = team.getPlayerCount();

        team.empty();
        sender.sendMessage(Messages.get("TEAM_EMPTIED", team.name, count));
        return count;
    }

    public static void setTeamSpawn(NativeProxyCommandSender sender, CommandArguments arguments) {
        String value = (String) arguments.get("value");
        GameTeam team = (GameTeam) arguments.get("team");

        if (value.equals("RESET")) {
            team.setSpawn(null);
            sender.sendMessage(Messages.get("TEAM_SPAWN_RESET", team.name));
            return;
        }

        Location location = sender.getLocation();
        team.setSpawn(sender.getLocation());
        sender.sendMessage(
            Messages.get(
                "TEAM_SPAWN_SET_TO",
                team.name,
                location.getX(),
                location.getY(),
                location.getZ()
            )
        );
    }

    public static void setTeamExit(NativeProxyCommandSender sender, CommandArguments arguments) {
        String value = (String) arguments.get("value");
        GameTeam team = (GameTeam) arguments.get("team");


        if (value.equals("RESET")) {
            team.setExit(null);
            sender.sendMessage(Messages.get("TEAM_EXIT_RESET", team.name));
            return;
        }

        Location location = sender.getLocation();
        team.setExit(sender.getLocation());
        sender.sendMessage(
            Messages.get(
                "TEAM_EXIT_SET_TO",
                team.name,
                location.getX(),
                location.getY(),
                location.getZ()
            )
        );
    }

    public static void setTeamLeaveOnDeath(CommandSender sender, CommandArguments arguments) {
        boolean value = (boolean) arguments.get("value");
        GameTeam team = (GameTeam) arguments.get("team");

        team.setLeaveOnDeath(value);

        sender.sendMessage(Messages.get("TEAM_OPTION_SET",team.name, "leaveOnDeath", value));
    }

    public static boolean executeIfGameTeam(CommandSender sender, Object[] arguments) {
        Player player = (Player) arguments[0];
        GameTeam team = (GameTeam) arguments[1];

        GamePlayer gamePlayer = GameCore.getPlayerManager().getPlayer(player);

        if (gamePlayer == null)
            return false;

        return team.equals(gamePlayer.getTeam());
    }
}

