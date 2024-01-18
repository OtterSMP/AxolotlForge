package dev.imlukas.axolotlforgeplugin.commands;

import dev.imlukas.axolotlforgeplugin.AxolotlForgePlugin;
import dev.imlukas.axolotlforgeplugin.menu.ForgeMenu;
import dev.imlukas.axolotlforgeplugin.utils.command.command.impl.AdvancedCommand;
import dev.imlukas.axolotlforgeplugin.utils.command.command.impl.ExecutionContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForgeMenuCommand extends AdvancedCommand {

    private final AxolotlForgePlugin plugin;
    public ForgeMenuCommand(AxolotlForgePlugin plugin) {
        super("forge");
        this.plugin = plugin;
    }

    @Override
    public String getPermission() {
        return "axolotlforge.forge";
    }

    @Override
    public void execute(CommandSender sender, ExecutionContext context) {
        if (!(sender instanceof Player player)) {
            return;
        }

        new ForgeMenu(plugin, player).open();
    }
}
