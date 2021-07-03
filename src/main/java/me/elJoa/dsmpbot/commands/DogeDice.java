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

public class DogeDice extends Command {
    private final String errorDescription = "¿1!!!¿¿¡¡QUE!!!!!!???? POR QUEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE NOOOOOOOO MI DOGEDICEEEEEEEEEEEEEEEEEEEEEEEEEE";

    public DogeDice() {
        this.name = "dogedice";
        this.help = "joke-EasterEgg";
        this.aliases = new String[] {"dd"};
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
                    "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        Message message = commandEvent.getMessage();
        message.delete().queue();
        String text = ArgsHandler.getArguments(commandEvent);
        commandEvent.reply(text);
    }
}