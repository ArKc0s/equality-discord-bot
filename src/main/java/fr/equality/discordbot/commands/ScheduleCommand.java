package fr.equality.discordbot.commands;

import fr.equality.discordbot.Core;
import fr.equality.discordbot.exceptions.GameNotFoundException;
import fr.equality.discordbot.exceptions.StreamNotFoundException;
import fr.equality.discordbot.twitch.stream.Stream;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.List;

public class ScheduleCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if(event.getName().equals("planning")) {

            if(event.getSubcommandName().equals("create")) {

                if(!event.getMember().getRoles().contains(Core.jdaClient.getRoleById(724609045603811438L))) {
                    event.reply("Permission non accordée !").queue();
                } else {

                    List<OptionMapping> options = event.getOptions();

                    final String title = options.get(0).getAsString();
                    final String game = options.get(1).getAsString();
                    final String startingTime = options.get(2).getAsString();
                    final String duration = options.get(3).getAsString();
                    final String date = options.get(4).getAsString();
                    final boolean isReccurent = options.get(5).getAsBoolean();

                    event.deferReply();

                    try {

                        Stream stream = new Stream(title, game, startingTime, duration, date, isReccurent);
                        int identifier = Core.streamManager.addStreamToSchedule(stream);
                        Core.streamManager.announceStreamCreation(stream);

                        event.reply("**Stream ajouté** : " + stream.getTitle() + " sur " + stream.getGame().getName() + " (ID = **" + identifier + "**)").queue();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Core.streamManager.getStreamsFromSchedule();
                                } catch (ParseException | GameNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    } catch (GameNotFoundException | ParseException e) {
                        e.printStackTrace();
                        event.reply("**Erreur d'exécution de la commande : **" + e.getMessage()).queue();
                        //TODO: Handle Helix Errors "HystrixRuntimeException"
                    }

                }

            } else if(event.getSubcommandName().equals("remove")) {

                if(!event.getMember().getRoles().contains(Core.jdaClient.getRoleById(724609045603811438L))) {
                    event.reply("**Permission non accordée !**").queue();
                } else {

                    List<OptionMapping> options = event.getOptions();

                    final int ID = options.get(0).getAsInt();

                    event.deferReply();

                    try {

                        Stream s = Core.streamManager.getStreamById(ID);

                        event.reply("OK").queue();

                        Core.streamManager.removeStreamFromSchedule(s);
                        Core.streamManager.announceStreamRemoval(s);

                    } catch (StreamNotFoundException e) {

                        e.printStackTrace();
                        event.reply("Erreur d'exécution de la commande : " + e.getMessage()).queue();

                    }


                }

            }


        }
    }
}
