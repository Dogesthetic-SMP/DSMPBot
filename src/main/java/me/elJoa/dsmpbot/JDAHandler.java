package me.elJoa.dsmpbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.commands.*;
import me.elJoa.dsmpbot.hooks.PlanHook;
import me.elJoa.dsmpbot.listeners.GuildMemberJoin;
import me.elJoa.dsmpbot.minecraftcommands.Inform117;
import me.elJoa.dsmpbot.utilities.DelayHandler;
import me.elJoa.dsmpbot.utilities.PerfHandler;
import me.elJoa.dsmpbot.utilities.QueryAPIAccessor;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.util.HashSet;
import java.util.Set;

public class JDAHandler {
    public final Set<Command> commandSet = new HashSet<>();
    private final String token = ConfigHandler.getSetting("token");
    private JDA jda = null;
    private boolean planIsInstalled = true;
    private QueryAPIAccessor queryAPIAccessor;
    public static boolean botBug = false;
    public static boolean botDevMode = false;

    public static boolean shouldExecuteDevMode(CommandEvent commandEvent) {
        Member author = commandEvent.getMember();
        return ConfigHandler.getAdmins().contains(author.getId());
    }

    public void initializeBot(ConfigHandler configHandler) {
        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(token);
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            jdaBuilder.enableIntents(GatewayIntent.GUILD_PRESENCES);
            jdaBuilder.addEventListeners(new GuildMemberJoin());
            jda = jdaBuilder.build();
        } catch (IllegalArgumentException | LoginException e) {
            ConfigHandler.getLogger().severe("Error iniciando sesión, por favor verifique su token.");
        }

        if (jda != null && !token.equals("")) {
            CommandClientBuilder builder = new CommandClientBuilder();
            initializeCommandClientBuilder(builder);

            try {
                queryAPIAccessor = new PlanHook().hookIntoPlan().get();
            } catch (Exception e) {
                planIsInstalled = false;
                ConfigHandler.getLogger().severe("Plan no está instalado o no se pudo acceder; Statistics deshabilitado.");
                ConfigHandler.getLogger().info(e.toString());
            }

            Spark spark = SparkProvider.get();
            CommandClient commandClient = builder.build();
            addCommands(commandClient, configHandler);

            jda.addEventListener(commandClient);
            new DelayHandler(configHandler.getBot());
            new PerfHandler(spark);
            configHandler.getBot().getCommand("informoneseventeen").setExecutor(new Inform117(jda));
        }
    }

    private void initializeCommandClientBuilder(CommandClientBuilder builder) {
        builder.setPrefix(ConfigHandler.getSetting("prefix"));
        builder.setActivity(Activity.playing("Dogesthetic SMP"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setOwnerId(ConfigHandler.getAdmin(0));
        builder.setHelpWord("comandos");
        builder.setHelpConsumer(new Comandos(commandSet));
    }

    public void shutdownBot() {
        jda.shutdownNow();
    }

    private void addCommands(CommandClient commandClient, ConfigHandler configHandler) {
        // Comandos
        commandSet.add(new Estado());
        commandSet.add(new DiscordAChat());
        commandSet.add(new IP());
        commandSet.add(new Fact());
        commandSet.add(new Info());
        commandSet.add(new Trailer());
        commandSet.add(new DogeDice());
        commandSet.add(new EstadoFAQ());
        commandSet.add(new BajosFPS());
        commandSet.add(new Estadok());
//      commandSet.add(new Debate());
//      commandSet.add(new FinalizarDebate());

        // Comandos administrativos
        commandSet.add(new Recargar(configHandler));
        commandSet.add(new SimularBug());
        commandSet.add(new Reiniciar());
        commandSet.add(new Limpiar());
        commandSet.add(new DiscordAConsola(configHandler));
        commandSet.add(new DevMode());

        if (planIsInstalled) {
            commandSet.add(new Statistics(queryAPIAccessor));
        }

        commandSet.forEach(commandClient::addCommand);
    }
}