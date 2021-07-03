package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;

public class BajosFPS extends Command {
    private final String lowfpsfast = "Primero, con respeto gordo pelotudo. Segundo, sure! Antes de nada, debes saber que OPTIFINE = MIERDA, CACA. Ahora sí, descarga e instala Fabric desde https://fabricmc.net/use, luego descarga HighPerformanceDSMP.zip desde https://autooffice.cf/download/dogestheticsmp/HighPerformanceDSMP.zip (en algún momento tendremos una página web propia de la comunidad) y descomprime su contenido en tu .minecraft/mods. Y listo, abre el juego desde tu launcher con Fabric.";
    private final String whybetterthanoptifine = "Buena pregunta, bueno; es algo complicado, pero aquí mi respuesta lo más sencillo posible. OptiFine solo renderiza menos chunks y cambia cosas mínimas, Sodium y Phosphor reescriben muchas cosas importantes en el rendimiento del juego y hacen que Minecraft soporte Multidraw (mucho mejor que Oneshot). Aunque es posible que tu gráfica no sea compatible con Multidraw, Sodium no tiene la culpa.";

    public BajosFPS() {
        this.name = "bajosfps";
        this.help = "¿Bajos FPS? Vení que te muestro otro mundo.";
        this.aliases = new String[] {"fps", "bfps"};
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

        String[] fields = {"Tengo bajos FPS, dame algo rápido bot de mierda.", "Bueno, está piola y funciona mejor que OptiFine; ¿pero por qué?"};
        String[] values = {lowfpsfast, whybetterthanoptifine};

        EmbedBuilder statusEmbed = EmbedHelper.newEmbedWithArrays(true, "¿Bajos FPS? Vení que te muestro otro mundo..", fields, values);

        commandEvent.reply(statusEmbed.build());
    }
}