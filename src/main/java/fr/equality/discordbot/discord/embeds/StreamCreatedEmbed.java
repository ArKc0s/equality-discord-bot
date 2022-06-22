package fr.equality.discordbot.discord.embeds;

import fr.equality.discordbot.twitch.stream.Stream;

import java.awt.*;

public class StreamCreatedEmbed extends AbstractEmbed {

    public StreamCreatedEmbed(Stream stream) {

        super();

        String thumbnail = stream.getGame().getThumbnailUrl().replace("{width}", "300")
                .replace("{height}", "400");

        eb.setTitle(stream.getTitle(), "https://www.twitch.tv/equalitytv/schedule")
                .setColor(new Color(50,190,166))
                .setDescription(stream.getGame().getName() + "\n\n")
                .setFooter("EqualityTV (Made by ArKc0s) | Stream ID : " + stream.getId(),
                        "https://cdn.discordapp.com/attachments/557966947493478420/988867044021772359/logo5.png")
                .setThumbnail("https://cdn.discordapp.com/attachments/557966947493478420/989137094054142002/schedule.png")
                .setImage(thumbnail)
                .setAuthor("NOUVEAU STREAM AU PROGRAMME !", "https://www.twitch.tv/equalitytv/schedule",
                        "https://cdn.discordapp.com/attachments/557966947493478420/989138632222855208/check.png")
                .addField("Du **" + stream.getStartDateAsString() +" à " + stream.getStartingTimeAsString() +
                        "** au **" + stream.getEndingDateAsString() + " à " + stream.getEndingTimeAsString() + "**",
                        "Sur [EqualityTV](https://www.twitch.tv/equalitytv/)", true);

    }
}
