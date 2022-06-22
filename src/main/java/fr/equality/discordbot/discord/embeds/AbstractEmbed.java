package fr.equality.discordbot.discord.embeds;

import fr.equality.discordbot.Core;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class AbstractEmbed {

    protected EmbedBuilder eb;

    public AbstractEmbed() {
        this.eb = new EmbedBuilder();
    }

    public MessageEmbed buildEmbed() {
        return eb.build();
    }

    public void sendEmbed(long channelId) {
        Core.jdaGuild.getChannelById(TextChannel.class, channelId).sendMessageEmbeds(buildEmbed()).queue();
    }
}
