package me.elJoa.dsmpbot.minecraftcommands;

// informoneseventeen

import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

public class Inform117 implements CommandExecutor {
    private final JDA jda;

    public Inform117(JDA jda) {
        this.jda = jda;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender != Bukkit.getConsoleSender()) {
            return false;
        }
        final LocalDate now = LocalDate.now();
        final LocalDate dsmpT4 = LocalDate.of(2021, 8, 1);
        int days = Period.between(now, dsmpT4).getDays();
        String message = "¡Faltan " + days + " días para la temporada 4!";
        if (days == 1) {
            message = "¡Falta " + days + " día para la temporada 4!";
        }
        jda.getGuildById("424731928315428865").getTextChannelById("424759167845138433").sendMessage(message).queue();
        return true;
    }
}