package net.stonegomes.trial.bukkit;

import de.leonhard.storage.SimplixBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ReloadSettings;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.ViewFrame;
import net.stonegomes.trial.bukkit.command.*;
import net.stonegomes.trial.bukkit.listener.AsyncPlayerChatListener;
import net.stonegomes.trial.bukkit.listener.PlayerLoginListener;
import net.stonegomes.trial.bukkit.punishment.user.PunishmentUserCacheImpl;
import net.stonegomes.trial.bukkit.punishment.user.PunishmentUserDaoImpl;
import net.stonegomes.trial.bukkit.punishment.user.PunishmentUserFactoryImpl;
import net.stonegomes.trial.bukkit.view.HistoryPaginatedView;
import net.stonegomes.trial.core.user.PunishmentUserCache;
import net.stonegomes.trial.core.user.PunishmentUserDao;
import net.stonegomes.trial.core.user.PunishmentUserFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class PunishmentsPlugin extends JavaPlugin {

    private Yaml storage;

    private PunishmentUserFactory punishmentUserFactory;
    private PunishmentUserCache punishmentUserCache;
    private PunishmentUserDao punishmentUserDao;

    private ViewFrame viewFrame;


    @Override
    public void onEnable() {
        // Storage

        final File storageFile = new File(getDataFolder(), "storage.yml");
        storage = SimplixBuilder.fromFile(storageFile)
            .setReloadSettings(ReloadSettings.INTELLIGENT)
            .createConfig();

        // Punishment

        punishmentUserFactory = new PunishmentUserFactoryImpl();
        punishmentUserCache = new PunishmentUserCacheImpl();
        punishmentUserDao = new PunishmentUserDaoImpl(punishmentUserFactory, storage);

        // Views

        viewFrame = ViewFrame.of(this)
            .with(new HistoryPaginatedView())
            .register();

        // Commands

        final BukkitFrame bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(
            new TemporaryMuteCommand(punishmentUserFactory, punishmentUserCache),
            new TemporaryBanCommand(punishmentUserFactory, punishmentUserCache),
            new KickCommand(punishmentUserFactory, punishmentUserCache),
            new MuteCommand(punishmentUserFactory, punishmentUserCache),
            new BanCommand(punishmentUserFactory, punishmentUserCache),
            new HistoryCommand(viewFrame),
            new UnbanCommand(),
            new UnmuteCommand()
        );

        // Listeners

        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(punishmentUserCache), this);
        pluginManager.registerEvents(new PlayerLoginListener(punishmentUserCache), this);
    }

}
