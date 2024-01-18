package dev.imlukas.axolotlforgeplugin.utils.item.comparator.impl;

import dev.imlukas.axolotlforgeplugin.utils.item.comparator.parameter.ItemComparatorParameter;
import org.bukkit.inventory.ItemStack;

public class ModelDataComparator extends ItemComparatorParameter<Integer> {

    public ModelDataComparator(Integer value) {
        super(value);
    }

    @Override
    public boolean compare(ItemStack value) {
        if (!value.hasItemMeta()) {
            return false;
        }

        if (!value.getItemMeta().hasCustomModelData()) {
            return false;
        }

        int modelData = value.getItemMeta().getCustomModelData();
        return getValue() == modelData;
    }
}
