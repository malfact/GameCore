package net.malfact.gamecore.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.malfact.gamecore.GameCore;
import net.malfact.gamecore.teams.GameTeam;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class SuggestionProviders {

    public static final QueueSuggestionProvider QUEUES = new QueueSuggestionProvider();
    public static final TeamSuggestionProvider TEAMS = new TeamSuggestionProvider();
    public static final TeamOptionSuggestionProvider TEAM_OPTIONS = new TeamOptionSuggestionProvider();


    public static class QueueSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

        private QueueSuggestionProvider(){}

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            String remaining = builder.getRemaining();

            for (String queueName : GameCore.getQueueManager().getQueueNames()) {
                if (queueName.startsWith(remaining))
                    builder.suggest(queueName);
            }

            return builder.buildFuture();
        }
    }

    public static class TeamSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

        private TeamSuggestionProvider() {}

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            String remaining = builder.getRemaining();
            for (GameTeam team : GameCore.getTeamManager().getTeams()) {
                if (team.name.startsWith(remaining))
                    builder.suggest(team.name);
            }

            return builder.buildFuture();
        }
    }

    public static class TeamOptionSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

        private TeamOptionSuggestionProvider() {}

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            return builder.buildFuture();
        }
    }
}
