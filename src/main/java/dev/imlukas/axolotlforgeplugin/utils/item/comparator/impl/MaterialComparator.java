package dev.imlukas.axolotlforgeplugin.utils.item.comparator.impl;

import dev.imlukas.axolotlforgeplugin.utils.item.comparator.parameter.ItemComparatorParameter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialComparator extends ItemComparatorParameter<Material> {

    public MaterialComparator(Material value) {
        super(value);
    }

    @Override
    public boolean compare(ItemStack toCompare) {
        Material material = toCompare.getType();
        return getValue().equals(material);
    }

}
