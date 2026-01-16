package cc.thonly.polydex2rei.plugin;

import cc.thonly.polydex2rei.Polydex2REI;
import com.mojang.serialization.MapCodec;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class Polydex2REIDisplay implements Display {
    public static final Text DISABLED_TEXT = Text.translatable("item.disabled").formatted(Formatting.RED);
    public static final ItemStack DEFAULT_ITEM = ((Supplier<ItemStack>) () -> {
        ItemStack defaultStack = Items.BEDROCK.getDefaultStack();
        defaultStack.set(DataComponentTypes.ITEM_NAME, DISABLED_TEXT);
        return defaultStack;
    }).get();
    public static final EntryIngredient INGREDIENT = EntryIngredient.of(EntryStack.of(VanillaEntryTypes.ITEM, DEFAULT_ITEM));

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(INGREDIENT);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(INGREDIENT);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return Polydex2REIPlugin.CATEGORY;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(Identifier.of(UUID.randomUUID().toString()));
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }

}
