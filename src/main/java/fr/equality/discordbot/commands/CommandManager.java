package fr.equality.discordbot.commands;

import fr.equality.discordbot.Core;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandManager {

    private JDA jdaClient;
    private Guild jdaGuild;

    public CommandManager(JDA jdaClient) {
        this.jdaClient = jdaClient;
        this.jdaGuild = jdaClient.getGuildById(Core.GUILD_ID);
    }

    public void createCommands() {

        //Planning command
        jdaGuild.upsertCommand("planning", "Test de la commande planning")
                .addSubcommands(
                        new SubcommandData("show", "Retourne la liste des streams prévus"),
                        new SubcommandData("confirm", "Permet de confirmer une action"),
                        new SubcommandData("create", "Permet d'ajouter un stream au planning Twitch & annonce le stream")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "titre", "Titre du stream", true),
                                        new OptionData(OptionType.STRING, "jeu", "Jeu du stream (Attention à l'orthographe !)", true),
                                        new OptionData(OptionType.STRING, "début", "Heure de début (format : hh:mm)", true),
                                        new OptionData(OptionType.STRING, "durée", "Durée du stream (format : hh:mm)", true),
                                        new OptionData(OptionType.STRING, "date", "Date du stream (format : JJ/MM/AAAA)", true),
                                        new OptionData(OptionType.BOOLEAN, "réccurent", "Est-ce que le stream est récurrent ?", true)
                                ),
                        new SubcommandData("remove", "Permet de retirer un streamau planning Twitch & annonce l'annulation")
                                .addOptions(new OptionData(OptionType.INTEGER, "id", "ID du stream a supprimer", true))
                )
                .queue();

    }
}
