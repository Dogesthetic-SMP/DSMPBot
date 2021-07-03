package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class FinalizarDebate extends Command {
    private final String errorDescription = "Welp, don't worry be happy!";
    private final String successDescription = "Nice! Now fuck each other arguments.";

    public FinalizarDebate() {
        this.name = "finalizardebate";
        this.help = "Finaliza el debate, ejecutar sólo si están todos de acuerdo en que se acabó.";
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

        Guild guild = commandEvent.getGuild();
        if (!guild.getName().startsWith("Dogesthetic SMP")) {
            return;
        }

        TextChannel textChannel = commandEvent.getTextChannel();
        net.dv8tion.jda.api.entities.Category debateCategory = guild.getCategoryById(ConfigHandler.getSetting("debateId"));
        if (textChannel.getParent() != debateCategory) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "Wow, ¿crees que eres inteligente?"
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        for (Member member : textChannel.getMembers()) {
            ConfigHandler.getLogger().info("outside if");
            ConfigHandler.getLogger().info(member.getEffectiveName());
            if (!member.getId().equals("342686594442067983") &&
                    !member.getId().equals("401068186113409024") &&
                    !member.getId().equals(ConfigHandler.getSetting("botId"))) {
                ConfigHandler.getLogger().info("inside if");
                ConfigHandler.getLogger().info(member.getEffectiveName());
                textChannel.getPermissionOverride(member).getManager().resetAllow().queue();
            }
        }

        EmbedBuilder successEmbed = EmbedHelper.newBasicEmbed(
                true,
                "Debate terminado, si no concuerdas con esto taguea a DrPuc o a Doksbol en otro canal."
        );
        commandEvent.reply(successEmbed.build());
    }
}