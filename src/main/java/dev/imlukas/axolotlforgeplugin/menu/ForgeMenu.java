package dev.imlukas.axolotlforgeplugin.menu;

import dev.imlukas.axolotlforgeplugin.AxolotlForgePlugin;
import dev.imlukas.axolotlforgeplugin.utils.item.comparator.ItemComparator;
import dev.imlukas.axolotlforgeplugin.utils.item.comparator.types.ComparatorTypes;
import dev.imlukas.axolotlforgeplugin.multiplier.ForgeMultipliers;
import dev.imlukas.axolotlforgeplugin.utils.item.ItemUtil;
import dev.imlukas.axolotlforgeplugin.utils.menu.button.Button;
import dev.imlukas.axolotlforgeplugin.utils.menu.layer.BaseLayer;
import dev.imlukas.axolotlforgeplugin.utils.menu.template.Menu;
import dev.imlukas.axolotlforgeplugin.utils.misc.PluginSettings;
import dev.imlukas.axolotlforgeplugin.utils.storage.SoundManager;
import dev.imlukas.axolotlforgeplugin.utils.text.Placeholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;
import java.util.Set;

public class ForgeMenu extends Menu {

    private final SoundManager sounds;
    private final Economy economy;
    private final ForgeMultipliers forgeMultipliers;
    private final double baseValue;
    private final int baseRunes;
    private final ItemComparator itemComparator;

    public ForgeMenu(AxolotlForgePlugin plugin, Player viewer) {
        super(plugin, viewer);
        PluginSettings settings = plugin.getPluginSettings();
        this.sounds = plugin.getSoundManager();
        this.forgeMultipliers = plugin.getForgeMultipliers();
        this.economy = plugin.getEconomy();
        this.baseValue = settings.getBaseRepairValue();
        this.baseRunes = settings.getBaseRunes();

        this.itemComparator = new ItemComparator();
        itemComparator.enable(ComparatorTypes.MODEl_DATA, 67);
        itemComparator.enable(ComparatorTypes.MATERIAL, Material.FLINT);
        setup();
    }

    @Override
    public void setup() {
        Player player = getViewer();
        BaseLayer layer = new BaseLayer(menu);
        menu.addRenderable(layer);

        ItemStack defaultRepair = applicator.getItem("r");
        ItemStack defaultResult = applicator.getItem("u");

        Button repairButton = applicator.registerButton(layer, "r");
        Button resultButton = applicator.registerButton(layer, "u");

        menu.setOnPlayerItemClick(itemStack -> {
            if (!ItemUtil.isTool(itemStack)) {
                messages.sendMessage(player, "cant-repair");
                return;
            }

            Damageable damageable = (Damageable) itemStack.getItemMeta();

            if (!damageable.hasDamage()) {
                messages.sendMessage(player, "not-damaged");
                return;
            }

            double multiplier = forgeMultipliers.getMultiplier(itemStack);
            double cost = baseValue * multiplier;
            int runes = (int) (baseRunes * multiplier);

            List<Placeholder<Player>> placeholders = List.of(
                    new Placeholder<>("value", String.valueOf(cost)),
                    new Placeholder<>("runes", runes));

            ItemStack clone = itemStack.clone();
            List<String> loreToAdd = applicator.getConfig().getStringList("lore-to-add");
            ItemUtil.addLore(clone, loreToAdd);

            repairButton.setDisplayItem(clone);
            repairButton.setItemPlaceholders(placeholders);
            repairButton.setLeftClickAction(() -> {
                if (!economy.has(getViewer(), cost)) {
                    messages.sendMessage(player, "not-enough-money");
                    sounds.playSound(player, "error");
                    return;
                }

                if (!removeRunes(player, runes)) {
                    messages.sendMessage(player, "not-enough-runes");
                    sounds.playSound(player, "error");
                    return;
                }

                economy.withdrawPlayer(player, cost);
                repairItem(itemStack);

                sounds.playSound(player, "repaired");
                messages.sendMessage(player, "repaired");

                repairButton.setDisplayItem(defaultRepair);
                resultButton.setDisplayItem(defaultResult);
                menu.forceUpdate();
            });

            ItemStack repairedClone = repairItem(itemStack.clone());
            resultButton.setDisplayItem(repairedClone);
            menu.forceUpdate();
        });

    }
    @Override
    public String getIdentifier() {
        return "forge";
    }

    public boolean removeRunes(Player player, int runes) {
        Inventory inventory = player.getInventory();
        Set<ItemStack> foundRunes = itemComparator.getExisting(inventory);

        if (foundRunes.isEmpty()) {
            return false;
        }

        int tempRunes = runes;

        for (ItemStack itemStack : foundRunes) {
            int amount = itemStack.getAmount();

            if (tempRunes == 0) {
                return true;
            }

            if (amount == tempRunes) {
                inventory.removeItem(itemStack);
                return true;
            }

            if (amount > tempRunes) {
                itemStack.setAmount(amount - tempRunes);
                return true;
            }

            inventory.removeItem(itemStack);
            tempRunes = tempRunes - amount;
        }
        return false;
    }

    public ItemStack repairItem(ItemStack itemStack) {
        Damageable damageable = (Damageable) itemStack.getItemMeta();
        damageable.setDamage(0);
        itemStack.setItemMeta(damageable);
        return itemStack;
    }
}
