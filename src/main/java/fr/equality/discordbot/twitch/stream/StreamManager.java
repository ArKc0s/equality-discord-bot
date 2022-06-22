package fr.equality.discordbot.twitch.stream;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.ScheduleSegmentInput;
import com.github.twitch4j.helix.domain.ScheduledSegment;
import com.github.twitch4j.helix.domain.StreamScheduleResponse;
import fr.equality.discordbot.Core;
import fr.equality.discordbot.discord.embeds.StreamCreatedEmbed;
import fr.equality.discordbot.discord.embeds.StreamRemovedEmbed;
import fr.equality.discordbot.exceptions.GameNotFoundException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.awt.*;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StreamManager {

    private TwitchClient client;
    private ArrayList<Stream> streams;

    public StreamManager(TwitchClient client) {
        this.client = client;
        this.streams = new ArrayList<>();
    }

    public int addStreamToSchedule(Stream stream) {

        StreamScheduleResponse response = client.getHelix().createStreamScheduleSegment(Core.AUTH_TOKEN,
                Core.CHANNEL_ID, new ScheduleSegmentInput(stream.getStartInstant(), "Europe/Paris",
                        stream.isRecurring(), false, stream.getDurationInMinutes(),
                        String.valueOf(stream.getGame().getIdentifier()), stream.getTitle())).execute();

        stream.setIDs(response.getSchedule().getSegments().get(0).getId());
        return stream.getId();
    }

    public void removeStreamFromSchedule(Stream stream) {

        client.getHelix().deleteStreamScheduleSegment(Core.AUTH_TOKEN, Core.CHANNEL_ID, stream.getTwitchID()).execute();
        streams.remove(stream);

    }

    public void getStreamsFromSchedule() throws ParseException, GameNotFoundException {

        List<ScheduledSegment> segments = client.getHelix().getChannelStreamSchedule(Core.AUTH_TOKEN, Core.CHANNEL_ID,
                null, null, "120", null, 25).execute().getSchedule().getSegments();

        assert segments != null;
        streams.clear();

        for(ScheduledSegment segment : segments) {

            Duration duration = Duration.between(segment.getStartTime(), segment.getEndTime());
            String durationString = DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm");

            streams.add(new Stream(segment.getId(), segment.getTitle(), segment.getCategory().getName(), durationString,
                    segment.getStartTime(), segment.getEndTime(), segment.isRecurring()));
        }
    }

    public void announceStreamCreation(Stream stream) {
        StreamCreatedEmbed embed = new StreamCreatedEmbed(stream);
        embed.sendEmbed(984391602338410516L);
    }

    public void announceStreamRemoval(Stream stream) {
        StreamRemovedEmbed embed = new StreamRemovedEmbed(stream);
        embed.sendEmbed(984391602338410516L);
    }

    public ArrayList<Stream> getStreams() {
        return streams;
    }

    public Stream getStreamById(int id) throws GameNotFoundException {
        for(Stream s: streams) {
            if(s.getId() == id) {
                return s;
            }
        }
        throw new GameNotFoundException("Stream non-existant ou ID incorrect !");
    }
}
