package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.SplittableRandom;

public class Fact extends Command {
    private final SplittableRandom random = new SplittableRandom();
    private final SplittableRandom luckyRandom = new SplittableRandom();
    private final String errorDescription = "Vaya, otro error en este bot de mierda. ¿En serio?";
    private int timesExecutedToday = 0;
    private int day = 0;
    private boolean maxedOut = false;

    public Fact() {
        this.name = "fact";
        this.help = "Elige y muestra una fact aleatoria del servidor.";
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

        ArrayList<String> factList = (ArrayList<String>) ConfigHandler.getConfig().getStringList("facts");
        if (factList.isEmpty()) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "Configura las facts en el config.yml, por favor."
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        timesExecutedToday += 1;

        if (maxedOut && LocalDate.now().getDayOfMonth() == day) {
            commandEvent.reply("Muchas cosas para memorizar por un día, vaya a tomarse un descanso dije lpmqlp csm.");
            return;
        }

        if (maxedOut && LocalDate.now().getDayOfMonth() != day) {
            day = 0;
            timesExecutedToday = 1;
            maxedOut = false;
        }

        if (timesExecutedToday > 15) {
            day = LocalDate.now().getDayOfMonth();
            maxedOut = true;
            commandEvent.reply("Muchas cosas para memorizar por un día, vaya a tomarse un descanso.");
            return;
        }

        boolean extremeLuck = luckyRandom.nextInt(1000) == 17;
        boolean lucky = luckyRandom.nextInt(100) == 73;

        if (extremeLuck) {
            boolean fiftyFifty = luckyRandom.nextInt(1, 101) <= 50;
            if (fiftyFifty) {
                commandEvent.reply("Fact EXTREMADAMENTE RARA: Se rumorea que la torre del Corrupted Voss tiene un Easter Egg.");
                return;
            } else {
                commandEvent.reply("Fact EXTREMADAMENTE RARA: -.-. --- .-. .-. ..- .--. - . -.. / ...- --- ... ... / . ... - .- / . -. / . .-.. / .-.. .. -- -... --- --..-- / . ... / .--. .-. --- -... .- -... .-.. . / --.- ..- . / ...- ..- . .-.. ...- .- / .- / .-.. .- / ...- .. -.. .- / -.-- / -- .- ... / ..-. ..- . .-. - . .-.-.");
                return;
            }
        }

        if (lucky) {
            commandEvent.reply("Fact RARA: El bot parece tener una probabilidad muy baja para mostrar secretos de DSMP.");
            return;
        }

        int max = factList.size();
        int randomNumber = random.nextInt(max);
        String selectedFact = factList.get(randomNumber);

        commandEvent.reply("Fact número " + (randomNumber + 1) + ": " + selectedFact);
    }
}