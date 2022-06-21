package fr.equality.discordbot.twitch.stream;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.ScheduleSegmentInput;
import com.github.twitch4j.helix.domain.ScheduledSegment;
import com.github.twitch4j.helix.domain.StreamScheduleResponse;
import fr.equality.discordbot.Core;
import fr.equality.discordbot.twitch.game.GameNotFoundException;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.ParseException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StreamManager {

    private TwitchClient client;

    public StreamManager(TwitchClient client) {
        this.client = client;
    }

    public int addStreamToSchedule(Stream stream) {

        String[] separatedDuration = stream.getDuration().split(":");
        int durationInMinutes = Integer.parseInt(separatedDuration[0]) * 60 + Integer.parseInt(separatedDuration[1]);

        System.out.println(new java.util.Date(stream.getStartDate().getTime()).toInstant());

        StreamScheduleResponse response = client.getHelix().createStreamScheduleSegment(Core.authToken, Core.channelID, new ScheduleSegmentInput(new java.util.Date(stream.getStartDate().getTime()).toInstant() , "Europe/Paris", stream.isReccurent(), false, durationInMinutes, String.valueOf(stream.getGame().getIdentifier()), stream.getTitle())).execute();
        stream.setTwitchID(response.getSchedule().getSegments().get(0).getId());

        return stream.getId();
    }

    public void getStreamFromHelix(ArrayList<Stream> streams) throws ParseException, GameNotFoundException {

        List<ScheduledSegment> segments = client.getHelix().getChannelStreamSchedule(Core.authToken, Core.channelID, null, null, "120", null, 25).execute().getSchedule().getSegments();

        if(segments != null) {
            streams.clear();
        }

        for(ScheduledSegment segment : segments) {

            DateTimeFormatter startingFormatter = DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
            Duration duration = Duration.between(segment.getStartTime(), segment.getEndTime());

            streams.add(new Stream(
                    segment.getTitle(),
                    segment.getCategory().getName(),
                    startingFormatter.format(segment.getStartTime()),
                    DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm"),
                    dateFormatter.format(segment.getStartTime()),
                    segment.isRecurring()
                    ));
        }
    }


}
