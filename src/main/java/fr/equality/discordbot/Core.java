package fr.equality.discordbot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import fr.equality.discordbot.commands.ScheduleCommand;
import fr.equality.discordbot.twitch.stream.Stream;
import fr.equality.discordbot.twitch.stream.StreamManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class Core {

    public static Dotenv dotenv;

    public static String authToken;
    public static String channelID;
    public static String botToken;
    public static Long guildID;
    public static String DB_HOST;
    public static String DB_NAME;
    public static String DB_PORT;
    public static String DB_USER;
    public static String DB_PASSWORD;

    public static TwitchClient twitchClient;

    public static JDA jda;
    public static Guild guild;

    public static ArrayList<Stream> streams;
    public static StreamManager streamManager;


    public static void main(String[] args) throws LoginException, InterruptedException {

        streams = new ArrayList<>();
        streamManager = new StreamManager();


        dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();

        authToken = dotenv.get("AUTH_TOKEN");
        channelID = dotenv.get("CHANNEL_ID");
        botToken = dotenv.get("BOT_TOKEN");
        guildID = Long.valueOf(dotenv.get("GUILD_ID"));
        DB_HOST = dotenv.get("DB_HOST");
        DB_NAME = dotenv.get("DB_NAME");
        DB_PORT = dotenv.get("DB_PORT");
        DB_USER = dotenv.get("DB_USER");
        DB_PASSWORD = dotenv.get("DB_PASSWORD");



        twitchClient = TwitchClientBuilder.builder()
                .withDefaultAuthToken(new OAuth2Credential("twitch", authToken))
                .withEnableHelix(true)
                .build();

        streamManager.init(twitchClient);

        JDA jda = JDABuilder.createDefault(botToken)
                .setActivity(Activity.playing("se faire développer par ArKc0s"))
                .addEventListeners(new ScheduleCommand())
                .build().awaitReady();

        guild = jda.getGuildById(guildID);

        guild.upsertCommand("planning", "Test de la commande planning")
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

        List<Command> commands = guild.retrieveCommands().complete();







    }
}
