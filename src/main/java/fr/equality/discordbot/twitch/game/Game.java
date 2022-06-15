package fr.equality.discordbot.twitch.game;

import com.github.twitch4j.helix.domain.GameList;
import fr.equality.discordbot.Core;

import java.util.List;

public class Game {

    private int identifier;
    private String name;
    private String thumbnailUrl;

    public Game(String name) throws GameNotFoundException {

        GameList gameList = Core.twitchClient.getHelix().getGames(Core.authToken, null, List.of(name)).execute();

        if(gameList.getGames().size() == 1) {
            this.identifier = Integer.parseInt(gameList.getGames().get(0).getId());
            this.name = gameList.getGames().get(0).getName();
            this.thumbnailUrl = gameList.getGames().get(0).getBoxArtUrl();
        } else if(gameList.getGames().size() == 0) {
            throw new GameNotFoundException("Jeu non trouvé dans l'API Twitch");
        }

    }

    public int getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
