package fr.equality.discordbot.twitch.stream;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.ScheduleSegmentInput;
import fr.equality.discordbot.Core;
import fr.equality.discordbot.twitch.game.GameNotFoundException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StreamManager {

    private Connection connection;
    private TwitchClient client;

    public void init(TwitchClient client) {

        this.client = client;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + Core.DB_HOST +":"+Core.DB_PORT+"/"+Core.DB_NAME, Core.DB_USER, Core.DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveStreams(ArrayList<Stream> streams) throws SQLException, ParseException, GameNotFoundException {

        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

        String getQuery = "SELECT * FROM streams WHERE (end_date > NOW()) OR (is_reccurent = 1)";
        PreparedStatement getStreams = connection.prepareStatement(getQuery);

        streams.clear();

        ResultSet rs = getStreams.executeQuery();

        while(rs.next()) {
            streams.add(new Stream(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("game"),
                    formatTime.format(rs.getDate("start_date")),
                    rs.getString("duration"),
                    formatDate.format(rs.getDate("start_date")),
                    rs.getBoolean("is_reccurent")
            ));

        }

        rs.close();
        getStreams.close();

    }

    public void pushStream(Stream stream) throws SQLException {

        String insertQuery = "INSERT INTO streams VALUES (?,?,?,?,?,?,?)";
        PreparedStatement insertStream = connection.prepareStatement(insertQuery);

        insertStream.setInt(1, stream.getId());
        insertStream.setString(2, stream.getTitle());
        insertStream.setString(3, stream.getGame().getName());
        insertStream.setString(4, stream.getDuration());
        insertStream.setTimestamp(5, new Timestamp(stream.getStartDate().getTime()));
        insertStream.setTimestamp(6, new Timestamp(stream.getEndDate().getTime()));
        insertStream.setBoolean(7, stream.isReccurent());

        insertStream.executeUpdate();
        insertStream.close();
    }

    /*public void updateReccurentStreams() throws SQLException {
        String query = "UPDATE streams SET start_date = ?, end_date = ? WHERE is_reccurent = 1 AND end_date < now()";
        PreparedStatement ps = connection.prepareStatement(query);
    }*/

    public int getNextID() throws SQLException {

        int lastID = 0;

        String getQuery = "SELECT id FROM streams ORDER BY id DESC LIMIT 1";
        PreparedStatement getLastID = connection.prepareStatement(getQuery);

        ResultSet rs = getLastID.executeQuery();
        while(rs.next()) {
            lastID = rs.getInt("id");
        }

        rs.close();
        getLastID.close();

        return lastID+1;
    }

    public void addStreamToSchedule(Stream stream) {

        String[] separatedDuration = stream.getDuration().split(":");
        int durationInMinutes = Integer.parseInt(separatedDuration[0]) * 60 + Integer.parseInt(separatedDuration[1]);

        client.getHelix().createStreamScheduleSegment(Core.authToken, Core.channelID, new ScheduleSegmentInput(stream.getStartDate().toInstant(), "Europe/Paris", stream.isReccurent(), false, durationInMinutes, ))
    }


}
