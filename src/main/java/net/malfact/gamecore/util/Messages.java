package net.malfact.gamecore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class Messages {
    public static final String ERROR_COULD_NOT_CREATE_QUEUE = "<red>Could not create queue '<queue>'";
    public static final String ERROR_UNKNOWN_QUEUE = "<red> Unknown queue name '<queue>'";
    public static final String ERROR_UNKNOWN_TEAM = "<red> Unknown team name '<team>'";

    public static final String ERROR_NOT_PLAYER = "<red><name> is not a player";

    public static final String ERROR_QUEUE_DISABLED = "<red>Queue '<queue>' is disabled";
    public static final String ERROR_NOT_IN_QUEUE = "<red>You are not in a queue";
    public static final String ERROR_TEAM_ALREADY_EXISTS = "<red>A team already exists by '<team>'";
    public static final String ERROR_TEAM_NAME_IN_USE = "<red>Team name '<team>' is already in use.";
    public static final String ERROR_PLAYER_ALREADY_IN_TEAM = "<red><name> is already in team [<team>]";
    public static final String ERROR_PLAYER_NOT_IN_TEAM = "<red><name> is not in a team";

    public static final String WARNING_QUEUE_ENABLED = "Queue '<queue>' is already enabled";
    public static final String WARNING_QUEUE_DISABLED = "Queue '<queue>' is already disabled";

    public static final String INFO_QUEUE_ENABLED = "Queue '<queue>' is now enabled";
    public static final String INFO_QUEUE_DISABLED = "Queue '<queue>' is now disabled";
    public static final String INFO_QUEUE_CREATED = "Created new queue '<queue>'";
    public static final String INFO_QUEUE_REMOVED = "Removed queue '<queue>'";

    public static final String INFO_COUNT_LEFT_QUEUE = "<count> players have left their queues";
    public static final String INFO_COUNT_JOINED_QUEUE = "<count> players have joined the queue '<queue>'";
    public static final String INFO_LEFT_QUEUE = "You have left the queue '<queue>'";
    public static final String INFO_JOINED_QUEUE = "You have joined the queue '<queue>'";
    public static final String INFO_PLAYER_LEFT_QUEUE = "<name> has left the queue '<queue>'";
    public static final String INFO_PLAYER_JOINED_QUEUE = "<name> has joined the queue '<queue>'";

    public static final String INFO_TEAM_CREATED = "Created team [<team>]";
    public static final String INFO_TEAM_REMOVED = "Removed team [<team>]";
    public static final String INFO_TEAM_EMPTIED = "Team [<team>] has been emptied.";
    public static final String INFO_NO_PLAYERS_JOINED_TEAM = "No Players have left the team [<team>]";
    public static final String INFO_PLAYER_JOINED_TEAM = "<name> has joined the team [<team>]";
    public static final String INFO_PLAYER_LEFT_TEAM = "<name> has left the team [<team>]";
    public static final String INFO_PLAYERS_JOINED_TEAM = "Players have joined the team [<team>]";
    public static final String INFO_PLAYERS_LEFT_TEAMS = "Players have left their teams";
    public static final String INFO_NO_PLAYERS_LEFT_TEAMS = "No Players have left their teams";

    public static final String INFO_TEAM_TP_RESET = "Teleport location for team [<team>] has been reset";
    public static final String INFO_TEAM_TP_HERE = "Teleport location for team [<team>] has been set to location";

    public static Component deserialize(final String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Component deserialize(final String message, final String tag, final String value) {
        return deserialize(message, Placeholder.unparsed(tag, value));
    }

    public static Component deserialize(final String message, TagResolver... resolvers) {
        return MiniMessage.miniMessage().deserialize(message, resolvers);
    }
}
