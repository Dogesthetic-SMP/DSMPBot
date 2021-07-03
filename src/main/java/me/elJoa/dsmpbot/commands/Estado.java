package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import me.elJoa.dsmpbot.utilities.PerfHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.StringJoiner;

public class Estado extends Command {
    private final int maxPlayers = Bukkit.getServer().getMaxPlayers();
    private final String description = "Este es el estado del servidor de Minecraft. (Versión: " + Bukkit.getMinecraftVersion() + ")";
    private final int threads = Runtime.getRuntime().availableProcessors();
    private boolean hasLilDoksbolInServer = false;

    public Estado() {
        this.name = "estado";
        this.help = "Responde con el estado del servidor de Minecraft.";
        this.aliases = new String[] {"e"};
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

        /*
        Verificar si Lil_Doksbol está en el servidor.
        Hacer la lista de jugadores sin Lil_Doksbol.


         */

        String tps = PerfHandler.getTPS();
        String mspt = PerfHandler.getMSPT();
        String memory = PerfHandler.getMemoryUsage();
        String cpu = PerfHandler.getCPUUsage();

        Collection<? extends Player> onlinePlayersCollection = Bukkit.getServer().getOnlinePlayers();
        int onlinePlayers = onlinePlayersCollection.size();
        String players = "0/" + maxPlayers;

        for (Player p : onlinePlayersCollection) {
            if (p.getName().equals("Lil_Doksbol")) {
                hasLilDoksbolInServer = true;
            }
        }

        if (hasLilDoksbolInServer) {
            onlinePlayers--;
        }

        if (!onlinePlayersCollection.isEmpty()) {
            // 2/20 ()
            StringJoiner playersSJ = new StringJoiner(
                    ", ",
                    onlinePlayers + "/" + maxPlayers + " (",
                    ")"
            );

            // 2/20 (DrPuc, John)
            for (Player p : onlinePlayersCollection) {
                if (!p.getName().equals("Lil_Doksbol")) {
                    playersSJ.add(p.getName().replace("_", "\\_"));
                }
            }

            players = playersSJ.toString();

            if (hasLilDoksbolInServer && onlinePlayersCollection.size() == 1) {
                players = "0/" + maxPlayers;
            }
        }

        String[] fields = {"TPS (5s):", "MSPT (10s):", "Procesadores lógicos:", "Uso de CPU:", "Uso de RAM:", "Jugadores:", "¿Qué significa todo esto? ¿MSPT? Andá a la mierda gringo..."};
        String[] values = {tps, mspt, String.valueOf(threads), cpu, memory, players, "Usa $estadofaq para más información."};

        EmbedBuilder statusEmbed = EmbedHelper.newEmbedWithArrays(true, description, fields, values);

        commandEvent.reply(statusEmbed.build());
    }
}