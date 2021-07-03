package me.elJoa.dsmpbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.elJoa.dsmpbot.ConfigHandler;
import me.elJoa.dsmpbot.JDAHandler;
import me.elJoa.dsmpbot.utilities.ArgsHandler;
import me.elJoa.dsmpbot.utilities.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class Debate extends Command {
    private final String errorDescription = "Welp, don't worry be happy!";
    private final String successDescription = "Nice! Now fuck each other's arguments.";

    public Debate() {
        this.name = "debate";
        this.help = "Crea un canal privado para debatir entre diferentes personas.";
        this.aliases = new String[] {"d"};
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

        Guild guild = commandEvent.getGuild();
        if (!guild.getName().startsWith("Dogesthetic SMP")) {
            return;
        }

        String debateName = ArgsHandler.getFirstArgument(commandEvent, false, false);
        List<Member> mentionedMembers = commandEvent.getMessage().getMentionedMembers();
        Member author = commandEvent.getMember();

        if (mentionedMembers.size() == 0) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "¡Usa bien el comando! Uso: $debate <Título> @DrPuc @PuerkosBender @Lil Doksbol"
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        if (debateName.startsWith("<@")) {
            EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                    errorDescription,
                    "¡Usa bien el comando! Uso: $debate <Título> @DrPuc @PuerkosBender @Lil Doksbol"
            );
            commandEvent.reply(errorEmbed.build());
            return;
        }

        if (debateName.length() > 24) {
            debateName = debateName.substring(0, 24);
        }

        for (TextChannel channel : guild.getTextChannels()) {
            if (channel.getName().equalsIgnoreCase(debateName)) {
                EmbedBuilder errorEmbed = EmbedHelper.newErrorEmbed(
                        errorDescription,
                        "Wow, ¿crees que eres inteligente?"
                );
                commandEvent.reply(errorEmbed.build());
                return;
            }
        }

        net.dv8tion.jda.api.entities.Category debateCategory = guild.getCategoryById(ConfigHandler.getSetting("debateId"));

        ChannelAction<TextChannel> creationTextChannel = guild.createTextChannel(debateName, debateCategory);
        // Permite al autor y bot para la lectura del canal
        creationTextChannel = creationTextChannel.addPermissionOverride(author, EnumSet.of(Permission.VIEW_CHANNEL), null);
        creationTextChannel = creationTextChannel.addPermissionOverride(Objects.requireNonNull(guild.getMemberById(ConfigHandler.getSetting("botId"))), EnumSet.of(Permission.VIEW_CHANNEL), null);

        // Permite a las personas mencionadas para la lectura del canal
        for (Member mentionedMember : mentionedMembers) {
            creationTextChannel = creationTextChannel.addPermissionOverride(mentionedMember, EnumSet.of(Permission.VIEW_CHANNEL), null);
        }

        // No permite que @everyone lea el canal
        creationTextChannel = creationTextChannel.addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL));

        // Permite que Doksbol y DrPuc lean el canal (por moderación)
        creationTextChannel = creationTextChannel.addPermissionOverride(Objects.requireNonNull(guild.getMemberById("401068186113409024")), EnumSet.of(Permission.VIEW_CHANNEL), null);
        creationTextChannel = creationTextChannel.addPermissionOverride(Objects.requireNonNull(guild.getMemberById("401068186113409024")), EnumSet.of(Permission.VIEW_CHANNEL), null);

        // Crea el canal
        creationTextChannel.queue();

        String[] titles = {"Canal de debate creado"};
        String[] values = {"Empieza a debatir en el canal llamado " + debateName};

        EmbedBuilder successEmbed = EmbedHelper.newEmbedWithArrays(true, successDescription, titles, values);
        commandEvent.reply(successEmbed.build());
    }
}