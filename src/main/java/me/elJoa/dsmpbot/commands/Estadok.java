package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.JDAHandler;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;

public class Estadok extends Command {
    public Estadok() {
        this.name = "estadok";
        this.help = "joke-EasterEgg";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (JDAHandler.botBug)  {
            return;
        }

        Member doksbolMember = commandEvent.getGuild().getMemberById("401068186113409024");
        boolean discordStatus = doksbolMember.getOnlineStatus() == OnlineStatus.ONLINE || doksbolMember.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB;

        if (discordStatus) {
            commandEvent.reply("Doksbol ESTÁ.");
        } else {
            commandEvent.reply("Doksbol NO ESTÁ.");
        }
    }
}