package fr.equality.discordbot.twitch.stream;

import fr.equality.discordbot.Core;
import fr.equality.discordbot.twitch.game.Game;
import fr.equality.discordbot.twitch.game.GameNotFoundException;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;


public class Stream {

    private int id;
    private String title;
    private Game game;
    private String duration;
    private Instant startInstant;
    private Instant endInstant;
    private boolean isRecurring;
    private String twitchID;

    public Stream(String title, String gameName, String startingTime, String duration, String date, boolean isRecurring)
            throws GameNotFoundException, ParseException {

        this.id = -1;
        this.twitchID = "";
        this.title = title;
        this.game = new Game(gameName);
        this.duration = duration;

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd/MM/yyyy HH:mm")
                .toFormatter()
                .withZone(ZoneId.of("Europe/Paris"));

        this.startInstant = formatter.parse(date + " " + startingTime, Instant::from);

        this.endInstant = startInstant;
        this.endInstant = this.endInstant.plusMillis(getDurationInMillis());

        this.isRecurring = isRecurring;
    }

    public Stream(String twitchID, String title, String gameName, String duration, Instant startInstant,
                  Instant endInstant, boolean isRecurring) throws GameNotFoundException {
        this.id = calcID(twitchID);
        this.twitchID = twitchID;
        this.title = title;
        this.game = new Game(gameName);
        this.duration = duration;
        this.startInstant = startInstant;
        this.endInstant = endInstant;
        this.isRecurring = isRecurring;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Game getGame() {
        return game;
    }

    public String getDuration() {
        return duration;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public Instant getStartInstant() {
        return startInstant;
    }

    public String getStartingTimeAsString() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("HH:mm")
                .toFormatter()
                .withZone(ZoneId.of("Europe/Paris"));

        return formatter.format(startInstant);
    }

    public String getStartDateAsString() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd/MM/yyyy")
                .toFormatter()
                .withZone(ZoneId.of("Europe/Paris"));

        return formatter.format(startInstant);
    }

    public Instant getEndInstant() {
        return endInstant;
    }

    public String getEndingTimeAsString() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("HH:mm")
                .toFormatter()
                .withZone(ZoneId.of("Europe/Paris"));

        return formatter.format(endInstant);
    }

    public String getEndingDateAsString() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd/MM/yyyy")
                .toFormatter()
                .withZone(ZoneId.of("Europe/Paris"));

        return formatter.format(endInstant);
    }

    public long getDurationInMillis() {
        String[] separatedDuration = duration.split(":");
        return (3600000L * Integer.parseInt(separatedDuration[0]) + (60000L * Integer.parseInt(separatedDuration[1])));
    }

    public int getDurationInMinutes() {
        String[] separatedDuration = duration.split(":");
        return Integer.parseInt(separatedDuration[0]) * 60 + Integer.parseInt(separatedDuration[1]);
    }

    public void setIDs(String twitchID) {
        this.twitchID = twitchID;
        this.id = calcID(twitchID);
    }

    public int calcID(String twitchID) {
        int sum = 0;
        for(char ch : twitchID.toCharArray()) sum += ch;
        return sum;
    }

    @Override
    public String toString() {
        return "Stream{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", game=" + game.getName() +
                ", duration='" + duration + '\'' +
                ", startInstant=" + startInstant +
                ", endInstant=" + endInstant +
                ", isRecurring=" + isRecurring +
                ", twitchID='" + twitchID + '\'' +
                '}';
    }
}
