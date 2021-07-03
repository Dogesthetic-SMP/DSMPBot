package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.ArgsHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Recargar extends Command {
    private final String successDescription = "Recargado:";
    private final String errorDescription = "Oops, ha ocurrido un error. ¡No te enojes conmigo!";
    private final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    private final ConfigHandler configHandler;

    public Recargar(ConfigHandler configHandler) {
        this.name = "recargar";
        this.help = "Recarga el bot; se necesita estar en la lista de administradores del bot.";

        this.configHandler = configHandler;
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

        if (reason == null || reason.equals("")) {
            reason = "No especificada.";
        }

        EmbedBuilder successEmbed = EmbedHelper.newBasicEmbed(true, successDescription);
        successEmbed.addField("El bot se recargó con éxito.", "Razón: " + reason, false);

        ConfigHandler.getLogger().severe("DSMPBot recargándose. Razón: " + reason);
        configHandler.reloadConfig();

        commandEvent.reply(successEmbed.build());
    }
}