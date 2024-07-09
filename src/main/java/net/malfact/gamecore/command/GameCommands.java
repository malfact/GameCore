package net.malfact.gamecore.command;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.Messages;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.game.ScriptedGame;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameCommands {

    public static void loadGame(CommandSender sender, @NotNull CommandArguments args) {
        String name = args.getUnchecked("game");

        if (GameCore.gameManager().getGame(name) != null)
            throw new CommandException("Game '" + name + "' is already loaded, use reload to update.");

        Path gamePath = Paths.get(GameCore.getInstance().getDataFolder() + "/games/" + name + ".lua");
        File file = gamePath.toFile();

        if (!file.exists())
            throw new CommandException("Game Script '" + name + "' does not exist!");

        if (file.isDirectory())
            throw new CommandException("'" + name + "' is a directory!");

        if (!file.canRead())
            throw new CommandException("Game Script '" + name + "' can not be read!");

        sender.sendMessage("Loading Game '" + name + "'!");

        var script = GameCore.scriptApi().getScript(file);
        ScriptedGame game = new ScriptedGame(script);
        GameCore.gameManager().registerGame(game);
        GameCore.scriptApi().loadScript(script, game);
    }

    public static void unloadGame(CommandSender sender, @NotNull CommandArguments args) {
        Game game = (Game) args.get("game");

        Game.State state = game.getState();
        if (state != Game.State.STOPPED)
            throw new CommandException("Can only unload a STOPPED game!");

        sender.sendMessage("Unloading Game " + game.getDisplayName() + "!");
        GameCore.gameManager().unregisterGame(game.getName());
    }

    public static void reloadGame(CommandSender sender, @NotNull CommandArguments args) {
        Game game = (Game) args.get("game");
        sender.sendMessage("Reloading Game '" + game.getDisplayName() + "'!");
        game.reload();
    }

    public static void startGame(CommandSender sender, @NotNull CommandArguments args) {
        Game game = (Game) args.get("game");

        Game.State state = game.getState();
        if (state != Game.State.STOPPED)
            throw new CommandException("Unable to start game in state " + state + "!");

        sender.sendMessage("Starting Game " + game.getDisplayName() + ".");
        game.start();
    }

    public static void stopGame(CommandSender sender, @NotNull CommandArguments args) {
        Game game = (Game) args.get("game");

        Game.State state = game.getState();
        if (state != Game.State.STARTING && state != Game.State.RUNNING)
            throw new CommandException("Unable to stop game in state " + state + "!");

        sender.sendMessage("Stopping Game " + game.getDisplayName() + ".");
        game.stop();
    }

    public static void joinGame(CommandSender sender, @NotNull CommandArguments args) throws WrapperCommandSyntaxException {
        Game game = (Game) args.get("game");

        if (game.getState() != Game.State.RUNNING)
            throw new CommandException("Game is not running!");

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

        Game inGame = GameCore.gameManager().getGame(player);

        if (inGame != null) {
            if (proxiedCaller != null)
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("PLAYER_ALREADY_IN_GAME", player.getName(), inGame.getDisplayName()));
            else
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("SELF_ALREADY_IN_GAME", inGame.getDisplayName()));
        }

        game.joinGame(player);

        if (proxiedCaller != null)
            proxiedCaller.sendMessage(Messages.get("PLAYER_JOINED_QUEUE", player.getName(), game.getDisplayName()));
    }

    public static void leaveGame(CommandSender sender, @NotNull CommandArguments args) throws WrapperCommandSyntaxException {
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

        Game inGame = GameCore.gameManager().getGame(player);

        if (inGame == null) {
            if (proxiedCaller != null)
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("PLAYER_NOT_IN_GAME", player.getName()));
            else
                throw CommandAPIBukkit.failWithAdventureComponent(Messages.get("SELF_NOT_IN_GAME"));
        }

        inGame.leaveGame(player);

        if (proxiedCaller != null)
            proxiedCaller.sendMessage(Messages.get("PLAYER_LEFT_GAME", player.getName(), inGame.getDisplayName()));
    }
}
