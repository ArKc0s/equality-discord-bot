package fr.equality.discordbot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import fr.equality.discordbot.commands.CommandManager;
import fr.equality.discordbot.commands.ScheduleCommand;
import fr.equality.discordbot.twitch.game.Game;
import fr.equality.discordbot.twitch.game.GameNotFoundException;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Core {

    public static Dotenv dotenv;

    public static String AUTH_TOKEN;
    public static String CHANNEL_ID;
    public static String BOT_TOKEN;
    public static Long GUILD_ID;

    public static TwitchClient twitchClient;

    public static ArrayList<Stream> streams;
    public static StreamManager streamManager;

    public static JDA jdaClient;
    public static Guild jdaGuild;

    public static CommandManager commandManager;


    public static void main(String[] args) throws LoginException, InterruptedException {

        /* ------ Environment Variables Initialization ------ */
        dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();

        AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
        CHANNEL_ID = dotenv.get("CHANNEL_ID");
        BOT_TOKEN = dotenv.get("BOT_TOKEN");
        GUILD_ID = Long.valueOf(dotenv.get("GUILD_ID"));
        /* -------------------------------------------------- */

        /* ----- Twitch & Discord Client Initialization ----- */
        initializeClients();
        /* -------------------------------------------------- */

        /* ---------- Other Objects Initialization ---------- */
        streams = new ArrayList<>();
        streamManager = new StreamManager(twitchClient);

        jdaGuild = jdaClient.getGuildById(GUILD_ID);
        commandManager = new CommandManager(jdaClient);
        /* -------------------------------------------------- */

        /* ---------- Creating Discord Bot Command ---------- */
        commandManager.createCommands();
        /* -------------------------------------------------- */

    }


    public static void initializeClients() throws LoginException, InterruptedException {

        twitchClient = TwitchClientBuilder.builder()
                .withDefaultAuthToken(new OAuth2Credential("twitch", AUTH_TOKEN))
                .withEnableHelix(true)
                .build();

        jdaClient = JDABuilder.createDefault(BOT_TOKEN)
                .setActivity(Activity.playing("se faire développer par ArKc0s"))
                .addEventListeners(new ScheduleCommand())
                .build().awaitReady();
    }

}
