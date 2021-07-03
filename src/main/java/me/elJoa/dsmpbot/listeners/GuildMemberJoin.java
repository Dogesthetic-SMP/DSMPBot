package me.elJoa.dsmpbot.listeners;

import me.elJoa.dsmpbot.ConfigHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class GuildMemberJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();
        List<Role> roles = event.getGuild().getRoles();

        for (Role role : roles) {
            if (role.getName().equals("In/gamemode 3")) {
                event.getGuild().addRoleToMember(member, role).queue();
                return;
            }
        }

        ConfigHandler.getLogger().warning("No encontré el rol In/gamemode 3 para el usuario " + member.getEffectiveName() + ". Verifique el servidor de Discord.");
    }
}