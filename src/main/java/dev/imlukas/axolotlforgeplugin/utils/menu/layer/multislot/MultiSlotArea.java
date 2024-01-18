package dev.imlukas.axolotlforgeplugin.utils.menu.layer.multislot;

import dev.imlukas.axolotlforgeplugin.utils.menu.base.BaseMenu;
import dev.imlukas.axolotlforgeplugin.utils.menu.button.Button;
import dev.imlukas.axolotlforgeplugin.utils.menu.element.MenuElement;
import dev.imlukas.axolotlforgeplugin.utils.menu.selection.Selection;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MultiSlotArea {

    private final List<Integer> slots;
    private final List<MenuElement> elements = new ArrayList<>();
    @Setter
    private MenuElement emptyElement = new Button(new ItemStack(Material.AIR));

    public MultiSlotArea(Selection selection) {
        this.slots = selection.getSlots();
    }

    public MultiSlotArea(Selection selection, MenuElement emptyElement) {
        this(selection);
        this.emptyElement = emptyElement;
    }

    public void clear() {
        elements.clear();
    }

    public void addElement(MenuElement element) {
        if (elements.size() < slots.size()) {
            elements.add(element);
        }
    }

    public void removeElement(MenuElement element) {
        elements.remove(element);
    }

    public void forceUpdate(BaseMenu menu) {

        for (int index = 0; index < slots.size(); index++) {
            int slot = slots.get(index);

            if (index >= elements.size()) {
                menu.setElement(slot, emptyElement);
            } else {
                menu.setElement(slot, elements.get(index));
            }
        }
    }

}
