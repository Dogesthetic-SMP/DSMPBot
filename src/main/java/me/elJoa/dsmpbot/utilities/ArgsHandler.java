package me.elJoa.dsmpbot.utilities;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.lang.StringUtils;

public class ArgsHandler {
    private static final String errorDescription = "Ha ocurrido un error, pero aquí lo tengo. No te molestes, porfa.";

    public static String getFirstArgument(CommandEvent commandEvent, boolean shouldBeNumeric, boolean canBeNull) {
        String arguments = commandEvent.getArgs();
        try {
            String argument = StringUtils.split(arguments)[0];
            boolean isNumeric = argument.chars().allMatch(Character::isDigit);

            if (shouldBeNumeric && !isNumeric) {
                EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(errorDescription, "Coloca un argumento válido, por favor.");
                commandEvent.reply(errorEmbed.build());
                return null;
            }

            if (!shouldBeNumeric && isNumeric) {
                EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(errorDescription, "Coloca un argumento válido, por favor.");
                commandEvent.reply(errorEmbed.build());
                return null;
            }

            return argument;
        } catch (ArrayIndexOutOfBoundsException e) {
            if (!canBeNull) {
                EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(errorDescription, "Coloca un argumento, por favor.");
                commandEvent.reply(errorEmbed.build());
            }
            return null;
        }
    }

    public static String getArguments(CommandEvent commandEvent) {
        return commandEvent.getArgs();
    }
}
