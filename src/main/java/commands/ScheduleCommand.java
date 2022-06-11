package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ScheduleCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if(event.getName().equals("planning")) {

            if(event.getSubcommandName().equals("create")) {

                List<OptionMapping> options = event.getOptions();

                final String title = options.get(0).getAsString();
                final String game = options.get(1).getAsString();
                final String startingTime = options.get(2).getAsString();
                final String duration = options.get(3).getAsString();
                final String date = options.get(4).getAsString();
                final boolean isReccurent = options.get(5).getAsBoolean();


                event.reply(options.get(0).getAsString()).queue();
            }


        }
    }
}
