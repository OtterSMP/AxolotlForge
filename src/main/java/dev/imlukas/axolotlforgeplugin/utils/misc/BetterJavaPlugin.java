package dev.imlukas.axolotlforgeplugin.utils.misc;

import dev.imlukas.axolotlforgeplugin.utils.io.FileUtils;
import dev.imlukas.axolotlforgeplugin.utils.menu.concurrency.MainThreadExecutor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterJavaPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        MainThreadExecutor.init(this);
        FileUtils.copyBuiltInResources(this, getFile());
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerCommand(String name, CommandExecutor executor) {
        getCommand(name).setExecutor(executor);
    }
}
