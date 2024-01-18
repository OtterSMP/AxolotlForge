package dev.imlukas.axolotlforgeplugin.multiplier;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ForgeMultipliers {

    private static final Map<String, String> RARITIES_UNICODES = Map.of(
            "common", "\uE11A",
            "rare", "\uE11B",
            "exotic", "\uE11C",
            "mythic", "\uE11D");
    private final Map<String, Double> rarityMultipliers = new HashMap<>();
    private final Map<String, Double> toolMultipliers = new HashMap<>();


    public void addRarityMultiplier(String rarity, double multiplier) {
        rarityMultipliers.put(rarity, multiplier);
    }

    public void addToolMultiplier(String tool, double multiplier) {
        toolMultipliers.put(tool, multiplier);
    }

    public double getMultiplier(ItemStack toRepair) {
        String material = toRepair.getType().name();
        String toolType = material.split("_")[1].toLowerCase(Locale.ROOT);

        ItemMeta meta = toRepair.getItemMeta();
        List<String> lore = meta.getLore();

        double rarityMultiplier = getRarityMultiplier(lore);
        double toolMultiplier = toolMultipliers.getOrDefault(toolType, 1.0);

        return rarityMultiplier * toolMultiplier;
    }

    public double getRarityMultiplier(List<String> lore) {
        if (lore == null) {
            return 1.0;
        }
        for (Map.Entry<String, Double> entry : rarityMultipliers.entrySet()) {
            String rarity = entry.getKey();
            double multiplier = entry.getValue();

            if (lore.contains(RARITIES_UNICODES.get(rarity))) {
                return multiplier;
            }
        }

        return 1.0;
    }
}
