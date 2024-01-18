package dev.imlukas.axolotlforgeplugin.commands;

import dev.imlukas.axolotlforgeplugin.utils.command.command.impl.AdvancedCommand;
import dev.imlukas.axolotlforgeplugin.utils.command.command.impl.ExecutionContext;
import dev.imlukas.axolotlforgeplugin.utils.command.language.type.Parameter;
import dev.imlukas.axolotlforgeplugin.utils.command.language.type.ParameterType;
import dev.imlukas.axolotlforgeplugin.utils.command.language.type.ParameterTypes;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveRepairRunes extends AdvancedCommand {
    public GiveRepairRunes() {
        super("forgerunes <amount>");

        registerParameter(new Parameter<>("amount", ParameterTypes.INTEGER, true));
    }

    @Override
    public String getPermission() {
        return "axolotlforge.runes";
    }

    @Override
    public void execute(CommandSender sender, ExecutionContext context) {
        ItemStack rune = new ItemStack(Material.FLINT, context.getParameter("amount"));
        ItemMeta meta = rune.getItemMeta();
        meta.setDisplayName("§eRepair Rune");
        meta.setCustomModelData(67);
        rune.setItemMeta(meta);
        Player player = (Player) sender;

        player.getInventory().addItem(rune);
        sender.sendMessage("§aYou have been given §e" + context.getParameter("amount") + " §arunes.");
    }
}
