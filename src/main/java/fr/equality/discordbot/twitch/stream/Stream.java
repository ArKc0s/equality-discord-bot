package fr.equality.discordbot.twitch.stream;

import fr.equality.discordbot.twitch.game.Game;
import fr.equality.discordbot.twitch.game.GameNotFoundException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Stream {

    private String title;
    private Game game;
    private String duration;
    private Date date;
    private boolean isReccurent;

    public Stream(String title, String gameName, String startingTime, String duration, String date, boolean isReccurent) throws GameNotFoundException, ParseException {
        this.title = title;
        this.game = new Game(gameName);
        this.duration = duration;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.date = new Date(format.parse(date + " " + startingTime).getTime());

        this.isReccurent = isReccurent;
    }

    public String getTitle() {
        return title;
    }

    public Game getGame() {
        return game;
    }

    public String getStartingTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public String getDuration() {
        return duration;
    }

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public boolean isReccurent() {
        return isReccurent;
    }
}
