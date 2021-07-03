package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Set;
import java.util.function.Consumer;

public class Comandos implements Consumer<CommandEvent> {
    private final Set<Command> commandSet;
    private final String description = "Estos son mis comandos, úsalos bien:";

    public Comandos(Set<Command> commandSet) {
        this.commandSet = commandSet;
    }

    @Override
    public void accept(CommandEvent event) {
        if (JDAHandler.botDevMode) {
            if (!JDAHandler.shouldExecuteDevMode(event)) {
                return;
            }
        }

        if (JDAHandler.botBug)  {
            return;
        }

        EmbedBuilder embedBuilder = EmbedHelper.newBasicEmbed(true, description);

        for (Command command : commandSet) {
            if (!command.getHelp().startsWith("joke")) {
                embedBuilder.addField(
                        ConfigHandler.getSetting("prefix") + command.getName(), command.getHelp(), false
                );
            }
        }

        event.getAuthor().openPrivateChannel().submit()
                .thenCompose(
                        channel -> channel.sendMessage(embedBuilder.build()).submit()
                )
                .whenComplete((message, error) -> {
                    if (error != null) event.reply(embedBuilder.build());
                });
    }
}