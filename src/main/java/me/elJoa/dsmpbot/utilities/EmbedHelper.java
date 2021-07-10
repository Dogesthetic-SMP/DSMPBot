package me.elJoa.dsmpbot.utilities;

import me.elJoa.dsmpbot.ConfigHandler;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.annotation.Nullable;
import java.awt.*;

public class EmbedHelper {
    private static final String thumbnail = ConfigHandler.getSetting("thumbnail");
    private static final String footer = "DSMPBot " + "v2.0.1" + " - " + ConfigHandler.getSetting("footer");

    public static EmbedBuilder newEmbedWithArrays(boolean success, @Nullable String description, String[] fieldTitles, String[] fieldValues) {
        EmbedBuilder embedBuilder = new net.dv8tion.jda.api.EmbedBuilder();
        embedBuilder.setThumbnail(thumbnail);
        embedBuilder.setFooter(footer);
        embedBuilder.setDescription(description);

        if (success) {
            embedBuilder.setColor(Color.GREEN);
        } else {
            embedBuilder.setColor(Color.RED);
        }

        // {"Title0", "Title1"}
        // {"Value0", "Value1"}
        for (int x = 0; x < fieldValues.length; x++) {
            for (int y = 0; y < fieldTitles.length; y++) {
                embedBuilder.addField(fieldTitles[y + x], fieldValues[x], false);
                break;
            }
        }

        return embedBuilder;
    }

    public static EmbedBuilder newBasicEmbed(boolean success, @Nullable String description) {
        EmbedBuilder embedBuilder = new net.dv8tion.jda.api.EmbedBuilder();
        embedBuilder.setThumbnail(thumbnail);
        embedBuilder.setFooter(footer);
        embedBuilder.setDescription(description);

        if (success) {
            embedBuilder.setColor(Color.GREEN);
        } else {
            embedBuilder.setColor(Color.RED);
        }

        return embedBuilder;
    }

    public static EmbedBuilder newErrorEmbed(String descriptionError, String value) {
        EmbedBuilder errorEmbed = EmbedHelper.newBasicEmbed(false, descriptionError);
        errorEmbed.addField("¡Error!", value, false);
        return errorEmbed;
    }
}