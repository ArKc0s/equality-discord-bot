package fr.equality.discordbot.twitch.stream;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.ScheduleSegmentInput;
import com.github.twitch4j.helix.domain.ScheduledSegment;
import com.github.twitch4j.helix.domain.StreamScheduleResponse;
import fr.equality.discordbot.Core;
import fr.equality.discordbot.twitch.game.GameNotFoundException;
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

    public StreamManager(TwitchClient client) {
        this.client = client;
    }

    public int addStreamToSchedule(Stream stream) {

        StreamScheduleResponse response = client.getHelix().createStreamScheduleSegment(Core.AUTH_TOKEN,
                Core.CHANNEL_ID, new ScheduleSegmentInput(stream.getStartInstant(), "Europe/Paris",
                        stream.isRecurring(), false, stream.getDurationInMinutes(),
                        String.valueOf(stream.getGame().getIdentifier()), stream.getTitle())).execute();

        stream.setIDs(response.getSchedule().getSegments().get(0).getId());
        return stream.getId();
    }

    public void getStreamsFromSchedule(ArrayList<Stream> streams) throws ParseException, GameNotFoundException {

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

        String thumbnail = stream.getGame().getThumbnailUrl().replace("{width}", "300")
                .replace("{height}", "400");

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Nouveau stream planifié !", "https://www.twitch.tv/equalitytv")
                .setDescription("__Un nouveau stream vient d'être ajouté à la programmation !__")
                .setColor(new Color(891305))
                .setFooter("EqualityTV (Made by ArKc0s) | Stream ID : " + stream.getId(),
                        "https://cdn.discordapp.com/attachments/557966947493478420/988867044021772359/logo5.png")
                .setThumbnail(thumbnail)
                .addBlankField(false)
                .addField("__"+ stream.getTitle() +"__", "sur _" + stream.getGame().getName() +"_",
                        false)
                .addBlankField(false)
                .addField("__Date__", stream.getStartDateAsString(), true)
                .addField("__Horaire__", stream.getStartingTimeAsString() + "-" +
                        stream.getEndingTimeAsString(), true)
                .addBlankField(false)
                .addField("__Retrouve la programmation ici__", "https://www.twitch.tv/equalitytv/schedule",
                        false);

        Core.jdaGuild.getChannelById(TextChannel.class, 984391602338410516L).sendMessageEmbeds(eb.build()).queue();

    }


}
