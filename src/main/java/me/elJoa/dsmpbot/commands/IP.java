package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class IP extends Command {
    private final String errorDescription = "Vaya, un error.";

    public IP() {
        this.name = "ip";
        this.help = "Muestra la IP del servidor...";
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

        commandEvent.getAuthor().openPrivateChannel().submit()
                .thenCompose(
                        channel -> channel.sendMessage("Pssst. La IP del servidor es " + ConfigHandler.getSetting("ip") + ", recuerda que pasarle la IP a una persona que no es Dogger es sancionable.").submit()
                );
        commandEvent.reply("No te voy a pasar la IP >:(");
    }
}