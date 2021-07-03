package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.JDAHandler;

public class Trailer extends Command {
    public Trailer() {
        this.name = "trailer";
        this.help = "Muestra el trailer del servidor.";
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
        commandEvent.reply("https://www.youtube.com/watch?v=bQAX-ee3qOw");
    }
}