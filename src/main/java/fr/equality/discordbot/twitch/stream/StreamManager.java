package fr.equality.discordbot.twitch.stream;

import fr.equality.discordbot.Core;

import java.sql.*;
import java.util.ArrayList;

public class StreamManager {

    private Connection connection;

    public void init() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + Core.DB_HOST +":"+Core.DB_PORT+"/"+Core.DB_NAME, Core.DB_USER, Core.DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveStreams(ArrayList<Stream> streams) {

    }

    public void pushStream(Stream stream) throws SQLException {

        String insert = "INSERT INTO streams VALUES (default,?,?,?,?,?,?)";
        PreparedStatement insertStream = connection.prepareStatement(insert);


        insertStream.setString(1, stream.getTitle());
        insertStream.setString(2, stream.getGame().getName());
        insertStream.setString(3, stream.getStartingTime());
        insertStream.setString(4, stream.getDuration());
        insertStream.setString(5, stream.getDate());
        insertStream.setBoolean(6, stream.isReccurent());

        insertStream.executeUpdate();
    }

    public int getLastID(){
        return 0;
    }


}
