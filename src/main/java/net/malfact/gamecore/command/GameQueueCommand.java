package net.malfact.gamecore.command;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.player.GamePlayer;
import net.malfact.gamecore.queue.GameQueue;
import net.malfact.gamecore.team.GameTeam;
import net.malfact.gamecore.Messages;
import net.malfact.gamecore.util.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class GameQueueCommand {

    public static void addQueue(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        String queue = (String) arguments.get("queue");

        if (GameCore.getQueueManager().hasQueue(queue)) {
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("QUEUE_ALREADY_EXISTS", queue));
        }

        GameQueue gameQueue = GameCore.getQueueManager().addQueue(queue);

        if (gameQueue == null) {
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("QUEUE_ADD_FAILED", queue));
        }

        sender.sendMessage(Messages.get("QUEUE_ADD_SUCCESS", queue));
    }

    public static void removeQueue(CommandSender sender, CommandArguments arguments) {
        GameQueue queue = (GameQueue) arguments.get("queue");

        GameCore.getQueueManager().removeQueue(queue.name);

        sender.sendMessage(Messages.get("QUEUE_REMOVED", queue.name));
    }

    public static int listQueue(CommandSender sender, CommandArguments arguments) {
        Optional<Object> optionalQueue = arguments.getOptional("queue");

        if (optionalQueue.isPresent()) {
            GameQueue queue = (GameQueue) optionalQueue.get();
            Set<GamePlayer> players = queue.getPlayers();

            String[] names = players.stream().map(player -> {
                boolean online = player.isOnline();
                return (online ? "<green>" + player.getName() + "</green>" : "<red>" + player.getName() + "</red>");
            }).toArray(String[]::new);

            int online = 0;

            for (GamePlayer player : players) {
                if (player.isOnline()) online++;
            }

            sender.sendMessage(Messages.get("QUEUE_LIST_MEMBERS", queue.name, "" + names.length, String.join(", ", names)));

            return online;
        } else {

            String[] names = GameCore.getQueueManager().getQueueNames();

            names = Arrays.stream(names).map(s -> {
                boolean enabled = GameCore.getQueueManager().getQueue(s).getEnabled();
                return (enabled ? "<green>" + s + "</green>" : "<red>" + s + "</red>");
            }).toArray(String[]::new);

            sender.sendMessage(Messages.get("QUEUE_LIST", "" + names.length, String.join(", ", names)));

            return names.length;
        }
    }

    public static void enableQueue(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        GameQueue queue = (GameQueue) arguments.get("queue");

        if (queue.getEnabled())
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("QUEUE_ALREADY_ENABLED", queue.name));

        queue.setEnabled(true);
        sender.sendMessage(Messages.get("QUEUE_NOW_ENABLED", queue.name));
    }

    public static void disableQueue(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        GameQueue queue = (GameQueue) arguments.get("queue");

        if (!queue.getEnabled())
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("QUEUE_ALREADY_DISABLED", queue.name));

        queue.setEnabled(false);
        sender.sendMessage(Messages.get("QUEUE_NOW_DISABLED", queue.name));
    }

    public static void joinQueue(CommandSender sender, @NotNull CommandArguments arguments) throws WrapperCommandSyntaxException {
        GameQueue queue = arguments.getUnchecked("queue");

        if (!queue.getEnabled())
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("QUEUE_DISABLED", queue.name));

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

        if (queue.hasPlayer(gamePlayer)) {
            if (proxiedCaller != null)
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("PLAYER_ALREADY_IN_QUEUE", gamePlayer.getName(), queue.name));
            else
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("SELF_ALREADY_IN_QUEUE", queue.name));

        }

        queue.addPlayer(gamePlayer);

        if (proxiedCaller != null)
            proxiedCaller.sendMessage(Messages.get("PLAYER_JOINED_QUEUE", gamePlayer.getName(), queue.name));
    }

    public static int joinQueueOther(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        @SuppressWarnings("unchecked")
        Collection<Player> players = (Collection<Player>) arguments.get("player");

        GameQueue queue = (GameQueue) arguments.get("queue");
        if (!queue.getEnabled())
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("QUEUE_DISABLED", queue.name));

        GamePlayer[] gamePlayers = players.stream().map(Validate::isGamePlayer).toArray(GamePlayer[]::new);

        int count = 0;

        for (GamePlayer gamePlayer : gamePlayers) {
            if (queue.hasPlayer(gamePlayer))
                continue;

            queue.addPlayer(gamePlayer);
            count++;
        }

        sender.sendMessage(Messages.get("COUNT_JOINED_QUEUE", "" + count, queue.name));
        return count;
    }

    public static void leaveQueue(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
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

        if (gamePlayer.getQueueName().isEmpty()) {
            if (proxiedCaller != null)
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("PLAYER_NOT_IN_QUEUE", gamePlayer.getName()));
            else
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("SELF_NOT_IN_QUEUE"));
        }

        GameQueue queue = GameCore.getQueueManager().getQueue(gamePlayer.getQueueName());
        queue.removePlayer(gamePlayer);

        if (proxiedCaller != null)
            proxiedCaller.sendMessage(Messages.get("PLAYER_LEFT_QUEUE", gamePlayer.getName(), queue.name));
    }

    public static void leaveQueueOther(CommandSender sender, CommandArguments arguments) throws WrapperCommandSyntaxException {
        GamePlayer player = Validate.isGamePlayer((Player) arguments.get("player"));

        if (player.getQueueName().isEmpty())
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("PLAYER_NOT_IN_QUEUE", player.getName()));

        GameQueue queue = GameCore.getQueueManager().getQueue(player.getQueueName());
        queue.removePlayer(player);

        sender.sendMessage(Messages.get("PLAYER_LEFT_QUEUE", player.getName(), queue.name));
    }

    public static int emptyQueue(CommandSender sender, CommandArguments arguments) {
        GameQueue queue = (GameQueue) arguments.get("queue");

        if (queue.isEmpty()) {
            sender.sendMessage(Messages.get("QUEUE_ALREADY_EMPTY", queue.name));
            return 0;
        }

        int count = queue.getPlayerCount();

        queue.empty();
        sender.sendMessage(Messages.get("QUEUE_EMPTIED", queue.name, ""+count));
        return count;
    }

    public static int popQueueTag(CommandSender sender, CommandArguments arguments) {
        GameQueue queue = (GameQueue) arguments.get("queue");
        String tag = (String) arguments.get("tag");

        int count = queue.getPlayerCount();

        queue.popWithTag(tag);

        sender.sendMessage(Messages.get("COUNT_POPPED_QUEUE_TAG", ""+count, queue.name, tag));
        return count;
    }

    public static int popQueueTeam(CommandSender sender, CommandArguments arguments) {
        GameQueue queue = (GameQueue) arguments.get("queue");
        GameTeam team = (GameTeam) arguments.get("team");

        Optional<Integer> optionalCount = arguments.getOptionalByClass("count", Integer.class);

        int count = optionalCount.orElseGet(() -> queue.getPlayerCount());

        queue.popWithTeam(team, count);

        sender.sendMessage(Messages.get("COUNT_POPPED_QUEUE_TEAM", ""+count, queue.name, team.name));
        return count;
    }
}
