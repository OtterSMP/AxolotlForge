package dev.imlukas.axolotlforgeplugin.utils.item.comparator.types;

import dev.imlukas.axolotlforgeplugin.utils.item.comparator.impl.MaterialComparator;
import dev.imlukas.axolotlforgeplugin.utils.item.comparator.impl.ModelDataComparator;
import dev.imlukas.axolotlforgeplugin.utils.item.comparator.parameter.ItemComparatorParameter;
import org.bukkit.Material;

public class ComparatorTypes {

    public static final ItemComparatorParameter<Integer> MODEl_DATA = new ModelDataComparator(0);
    public static final ItemComparatorParameter<Material> MATERIAL = new MaterialComparator(Material.AIR);
}
