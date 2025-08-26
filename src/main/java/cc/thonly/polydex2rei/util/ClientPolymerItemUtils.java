package cc.thonly.polydex2rei.util;

import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ClientPolymerItemUtils {
    public static final String POLYMER_ID = PolymerItemUtils.POLYMER_STACK;
    public static final String REI_ITEM_ID = "$rei_item_id";
    public static final String POLY_MC_ID = "PolyMcOriginal";
    public static final String DEFAULT_ITEM_ID = Identifier.ofVanilla("stone").toString();

    public static ItemStack parseToEiv(ItemStack itemStack) {
        ItemStack result = new ItemStack(Items.TRIAL_KEY);
        applyIfPresent(result, itemStack, DataComponentTypes.ITEM_NAME);
        applyIfPresent(result, itemStack, DataComponentTypes.CUSTOM_NAME);
        applyIfPresent(result, itemStack, DataComponentTypes.ITEM_MODEL);
        applyIfPresent(result, itemStack, DataComponentTypes.CUSTOM_MODEL_DATA);
        applyIfPresent(result, itemStack, DataComponentTypes.DYED_COLOR);
        NbtComponent nbtComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null) {
            Optional<String> polymerStackId = getPolymerStackId(itemStack);
            if (polymerStackId.isPresent()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putString(REI_ITEM_ID, polymerStackId.get());
                NbtComponent nbtCompoundResult = NbtComponent.of(nbtCompound);
                result.set(DataComponentTypes.CUSTOM_DATA, nbtCompoundResult);
            }
        }
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void applyIfPresent(ItemStack target, ItemStack source, ComponentType componentType) {
        if (source.get(componentType) != null) {
            target.set(componentType, source.get(componentType));
        }
    }

    public static boolean isPolyItem(ItemStack itemStack) {
        return getPolymerStackId(itemStack).isPresent() || getPolyMcStackId(itemStack).isPresent() || getEivStackId(itemStack).isPresent();
    }

    public static String getRealItemId(ItemStack itemStack) {
        if (itemStack == null) {
            return DEFAULT_ITEM_ID;
        }
        if (itemStack.isEmpty()) {
            return DEFAULT_ITEM_ID;
        }
        Optional<String> polymerStackId = getPolymerStackId(itemStack);
        Optional<String> polyMcStackId = getPolyMcStackId(itemStack);
        Optional<String> eivStackId = getEivStackId(itemStack);
        if (polymerStackId.isPresent()) return polymerStackId.get();
        if (polyMcStackId.isPresent()) return polyMcStackId.get();
        if (eivStackId.isPresent()) return eivStackId.get();
        if (itemStack.getItem() == Items.AIR) {
            return DEFAULT_ITEM_ID;
        }
        return Registries.ITEM.getId(itemStack.getItem()).toString();
    }

    public static Optional<String> getPolymerStackId(ItemStack itemStack) {
        NbtComponent customData = getCustomData(itemStack);
        if (customData == null) {
            return Optional.empty();
        }
        NbtCompound nbtCompound = customData.copyNbt();
        NbtElement element = nbtCompound.get(POLYMER_ID);
        if (!(element instanceof NbtCompound polymerStack)) {
            return Optional.empty();
        }
        if (polymerStack.contains("id")) {
            return polymerStack.getString("id");
        }
        return Optional.empty();
    }

    public static Optional<String> getPolyMcStackId(ItemStack itemStack) {
        NbtComponent customData = getCustomData(itemStack);
        if (customData == null) {
            return Optional.empty();
        }
        NbtCompound nbtCompound = customData.copyNbt();
        NbtElement element = nbtCompound.get(POLY_MC_ID);
        if (!(element instanceof NbtCompound polymerStack)) {
            return Optional.empty();
        }
        if (polymerStack.contains("id")) {
            return polymerStack.getString("id");
        }
        return Optional.empty();
    }

    public static Optional<String> getEivStackId(ItemStack itemStack) {
        NbtComponent customData = getCustomData(itemStack);
        if (customData == null) {
            return Optional.empty();
        }
        NbtCompound nbtCompound = customData.copyNbt();
        if (!nbtCompound.contains(REI_ITEM_ID)) {
            return Optional.empty();
        }
        return nbtCompound.getString(REI_ITEM_ID);
    }

    public static NbtComponent getCustomData(ItemStack itemStack) {
        return itemStack.get(DataComponentTypes.CUSTOM_DATA);
    }
}