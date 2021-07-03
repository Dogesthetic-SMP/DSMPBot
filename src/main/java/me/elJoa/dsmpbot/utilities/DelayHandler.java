package me.elJoa.dsmpbot.utilities;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DelayHandler {
    private static final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    private static Plugin bot;

    public DelayHandler(Plugin bot) {
        DelayHandler.bot = bot;
    }

    public static void sendMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    public static void sendDelayMessage(String message, int delay) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(bot, () -> {
            Bukkit.broadcastMessage(message);
        }, delay);
    }

    public static void sendDelayDiscordMessage(MessageEmbed message, CommandEvent commandEvent, int delay) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(bot, () -> {
            commandEvent.reply(message);
        }, delay);
    }

    public static void delayKickEveryone(int delay, String reason) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(bot, () -> {
            for(Player player : Bukkit.getServer().getOnlinePlayers()){
                player.kickPlayer("Reiniciando el servidor; razón: " + reason);
            }
        }, delay);
    }

    public static void executeDelayCommand(String command, int delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(bot, () -> {
            Bukkit.getServer().dispatchCommand(console, command);
        }, delay);
    }
}