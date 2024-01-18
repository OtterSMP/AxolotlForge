package dev.imlukas.axolotlforgeplugin.utils.menu.configuration;

import dev.imlukas.axolotlforgeplugin.utils.item.parser.ItemParser;
import dev.imlukas.axolotlforgeplugin.utils.menu.button.Button;
import dev.imlukas.axolotlforgeplugin.utils.menu.button.DecorationItem;
import dev.imlukas.axolotlforgeplugin.utils.menu.element.MenuElement;
import dev.imlukas.axolotlforgeplugin.utils.menu.layer.BaseLayer;
import dev.imlukas.axolotlforgeplugin.utils.menu.mask.PatternMask;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ConfigurationApplicator {

    private final Map<String, ItemStack> items = new ConcurrentHashMap<>();
    @Getter
    private final PatternMask mask;
    @Getter
    private final FileConfiguration config;
    private final String defaultTitle;

    public ConfigurationApplicator(FileConfiguration config) {
        this.config = config;

        defaultTitle = config.getString("title");
        ConfigurationSection items = config.getConfigurationSection("items");

        for (String key : items.getKeys(false)) {
            this.items.put(key, ItemParser.from(items.getConfigurationSection(key)));
        }

        List<String> maskLayout = config.getStringList("layout");
        mask = PatternMask.of(maskLayout);
    }

    public ItemStack getItem(String key) {
        return items.get(key);
    }

    public Button makeButton(String key) {
        return new Button(getItem(key));
    }

    public DecorationItem getDecorationItem(String key) {
        return new DecorationItem(getItem(key));
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public Button registerButton(BaseLayer layer, String key) {
        ItemStack item = getItem(key);

        if (item == null) {
            throw new IllegalArgumentException("No item with key " + key + " found! (items: " + items.keySet() + ")");
        }

        Button button = new Button(item);
        layer.applyRawSelection(mask.selection(key), button);

        return button;
    }

    public Button registerButton(BaseLayer layer, String key, Consumer<InventoryClickEvent> defaultHandler) {
        Button button = new Button(getItem(key), defaultHandler);
        layer.applyRawSelection(mask.selection(key), button);

        return button;
    }

    public Button registerButton(BaseLayer layer, String key, Runnable clickHandler) {
        Button button = new Button(getItem(key), (event) -> clickHandler.run());
        layer.applyRawSelection(mask.selection(key), button);

        return button;
    }

    public void registerButton(BaseLayer layer, String key, Button button) {
        layer.applyRawSelection(mask.selection(key), button);
    }

    public Button createButton(String key, Consumer<InventoryClickEvent> defaultHandler) {
        return new Button(getItem(key).clone(), defaultHandler);
    }

    public void applyConfiguration(BaseLayer layer) {
        for (Map.Entry<String, ItemStack> entry : items.entrySet()) {
            String itemId = entry.getKey();
            ItemStack item = entry.getValue();

            MenuElement element = new DecorationItem(item);

            layer.applySelection(mask.selection(itemId), element);
        }
    }
}
