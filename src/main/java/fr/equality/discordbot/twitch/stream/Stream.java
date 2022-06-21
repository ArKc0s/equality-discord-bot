package fr.equality.discordbot.twitch.stream;

import fr.equality.discordbot.Core;
import fr.equality.discordbot.twitch.game.Game;
import fr.equality.discordbot.twitch.game.GameNotFoundException;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;


public class Stream {

    private int id;
    private String title;
    private Game game;
    private String duration;
    private Date startDate;
    private Date endDate;
    private boolean isReccurent;

    public Stream(int id, String title, String gameName, String startingTime, String duration, String date, boolean isReccurent) throws GameNotFoundException, ParseException, SQLException {

        this.id = id;
        this.title = title;
        this.game = new Game(gameName);
        this.duration = duration;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.startDate = new Date(format.parse(date + " " + startingTime).getTime());
        this.endDate = new Date(Long.sum(startDate.getTime(), processEndDate()));

        this.isReccurent = isReccurent;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Game getGame() {
        return game;
    }

    public String getStartingTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(startDate);
    }

    public String getDuration() {
        return duration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStartDateAsString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isReccurent() {
        return isReccurent;
    }

    public long processEndDate() {
        String[] separatedDuration = duration.split(":");
        return (3600000L * Integer.parseInt(separatedDuration[0]) + (1000L * Integer.parseInt(separatedDuration[1])));
    }
}
