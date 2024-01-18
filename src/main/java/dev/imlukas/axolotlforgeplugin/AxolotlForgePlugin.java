package dev.imlukas.axolotlforgeplugin;

import dev.imlukas.axolotlforgeplugin.commands.ForgeMenuCommand;
import dev.imlukas.axolotlforgeplugin.multiplier.ForgeMultipliers;
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
    private FileConfiguration multiplierConfig;

    @Override
    public void onEnable() {
        super.onEnable();
        this.pluginSettings = new PluginSettings(this.getConfig());
        setupEconomy();
        this.messages = new Messages(this);
        this.soundManager = new SoundManager(this);
        this.menuRegistry = new MenuRegistry(this);
        this.forgeMultipliers = new ForgeMultipliers();

        YMLBase base = new YMLBase(this, "multipliers.yml", true);
        multiplierConfig = base.getConfiguration();

        for (Map.Entry<String, Double> rarity : getRarityMultipliers("rarity").entrySet()) {
            forgeMultipliers.addRarityMultiplier(rarity.getKey(), rarity.getValue());
        }

        for (Map.Entry<String, Double> tool : getRarityMultipliers("tool").entrySet()) {
            forgeMultipliers.addToolMultiplier(tool.getKey(), tool.getValue());
        }

        this.commandManager = new CommandManager(this, messages);

        registerCommand(new ForgeMenuCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Map<String, Double> getRarityMultipliers(String sectionName) {
        Map<String, Double> rarityMultipliers = new HashMap<>();

        ConfigurationSection multiplierSection = multiplierConfig.getConfigurationSection(sectionName);

        for (String rarity : multiplierSection.getKeys(false)) {
            rarityMultipliers.put(rarity, multiplierSection.getDouble(rarity));
        }

        return rarityMultipliers;
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
