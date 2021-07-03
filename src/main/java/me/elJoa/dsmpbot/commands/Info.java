package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDate;
import java.util.Locale;

public class Info extends Command {
    private final String description = "Esta es la información que tengo sobre Dogesthetic SMP.";
    private final PrettyTime pT = new PrettyTime(new Locale("es"));

    public Info() {
        this.name = "info";
        this.help = "Devuelve información sobre DSMP.";
        this.aliases = new String[] {"informacion", "información"};
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

        final LocalDate dsmpStart = LocalDate.of(2018, 3, 17);
        final LocalDate dsmpT2 = LocalDate.of(2020, 6, 28);
        final LocalDate dsmpT3 = LocalDate.of(2020, 11, 29);

        String[] fields = {"¿Qué es Dogesthetic SMP?", "Tiempo desde el inicio de DSMP:", "Tiempo desde la segunda temporada:", "Tiempo desde la tercera temporada:", "¿Cómo está el bot?"};
        String[] values = {
                ConfigHandler.getSetting("acercade"),
                pT.format(dsmpStart).replaceAll("hace ", ""),
                pT.format(dsmpT2).replaceAll("hace ", ""),
                pT.format(dsmpT3).replaceAll("hace ", ""),
                "Crees que bien, crees que mal. ¿Por qué nadie pregunta eso? ¡Aunque ahora que soy open-source! https://github.com/Dogesthetic-SMP/DSMPBot"
        };

        EmbedBuilder statusEmbed = EmbedHelper.newEmbedWithArrays(true, description, fields, values);

        commandEvent.reply(statusEmbed.build());
    }
}