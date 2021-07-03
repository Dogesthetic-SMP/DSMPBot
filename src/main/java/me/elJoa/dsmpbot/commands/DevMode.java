package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class DevMode extends Command {
    public DevMode() {
        this.name = "devmode";
        this.help = "joke-NotAJoke";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Guild guild = commandEvent.getGuild();
        if (!guild.getName().equals("DrPuc's development server")) {
            return;
        }

        Member author = commandEvent.getMember();
        if (!ConfigHandler.getAdmins().contains(author.getId())) {
            return;
        }

        JDAHandler.botDevMode = !(JDAHandler.botDevMode);
    }
}