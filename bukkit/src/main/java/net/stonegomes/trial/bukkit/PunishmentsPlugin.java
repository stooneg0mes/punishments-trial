package net.stonegomes.trial.bukkit;

import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import net.stonegomes.trial.bukkit.command.*;
import net.stonegomes.trial.bukkit.punishment.user.PunishmentUserCacheImpl;
import net.stonegomes.trial.bukkit.punishment.user.PunishmentUserDaoImpl;
import net.stonegomes.trial.bukkit.punishment.user.PunishmentUserFactoryImpl;
import net.stonegomes.trial.core.user.PunishmentUserCache;
import net.stonegomes.trial.core.user.PunishmentUserDao;
import net.stonegomes.trial.core.user.PunishmentUserFactory;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PunishmentsPlugin extends JavaPlugin {

    private PunishmentUserFactory punishmentUserFactory;
    private PunishmentUserCache punishmentUserCache;
    private PunishmentUserDao punishmentUserDao;

    @Override
    public void onEnable() {
        // Initializers

        punishmentUserFactory = new PunishmentUserFactoryImpl();
        punishmentUserCache = new PunishmentUserCacheImpl();
        punishmentUserDao = new PunishmentUserDaoImpl();

        // Commands

        final BukkitFrame bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(
            new TemporaryMuteCommand(this),
            new TemporaryBanCommand(this),
            new BanCommand(this),
            new KickCommand(this),
            new MuteCommand(this),
            new UnbanCommand(),
            new UnmuteCommand()
        );
    }

}
