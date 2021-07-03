package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.ArgsHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Limpiar extends Command {
    private final String errorDescription = "Ha ocurrido un error, pero aquí lo tengo. No le digas a nadie, casi se me cae.";
    private final String successDescription = "Mensajes eliminados.";

    public Limpiar() {
        this.name = "limpiar";
        this.help = "Elimina x mensajes; se necesita el permiso para administrar mensajes o estar en la lista de administradores del bot.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (JDAHandler.botDevMode) {
            if (!JDAHandler.shouldExecuteDevMode(commandEvent)) {
                return;
            }
        }

        if (JDAHandler.botBug)  {
            return;
        }

        Member author = commandEvent.getMember();
        if (!ConfigHandler.getAdmins().contains(author.getId())) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "No tienes los permisos necesarios para usar este comando."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        TextChannel textChannel = commandEvent.getTextChannel();
        String firstArgument = ArgsHandler.getFirstArgument(commandEvent, true, false);
        if (firstArgument == null) {
            return;
        }

        int amountOfMessagesToDelete = Integer.parseInt(firstArgument);

        if (amountOfMessagesToDelete == 0) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "No puedo eliminar 0 mensajes."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        if (amountOfMessagesToDelete > 50) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "¡Creo que te equivocaste y has puesto un dígito de más! Pero tranquilo, I got it."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        List<Message> messagesToDelete = textChannel.getHistory().retrievePast(amountOfMessagesToDelete + 1).complete();
        textChannel.deleteMessages(messagesToDelete).complete();

        EmbedBuilder successEmbed = EmbedHelper.newBasicEmbed(true, successDescription);
        String output = "Eliminé " + amountOfMessagesToDelete;

        if  (amountOfMessagesToDelete > 1) {
            output += " mensajes.";
        } else {
            output += " mensaje.";
        }

        successEmbed.addField(output, "", false);
        textChannel.sendMessage(successEmbed.build()).queue(
                message -> message.delete().queueAfter(2, TimeUnit.SECONDS)
        );
    }
}