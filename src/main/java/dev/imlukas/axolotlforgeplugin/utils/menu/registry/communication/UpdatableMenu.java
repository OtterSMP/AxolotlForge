package dev.imlukas.axolotlforgeplugin.utils.menu.registry.communication;

import dev.imlukas.axolotlforgeplugin.AxolotlForgePlugin;
import dev.imlukas.axolotlforgeplugin.utils.menu.template.Menu;
import org.bukkit.entity.Player;

public abstract class UpdatableMenu extends Menu {

    protected UpdatableMenu(AxolotlForgePlugin plugin, Player viewer) {
        super(plugin, viewer);
    }

    /**
     * Handles refreshing placeholders and updating buttons and other elements accordingly.
     */
    public abstract void refresh();
}
