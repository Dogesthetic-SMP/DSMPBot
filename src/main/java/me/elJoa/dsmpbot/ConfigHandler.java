package me.elJoa.dsmpbot;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ConfigHandler {
    private static Logger logger;
    private static FileConfiguration config;
    private static Set<String> adminsInSet;
    private static String[] adminsInArray;
    private final JavaPlugin bot;

    public ConfigHandler(final JavaPlugin bot) {
        this.bot = bot;
        logger = bot.getLogger();
        config = bot.getConfig();

        reloadConfig();
    }

    public void reloadConfig() {
        bot.saveDefaultConfig();
        bot.reloadConfig();
        config = bot.getConfig();

        String adminsWithCommas = getSetting("admins");
        adminsInArray = adminsWithCommas.split(",");
        adminsInSet = new HashSet<>(
                Arrays.asList(adminsInArray)
        );
    }

    public static Set<String> getAdmins() {
        return adminsInSet;
    }

    public static String getAdmin(int admin) {
        return adminsInArray[admin];
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String getSetting(String setting) {
        return config.getString(setting);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public JavaPlugin getBot() {
        return bot;
    }
}