package dev.imlukas.axolotlforgeplugin.utils.item.comparator.parameter;

import org.bukkit.inventory.ItemStack;

public abstract class ItemComparatorParameter<T> {

    private T value;

    public ItemComparatorParameter(T value) {
        this.value = value;
    }

    public abstract boolean compare(ItemStack toCompare);

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
