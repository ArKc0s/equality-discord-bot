package fr.equality.discordbot.discord.embeds;

import fr.equality.discordbot.twitch.stream.Stream;

import java.awt.*;

public class StreamRemovedEmbed extends AbstractEmbed {

    public StreamRemovedEmbed(Stream stream) {
        super();

        String thumbnail = stream.getGame().getThumbnailUrl().replace("{width}", "300")
                .replace("{height}", "400");

        eb.setTitle(stream.getTitle(), "https://www.twitch.tv/equalitytv/schedule")
                .setDescription(stream.getGame().getName() + "\n\n")
                .setColor(new Color(224,79,95))
                .setFooter("EqualityTV (Made by ArKc0s) | Stream ID : " + stream.getId(),
                        "https://cdn.discordapp.com/attachments/557966947493478420/988867044021772359/logo5.png")
                .setThumbnail("https://cdn.discordapp.com/attachments/557966947493478420/989137094054142002/schedule.png")
                .setImage(thumbnail)
                .setAuthor("STREAM SUPPRIMÉ DU PROGRAMME !", "https://www.twitch.tv/equalitytv/schedule",
                        "https://cdn.discordapp.com/attachments/557966947493478420/989138793045032980/close.png")
                .addField("Du **" + stream.getStartDateAsString() +" à " + stream.getStartingTimeAsString() +
                                "** au **" + stream.getEndingDateAsString() + " à " + stream.getEndingTimeAsString() + "**",
                        "Sur [EqualityTV](https://www.twitch.tv/equalitytv/)", true);

    }
}
