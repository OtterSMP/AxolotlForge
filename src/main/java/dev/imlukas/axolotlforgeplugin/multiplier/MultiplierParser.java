package dev.imlukas.axolotlforgeplugin.multiplier;

import dev.imlukas.axolotlforgeplugin.utils.storage.YMLBase;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MultiplierParser extends YMLBase {

    public MultiplierParser(JavaPlugin plugin) {
        super(plugin, "multipliers.yml", true);
    }

    public Map<String, Double> parse(String sectionName) {
        Map<String, Double> rarityMultipliers = new HashMap<>();

        ConfigurationSection multiplierSection = getConfiguration().getConfigurationSection(sectionName);

        for (String rarity : multiplierSection.getKeys(false)) {
            rarityMultipliers.put(rarity, multiplierSection.getDouble(rarity));
        }

        return rarityMultipliers;
    }
}
