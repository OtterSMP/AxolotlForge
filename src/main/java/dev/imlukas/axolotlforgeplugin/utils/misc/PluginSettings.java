package dev.imlukas.axolotlforgeplugin.utils.misc;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class PluginSettings {

    private final FileConfiguration config;
    private double baseRepairValue;
    private int baseRunes;

    public PluginSettings(FileConfiguration configuration) {
        this.config = configuration;
        load();
    }

    public void load() {
        baseRepairValue = config.getInt("base-value");
        baseRunes = config.getInt("base-runes");
    }
}
