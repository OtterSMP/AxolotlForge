package dev.imlukas.axolotlforgeplugin;

import dev.imlukas.axolotlforgeplugin.commands.ForgeMenuCommand;
import dev.imlukas.axolotlforgeplugin.multiplier.ForgeMultipliers;
import dev.imlukas.axolotlforgeplugin.multiplier.MultiplierParser;
import dev.imlukas.axolotlforgeplugin.utils.command.command.CommandManager;
import dev.imlukas.axolotlforgeplugin.utils.command.command.impl.AdvancedCommand;
import dev.imlukas.axolotlforgeplugin.utils.menu.registry.MenuRegistry;
import dev.imlukas.axolotlforgeplugin.utils.misc.BetterJavaPlugin;
import dev.imlukas.axolotlforgeplugin.utils.misc.PluginSettings;
import dev.imlukas.axolotlforgeplugin.utils.storage.Messages;
import dev.imlukas.axolotlforgeplugin.utils.storage.SoundManager;
import dev.imlukas.axolotlforgeplugin.utils.storage.YMLBase;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class AxolotlForgePlugin extends BetterJavaPlugin {

    private PluginSettings pluginSettings;
    private Economy economy;
    private Messages messages;
    private SoundManager soundManager;
    private MenuRegistry menuRegistry;
    private CommandManager commandManager;

    private ForgeMultipliers forgeMultipliers;

    @Override
    public void onEnable() {
        FileConfiguration configuration = getConfig();
        super.onEnable();
        this.pluginSettings = new PluginSettings(this.getConfig());
        setupEconomy();
        this.messages = new Messages(this);
        this.soundManager = new SoundManager(this);
        this.menuRegistry = new MenuRegistry(this);
        this.commandManager = new CommandManager(this, messages);
        this.forgeMultipliers = new ForgeMultipliers();
        MultiplierParser parser = new MultiplierParser(this);
        parser.parse("rarity").forEach(forgeMultipliers::addRarityMultiplier);
        parser.parse("tool").forEach(forgeMultipliers::addToolMultiplier);

        configuration.getConfigurationSection("unicodes").getKeys(false).forEach(key -> {
            String unicode = configuration.getString("unicodes." + key);
            forgeMultipliers.addUnicode(key, unicode);
        });

        registerCommand(new ForgeMenuCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void registerCommand(AdvancedCommand command) {
        commandManager.registerCommand(command);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
}
