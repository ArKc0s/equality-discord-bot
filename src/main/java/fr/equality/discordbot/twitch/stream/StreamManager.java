package fr.equality.discordbot.twitch.stream;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.ScheduleSegmentInput;
import com.github.twitch4j.helix.domain.ScheduledSegment;
import com.github.twitch4j.helix.domain.StreamScheduleResponse;
import fr.equality.discordbot.Core;
import fr.equality.discordbot.twitch.game.GameNotFoundException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.awt.*;
import java.text.ParseException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    public void announceStreamCreation(Stream stream) {

        String thumbnail = stream.getGame().getThumbnailUrl().replace("{width}", "300").replace("{height}", "400");

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Nouveau stream planifié !", "https://www.twitch.tv/equalitytv")
                .setDescription("__Un nouveau stream vient d'être ajouté à la programmation !__")
                .setColor(new Color(891305))
                .setFooter("EqualityTV (Made by ArKc0s)", "https://cdn.discordapp.com/attachments/557966947493478420/988867044021772359/logo5.png")
                .setThumbnail(thumbnail)
                .addBlankField(false)
                .addField("__"+ stream.getTitle() +"__", "sur _" + stream.getGame().getName() +"_", false)
                .addBlankField(false)
                .addField("__Date__", stream.getStartDateAsString(), true)
                .addField("__Horaire__", stream.getStartingTime() + "-" + stream.getEndingTime(), true)
                .addBlankField(false)
                .addField("__Retrouve la programmation ici__", "https://www.twitch.tv/equalitytv/schedule", false);

        Core.guild.getChannelById(TextChannel.class, 724609006018232400L).sendMessageEmbeds(eb.build()).queue();

    }


}
