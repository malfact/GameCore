package net.malfact.gamecore.command;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.Messages;
import net.malfact.gamecore.game.Game;
import net.malfact.gamecore.queue.GameQueue;
import net.malfact.gamecore.team.GameTeam;

public class Arguments {

    public static Argument<GameQueue> GameQueue(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            GameQueue queue = GameCore.getQueueManager().getQueue(info.input());

            if (queue == null)
                throw CustomArgument.CustomArgumentException.fromAdventureComponent(Messages.get("QUEUE_UNKNOWN", info.input()));
            else
                return queue;
        }).replaceSuggestions(ArgumentSuggestions.strings(info -> GameCore.getQueueManager().getQueueNames()));
    }

    public static Argument<GameTeam> GameTeam(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            GameTeam team = GameCore.getTeamManager().getTeam(info.input());

            if (team == null)
                throw CustomArgument.CustomArgumentException.fromAdventureComponent(Messages.get("TEAM_UNKNOWN", info.input()));
            else
                return team;

        }).replaceSuggestions(ArgumentSuggestions.strings(info -> GameCore.getTeamManager().getTeamNames()));
    }

    public static Argument<Game> Game(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            Game game = GameCore.gameManager().getGame(info.input());

            if (game == null)
                throw CustomArgument.CustomArgumentException.fromAdventureComponent(Messages.get("GAME_UNKNOWN", info.input()));
            else
                return game;

        }).replaceSuggestions(ArgumentSuggestions.strings(info -> GameCore.gameManager().getGameNames()));
    }
}
