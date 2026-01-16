package cc.thonly.polydex2rei.util;

import cc.thonly.polydex2rei.Polydex2REI;
import cc.thonly.polydex2rei.network.Action;
import cc.thonly.polydex2rei.plugin.Polydex2REICategory;
import cc.thonly.polydex2rei.plugin.Polydex2REIDisplay;
import eu.pb4.polymer.core.api.client.ClientPolymerItem;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.display.DisplaySpec;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class ItemUtils {
    public static int PRINT_ID = 0;

    public static ItemStack getServerStack(ItemStack itemStack) {
        Optional<String> itemModelId = ClientPolymerItemUtils.getItemModelId(itemStack);
        if (itemModelId.isEmpty()) {
            return itemStack;
        }
        String strId = itemModelId.get();
        Identifier itemId = Identifier.of(strId);
        ClientPolymerItem clientPolymerItem = ClientPolymerItem.REGISTRY.get(itemId);
        if (clientPolymerItem != null) {
            return clientPolymerItem.visualStack().copyWithCount(itemStack.getCount());
        }
        return itemStack;
    }

    public static Map<DisplayCategory<?>, List<DisplaySpec>> filter(Map<DisplayCategory<?>, List<DisplaySpec>> categoriesMap) {
        Map<DisplayCategory<?>, List<DisplaySpec>> newMap = new LinkedHashMap<>();
        for (Map.Entry<DisplayCategory<?>, List<DisplaySpec>> mapEntry : categoriesMap.entrySet()) {
            DisplayCategory<?> category = mapEntry.getKey();
            List<DisplaySpec> displaySpecs = mapEntry.getValue();
            List<DisplaySpec> newDisplaySpecs = new ArrayList<>();
            for (DisplaySpec displaySpec : displaySpecs) {
                Display display = displaySpec.provideInternalDisplay();
                boolean matched = false;

                if (Polydex2REI.ACTION == Action.LEFT) {
                    for (EntryIngredient outputEntry : display.getOutputEntries()) {
                        for (EntryStack<?> entryStack : outputEntry) {
                            Object value = entryStack.getValue();
                            if (!(value instanceof ItemStack itemStack)) continue;
//                            printfItemStack(itemStack);

                            if (ItemStack.areItemsAndComponentsEqual(
                                    Polydex2REI.TARGET_ITEM_STACK,
                                    itemStack
                            )) {
                                matched = true;
                                break;
                            }
                        }
                        if (matched) break;
                    }
                } else if (Polydex2REI.ACTION == Action.RIGHT) {
                    for (EntryIngredient inputEntry : display.getInputEntries()) {
                        for (EntryStack<?> entryStack : inputEntry) {
                            Object value = entryStack.getValue();
                            if (!(value instanceof ItemStack itemStack)) continue;

                            if (ItemStack.areItemsAndComponentsEqual(
                                    Polydex2REI.TARGET_ITEM_STACK,
                                    itemStack
                            )) {
                                matched = true;
                                break;
                            }
                        }
                        if (matched) break;
                    }
                } else {
                    matched = true;
                }

                if (matched) {
                    newDisplaySpecs.add(displaySpec);
                }
            }
            if (!newDisplaySpecs.isEmpty()) {
                newMap.put(category, newDisplaySpecs);
            }
        }
        newMap.put(Polydex2REICategory.INSTANCE, new ArrayList<>(List.of(new Polydex2REIDisplay())));
        return newMap;
    }

    public static void printfItemStack(ItemStack itemStack) {
        System.out.println("id" + PRINT_ID + itemStack);
        ComponentChanges componentChanges = itemStack.getComponentChanges();
        for (Map.Entry<ComponentType<?>, Optional<?>> mapEntry : componentChanges.entrySet()) {
            Optional<?> opt = mapEntry.getValue();
            System.out.println(Registries.DATA_COMPONENT_TYPE.getId(mapEntry.getKey()) + " == " + (opt.isEmpty() ? "null" : opt.get()));
        }
        PRINT_ID++;
    }
}
