package net.malfact.gamecore.command;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.Messages;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.ScriptedGame;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class GameCommands {

    public static void loadGame(CommandSender sender, @NotNull CommandArguments args) throws WrapperCommandSyntaxException {
        String name = args.getUnchecked("game");

        if (GameCore.gameManager().getGame(name) != null)
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("GAME_ALREADY_LOADED", name));

        Path gamePath = Paths.get(GameCore.getInstance().getDataFolder() + "/games/" + name + ".lua");
        File file = gamePath.toFile();

        if (!file.exists())
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("GAME_SCRIPT_DOES_NOT_EXIST", name));

        if (file.isDirectory())
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("ERROR_PATH_IS_DIR", gamePath));

        if (!file.canRead())
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("ERROR_READ_ACCESS", gamePath));

        sender.sendMessage(Messages.get("GAME_LOADING", name));

        var script = GameCore.scriptApi().getScript(file);
        ScriptedGame game = new ScriptedGame(script);
        GameCore.gameManager().registerGame(game);
        GameCore.scriptApi().loadScript(script, game);
    }

    public static void unloadGame(CommandSender sender, @NotNull CommandArguments args) throws WrapperCommandSyntaxException {
        Game game = (Game) args.get("game");

        Game.State state = game.getState();
        if (state != Game.State.STOPPED)
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("GAME_UNABLE_TO_X_IN_STATE", "unload", state));

        sender.sendMessage(Messages.get("GAME_UNLOADING", game.getName()));
        GameCore.gameManager().unregisterGame(game.getName());
    }

    public static void reloadGame(CommandSender sender, @NotNull CommandArguments args) {
        Game game = (Game) args.get("game");
        sender.sendMessage(Messages.get("GAME_RELOADING", game.getDisplayName()));
        game.reload();
    }

    public static void startGame(CommandSender sender, @NotNull CommandArguments args) throws WrapperCommandSyntaxException {
        Game game = (Game) args.get("game");

        Game.State state = game.getState();
        if (state != Game.State.STOPPED)
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("GAME_UNABLE_TO_X_IN_STATE", "start", state));

        sender.sendMessage(Messages.get("GAME_STARTING", game.getDisplayName()));
        game.start();
    }

    public static void stopGame(CommandSender sender, @NotNull CommandArguments args) throws WrapperCommandSyntaxException {
        Game game = (Game) args.get("game");

        Game.State state = game.getState();
        if (state != Game.State.STARTING && state != Game.State.RUNNING)
            throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("GAME_UNABLE_TO_X_IN_STATE", "stop", state));

        sender.sendMessage(Messages.get("GAME_STOPPING"));
        game.stop();
    }

    private static Player checkPlayer(CommandSender sender) throws WrapperCommandSyntaxException {
        if (sender instanceof Player player)
            return player;

        throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("TARGET_NOT_PLAYER", sender.getName()));
    }

    private static int joinGame(CommandSender sender, Game game) throws WrapperCommandSyntaxException {
        Player player = checkPlayer(sender);

        if (GameCore.gameManager().joinGame(player, game))
            return 1;

        sender.sendMessage(Messages.get("SELF_ALREADY_IN_GAME"));
        return 0;
    }

    private static int joinGame(CommandSender caller, CommandSender callee, Game game) throws WrapperCommandSyntaxException {
        Player player = checkPlayer(callee);

        if (GameCore.gameManager().joinGame(player, game)) {
            caller.sendMessage(Messages.get("PLAYER_JOINED_GAME", player.getName(), game.getDisplayName()));
            return 1;
        }

        caller.sendMessage(Messages.get("PLAYER_ALREADY_IN_GAME"));
        return 0;
    }

    private static int joinGame(CommandSender sender, Game game, Collection<Player> players) {
        int count = 0;
        for (var player : players) {
            if (GameCore.gameManager().joinGame(player, game))
                count++;
        }

        sender.sendMessage(Messages.get("COUNT_JOINED_GAME", count, game));

        return count;
    }

    public static int joinGame(CommandSender sender, @NotNull CommandArguments args) throws WrapperCommandSyntaxException {
        Game game = (Game) args.get("game");
        if (args.count() == 1) {
            if (sender instanceof ProxiedCommandSender proxy)
                return joinGame(proxy.getCaller(), proxy.getCallee(), game);
            else
                return joinGame(sender, game);
        } else {
            Collection<Player> players = args.getUnchecked("players");
            if (players == null)
                return 0;

            return joinGame(sender, game, players);
        }
    }

    private static void leaveGame(CommandSender sender) throws WrapperCommandSyntaxException {
        Player player = checkPlayer(sender);

        if (!GameCore.gameManager().isPlayerInGame(player)) {
            sender.sendMessage(Messages.get("SELF_NOT_IN_GAME"));
            return;
        }

        GameCore.gameManager().leaveGame(player);
    }

    private static void leaveGame(CommandSender caller, CommandSender callee) throws WrapperCommandSyntaxException {
        leaveGame(caller, checkPlayer(callee));
    }

    private static void leaveGame(CommandSender sender, Player player) {
        if (!GameCore.gameManager().isPlayerInGame(player)) {
            sender.sendMessage(Messages.get("PLAYER_NOT_IN_GAME", player.getName()));
            return;
        }

        Game game = GameCore.gameManager().getGame(player);
        GameCore.gameManager().leaveGame(player);
        sender.sendMessage(Messages.get("PLAYER_LEFT_GAME", player.getName(), game.getDisplayName()));
    }

    public static void leaveGame(CommandSender sender, @NotNull CommandArguments args) throws WrapperCommandSyntaxException {
        if (args.count() == 1) {
            if (sender instanceof ProxiedCommandSender proxy)
                leaveGame(proxy.getCaller(), proxy.getCallee());
            else
                leaveGame(sender);
        } else {
            Player player = args.getUnchecked("player");
            if (player != null)
                leaveGame(sender, player);
        }
    }
}
