import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import discord4j.core.GatewayDiscordClient;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    public static Dotenv dotenv;

    public static String authToken;
    public static String channelID;
    public static String botToken;

    public static Long guildID;
    public static Long applicationID;

    public static TwitchClient twitchClient;


    public static void main(String[] args) {

        dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();

        authToken = dotenv.get("AUTH_TOKEN");
        channelID = dotenv.get("CHANNEL_ID");
        botToken = dotenv.get("BOT_TOKEN");
        guildID = Long.valueOf(dotenv.get("GUILD_ID"));

        System.out.println(guildID);

        twitchClient = TwitchClientBuilder.builder()
                .withDefaultAuthToken(new OAuth2Credential("twitch", authToken))
                .withEnableHelix(true)
                .build();


    }
}
