package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.util.ItemUtils;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.impl.client.gui.screen.CompositeDisplayViewingScreen;
import me.shedaniel.rei.impl.display.DisplaySpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
@Pseudo
@Mixin(CompositeDisplayViewingScreen.class)
public class CompositeDisplayViewingScreenMixin {
    @ModifyVariable(
            method = "<init>",
            at = @At("HEAD"),
            argsOnly = true,
            remap = false
    )
    private static Map<DisplayCategory<?>, List<DisplaySpec>> init(Map<DisplayCategory<?>, List<DisplaySpec>> categoriesMap) {
        Map<DisplayCategory<?>, List<DisplaySpec>> newMap = ItemUtils.filter(categoriesMap);
        return newMap;
    }

}
