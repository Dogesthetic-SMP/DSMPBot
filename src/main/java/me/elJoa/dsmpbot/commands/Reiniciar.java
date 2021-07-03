package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.ArgsHandler;
import me.elJoa.dsmpbot.utilities.DelayHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.ChatColor;

public class Reiniciar extends Command {
    private boolean restarting = false;
    private final String successDescription = "Reiniciado:";
    private final String errorDescription = "Oops, ha ocurrido un error. ¡No te enojes conmigo!";

    public Reiniciar() {
        this.name = "reiniciar";
        this.help = "Reinicia el servidor; se necesita estar en la lista de administradores del bot.";
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
        String reason = ArgsHandler.getArguments(commandEvent);

        if (!ConfigHandler.getAdmins().contains(author.getId())) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "No estás en la lista de administradores del bot."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        if (restarting) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "El servidor ya está reiniciándose."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        if (reason == null || reason.equals("")) {
            reason = "No especificada.";
        }

        restarting = true;
        EmbedBuilder restartingEmbed = EmbedHelper.newBasicEmbed(true, "Reiniciando.");
        restartingEmbed.addField("Reiniciando el servidor en 30 segundos.", "Razón: " + reason, false);
        commandEvent.reply(restartingEmbed.build());

        DelayHandler.sendMessage(ChatColor.GREEN + "El servidor se reiniciará en 30 segundos, disculpen las molestias.");
        DelayHandler.sendDelayMessage(ChatColor.GREEN + "15 segundos para el reinicio.", 20 * 15);
        DelayHandler.sendDelayMessage(ChatColor.GREEN + "Reiniciando el servidor, no entren.", 20 * 26);

        DelayHandler.delayKickEveryone(20 * 30, reason);
        ConfigHandler.getLogger().severe(
                "Servidor reiniciado por DSMPBot. Nombre: " + author.getEffectiveName() + ". Razón: " + reason);

        EmbedBuilder successEmbed = EmbedHelper.newBasicEmbed(true, successDescription);
        successEmbed.addField("El servidor se reinició con éxito.", "Razón: " + reason, false);
        DelayHandler.sendDelayDiscordMessage(successEmbed.build(), commandEvent, 20 * 30);

        DelayHandler.executeDelayCommand("restart", 20 * 32);
    }
}