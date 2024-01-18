package dev.imlukas.axolotlforgeplugin.utils.menu.template;

import dev.imlukas.axolotlforgeplugin.AxolotlForgePlugin;
import dev.imlukas.axolotlforgeplugin.utils.menu.base.ConfigurableMenu;
import dev.imlukas.axolotlforgeplugin.utils.menu.concurrency.MainThreadExecutor;
import dev.imlukas.axolotlforgeplugin.utils.menu.configuration.ConfigurationApplicator;
import dev.imlukas.axolotlforgeplugin.utils.menu.registry.MenuRegistry;
import dev.imlukas.axolotlforgeplugin.utils.menu.registry.meta.HiddenMenuTracker;
import dev.imlukas.axolotlforgeplugin.utils.storage.Messages;
import dev.imlukas.axolotlforgeplugin.utils.text.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;

public abstract class Menu {

    protected final AxolotlForgePlugin plugin;
    protected final Messages messages;
    protected final ConfigurableMenu menu;
    protected final ConfigurationApplicator applicator;

    private final MenuRegistry menuRegistry;
    private final HiddenMenuTracker hiddenMenuTracker;
    private final UUID viewerId;

    protected Menu(AxolotlForgePlugin plugin, Player viewer) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.menuRegistry = plugin.getMenuRegistry();
        this.hiddenMenuTracker = plugin.getMenuRegistry().getHiddenMenuTracker();
        this.viewerId = viewer.getUniqueId();
        this.menu = createMenu();
        this.applicator = menu.getApplicator();
    }

    /**
     * Handles creation of the menu, definition of variables and static button creation.
     */
    public abstract void setup();

    public void open() {
        menu.open();
    }

    public void close() {
        Player viewer = getViewer();

        if (viewer.getOpenInventory().getTopInventory().equals(menu.getInventory())) {
            if (Bukkit.isPrimaryThread()) {
                viewer.closeInventory();
            } else {
                MainThreadExecutor.INSTANCE.execute(viewer::closeInventory); // fuck you bukkit
            }
        }
    }

    public ConfigurableMenu createMenu() {
        return (ConfigurableMenu) menuRegistry.create(getIdentifier(), getViewer());
    }

    public abstract String getIdentifier();

    public Player getViewer() {
        return Bukkit.getPlayer(viewerId);
    }

    public ConfigurableMenu getMenu() {
        return menu;
    }

    public Menu onClose(Runnable onClose) {
        menu.onClose(onClose);
        return this;
    }

    public void onPlayerInventoryClick(Consumer<ItemStack> action) {
        menu.setOnPlayerItemClick(action);
    }

    public void holdForInput(Consumer<String> action) {
        holdForInput(action, true);
    }

    @SafeVarargs
    public final void holdForInput(Consumer<String> action, String inputMessage, Placeholder<Player>... placeholders) {
        messages.sendMessage(getViewer(), inputMessage, placeholders);
        holdForInput(action, true);
    }

    public void holdForInput(Consumer<String> action, String inputMessage) {
        messages.sendMessage(getViewer(), inputMessage);
        holdForInput(action, true);
    }

    public void holdForInput(Consumer<String> action, boolean reopenMenu) {
        hiddenMenuTracker.holdForInput(menu, action, reopenMenu);
    }
}
