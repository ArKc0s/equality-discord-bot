package fr.equality.discordbot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import fr.equality.discordbot.commands.CommandManager;
import fr.equality.discordbot.commands.ScheduleCommand;
import fr.equality.discordbot.exceptions.GameNotFoundException;
import fr.equality.discordbot.twitch.stream.Stream;
import fr.equality.discordbot.twitch.stream.StreamManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.text.ParseException;

public class Core {

    public static Dotenv dotenv;

    public static String AUTH_TOKEN;
    public static String CHANNEL_ID;
    public static String BOT_TOKEN;
    public static Long GUILD_ID;

    public static TwitchClient twitchClient;

    public static StreamManager streamManager;

    public static JDA jdaClient;
    public static Guild jdaGuild;

    public static CommandManager commandManager;


    public static void main(String[] args) throws LoginException, InterruptedException, ParseException, GameNotFoundException {

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
        streamManager = new StreamManager(twitchClient);

        jdaGuild = jdaClient.getGuildById(GUILD_ID);
        commandManager = new CommandManager(jdaClient);
        /* -------------------------------------------------- */

        /* ---------- Creating Discord Bot Command ---------- */
        commandManager.createCommands();
        jdaClient.addEventListener(new ScheduleCommand());
        /* -------------------------------------------------- */

        streamManager.getStreamsFromSchedule();

        for(Stream s : streamManager.getStreams()) {
            System.out.println(s.toString());
            System.out.println(" ");
        }

    }


    public static void initializeClients() throws LoginException, InterruptedException {

        twitchClient = TwitchClientBuilder.builder()
                .withDefaultAuthToken(new OAuth2Credential("twitch", AUTH_TOKEN))
                .withEnableHelix(true)
                .build();

        jdaClient = JDABuilder.createDefault(BOT_TOKEN)
                .setActivity(Activity.playing("se faire développer par ArKc0s"))
                .build().awaitReady();
    }

}
