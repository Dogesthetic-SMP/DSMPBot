package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.concurrent.TimeUnit;

public class DiscordAConsola extends Command {
    private final ConfigHandler configHandler;
    private final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    private final String errorDescription = "¿Cuántos errores tiene este bot de mierda?";

    public DiscordAConsola(ConfigHandler configHandler) {
        this.name = "cmd";
        this.help = "joke-NotAJoke";
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
        String command = commandEvent.getArgs();
        TextChannel textChannel = commandEvent.getTextChannel();

        if (!ConfigHandler.getAdmins().contains(author.getId())) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "No tienes los permisos necesarios para usar este comando."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        if (command.equals("")) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "Coloca un comando, por favor."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        textChannel.sendMessage("Comando ejecutado correctamente.").queue(
                message -> message.delete().queueAfter(2, TimeUnit.SECONDS)
        );

        Bukkit.getScheduler().runTask(configHandler.getBot(), () -> Bukkit.getServer().dispatchCommand(console, command));
    }
}