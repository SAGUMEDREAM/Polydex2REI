package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.Polydex2REI;
import cc.thonly.polydex2rei.plugin.Polydex2REICategory;
import cc.thonly.polydex2rei.util.ClientPolymerItemUtils;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.impl.client.gui.screen.AbstractDisplayViewingScreen;
import me.shedaniel.rei.impl.display.DisplaySpec;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
@Pseudo
@Mixin(AbstractDisplayViewingScreen.class)
public class AbstractDisplayViewingScreenMixin {
    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void onInitPolydexCategory(Map<DisplayCategory<?>, List<DisplaySpec>> categoryMap, @Nullable CategoryIdentifier<?> category, CallbackInfo ci) {
        if (category == Polydex2REICategory.INSTANCE.getCategoryIdentifier()) {
            ItemStack targetItemStack = Polydex2REI.TARGET_ITEM_STACK;
            String realItemId = ClientPolymerItemUtils.getRealItemId(targetItemStack);
//            System.out.println(realItemId);
            Polydex2REI.sendQueryPolydexPagePacket(Polydex2REI.ACTION, realItemId);
            return;
        }
        for (Map.Entry<DisplayCategory<?>, List<DisplaySpec>> entry : categoryMap.entrySet()) {
            DisplayCategory<?> displayCategory = entry.getKey();
            CategoryIdentifier<?> categoryIdentifier = displayCategory.getCategoryIdentifier();
            if (Objects.equals(categoryIdentifier, Polydex2REICategory.INSTANCE.getCategoryIdentifier())) {
                ItemStack targetItemStack = Polydex2REI.TARGET_ITEM_STACK;
                String realItemId = ClientPolymerItemUtils.getRealItemId(targetItemStack);
//                System.out.println(realItemId);
                Polydex2REI.sendQueryPolydexPagePacket(Polydex2REI.ACTION, realItemId);
            }
            break;
        }
    }

    @Inject(method = "selectCategory(Lme/shedaniel/rei/api/common/category/CategoryIdentifier;Z)V",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    public void onSelectPolydexCategory(CategoryIdentifier<?> category, boolean init, CallbackInfo ci) {
        if (Objects.equals(category, Polydex2REICategory.INSTANCE.getCategoryIdentifier())) {
            ItemStack targetItemStack = Polydex2REI.TARGET_ITEM_STACK;
            String realItemId = ClientPolymerItemUtils.getRealItemId(targetItemStack);
//            System.out.println(realItemId);
            Polydex2REI.sendQueryPolydexPagePacket(Polydex2REI.ACTION, realItemId);
        }
    }
}
