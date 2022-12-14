package net.stonegomes.trial.bukkit.command;

import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.stonegomes.trial.core.Punishment;
import net.stonegomes.trial.core.PunishmentType;
import net.stonegomes.trial.core.user.PunishmentUser;
import org.bukkit.command.CommandSender;

public class UnmuteCommand {

    @Command(
        name = "unmute",
        usage = "unmute <player>",
        permission = "punishments.admin.unmute",
        target = CommandTarget.ALL
    )
    public void handleCommand(Context<CommandSender> context, PunishmentUser user) {
        if (user == null) {
            context.sendMessage("§cThis user has not suffered any punishments on the server.");
            return;
        }

        final Punishment punishment = user.findActivePunishment(
            PunishmentType.TEMPORARY_MUTE,
            PunishmentType.MUTE
        );
        if (punishment == null) {
            context.sendMessage("§cThis user has no active mute at the moment.");
            return;
        }

        punishment.setActive(false);

        context.sendMessage("§eYou unmuted the user §f'" + user.getName() + "'§e successfully.");
    }

}
