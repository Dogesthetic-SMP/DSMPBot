package me.elJoa.dsmpbot;

import me.elJoa.dsmpbot.minecraftcommands.Inform117;
import org.bukkit.plugin.java.JavaPlugin;

public class Bot extends JavaPlugin {
    private ConfigHandler configHandler;
    private JDAHandler jdaHandler;

    @Override
    public void onEnable() {
        configHandler = new ConfigHandler(this);
        initialize();
    }

    @Override
    public void onDisable() {
        shutdown();
    }

    private void initialize() {
        jdaHandler = new JDAHandler();
        jdaHandler.initializeBot(configHandler);
    }

    private void shutdown() {
        jdaHandler.shutdownBot();
    }
}