package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;

public class EstadoFAQ extends Command {
    private final String tps = "Puedes pensar de los TPS como un medidor de lag pero al revés. Un tick en un servidor de Minecraft es la unidad de tiempo más pequeña, como si fuera un segundo. TPS significa ticks por segundo, entonces: ¿qué sería un buen valor TPS? Bueno, todos los servidores de Minecraft están limitados a 20 TPS; esto significa que si un servidor no tiene lag, sus TPS son o se acercan a 20.";
    private final String mspt = "Ahora que sabes qué es un tick y qué significa TPS, necesitas saber qué significa MSPT. En términos sencillos, es cuánto tarda un tick en ejecutarse. Ok, ok... Pero ¿y qué es? Son millisegundos, si haces los cálculos tienes que < 50ms por tick es correcto y tendrías más de 19 tps: sin lag.";
    private final String cpu = "Número de hilos del CPU en el servidor de Minecraft, no necesito explicar esto; ¿cierto?";
    private final String ram = "Proporción del uso de RAM en el servidor, mientras más espacio haya; mejor.";

    public EstadoFAQ() {
        this.name = "estadofaq";
        this.help = "Explica los datos en $estado.";
        this.aliases = new String[] {"efaq"};
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

        String[] fields = {"TPS:", "MSPT:", "Procesadores:", "Uso de RAM:"};
        String[] values = {tps, mspt, cpu, ram};

        EmbedBuilder statusEmbed = EmbedHelper.newEmbedWithArrays(true, "¿Qué significan esos datos de Estado? Ahora te los explico, mirá.", fields, values);

        commandEvent.reply(statusEmbed.build());
    }
}