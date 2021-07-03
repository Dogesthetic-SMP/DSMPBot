package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.ArgsHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import me.elJoa.dsmpbot.utilities.QueryAPIAccessor;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.util.concurrent.TimeUnit;

public class Statistics extends Command {
    private final String errorDescription = "Vaya, un error.";
    private final QueryAPIAccessor queryAPIAccessor;

    public Statistics(QueryAPIAccessor queryAPIAccessor) {
        this.name = "stats";
        this.aliases = new String[] {"statistics", "estadisticas"};
        this.help = "Muestra las estadísticas de un jugador.";

        this.queryAPIAccessor = queryAPIAccessor;
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

        String nameOfPlayer = ArgsHandler.getFirstArgument(commandEvent, false, true);
        if (nameOfPlayer == null) {
            nameOfPlayer = commandEvent.getMember().getEffectiveName();
        }

        if (nameOfPlayer.equalsIgnoreCase("lil_doksbol")) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    nameOfPlayer + " nunca entró al servidor, revisa el nombre."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(nameOfPlayer);
        if (player != null) {
            long timePlayedInSeconds = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
            long afkTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(queryAPIAccessor.getAFKTime(nameOfPlayer));
            int mobKills = player.getStatistic(Statistic.MOB_KILLS);
            int deaths = player.getStatistic(Statistic.DEATHS);
            int netherrackMined = player.getStatistic(Statistic.MINE_BLOCK, Material.NETHERRACK);

            String timePlayed = secondsToDaysHoursMinutes(timePlayedInSeconds);
            String afkTime = secondsToDaysHoursMinutes(afkTimeInSeconds);

            String skinHeadURL = "https://cravatar.eu/helmhead/" + nameOfPlayer + "/256.png";

            EmbedBuilder playerEmbed = EmbedHelper.newBasicEmbed(true, nameOfPlayer);
            playerEmbed.setThumbnail(skinHeadURL);

            playerEmbed.addField("Tiempo jugado:", timePlayed, false);
            playerEmbed.addField("Tiempo AFK: ", afkTime, false);
            playerEmbed.addField("Mobs eliminados:", String.valueOf(mobKills), false);
            playerEmbed.addField("Muertes:", String.valueOf(deaths), false);
            playerEmbed.addField("Netherrack picada:", String.valueOf(netherrackMined), false);
            commandEvent.reply(playerEmbed.build());
        } else {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    nameOfPlayer + " nunca entró al servidor, revisa el nombre."
            );
            commandEvent.reply(errorEmbed.build());
        }
    }

    private static String secondsToDaysHoursMinutes(long seconds) {
        long days = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (days * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        return days + "d, " + hours + "h, " + minutes + "m";
    }
}