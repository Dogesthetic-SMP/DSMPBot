package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DiscordAChat extends Command {
    private final String errorDescription = "Oh no, un error.";

    public DiscordAChat() {
        this.name = "msg";
        this.help = "Envía un mensaje al chat del servidor; se necesita el rol Dogger.";
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
        String discordMessage = commandEvent.getArgs();
        List<Role> doggerRoles = commandEvent.getGuild().getRolesByName("Dogger", true);
        boolean dogger = true;

        for (Role doggerRole : doggerRoles) {
            if (!author.getRoles().contains(doggerRole)) {
                dogger = false;
            }
        }

        if (!dogger) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "No eres Dogger."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        if (discordMessage.equals("")) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "Escribe un mensaje, por favor."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        String discordName = author.getEffectiveName();
        TextChannel textChannel = commandEvent.getTextChannel();
        String messageToSend = ChatColor.AQUA + "[Discord] " + ChatColor.WHITE + discordName + ": " + discordMessage;

        Bukkit.broadcastMessage(messageToSend);
        textChannel.sendMessage("Mensaje enviado.").queue(
                message -> message.delete().queueAfter(2, TimeUnit.SECONDS)
        );

    }
}