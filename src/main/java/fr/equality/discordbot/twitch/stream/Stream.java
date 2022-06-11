package fr.equality.discordbot.twitch.stream;

import fr.equality.discordbot.twitch.game.Game;
import fr.equality.discordbot.twitch.game.GameNotFoundException;


public class Stream {

    private String title;
    private Game game;
    private String startingTime;
    private String duration;
    private String date;
    private boolean isReccurent;

    public Stream(String title, String gameName, String startingTime, String duration, String date, boolean isReccurent) throws GameNotFoundException {
        this.title = title;
        this.game = new Game(gameName);
        this.startingTime = startingTime;
        this.duration = duration;
        this.date = date;
        this.isReccurent = isReccurent;
    }

    public String getTitle() {
        return title;
    }

    public Game getGame() {
        return game;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public boolean isReccurent() {
        return isReccurent;
    }
}
