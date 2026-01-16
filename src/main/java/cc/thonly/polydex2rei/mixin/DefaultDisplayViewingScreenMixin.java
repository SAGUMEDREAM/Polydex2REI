package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.util.ItemUtils;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.impl.client.gui.screen.DefaultDisplayViewingScreen;
import me.shedaniel.rei.impl.display.DisplaySpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
@Pseudo
@Mixin(DefaultDisplayViewingScreen.class)
public abstract class DefaultDisplayViewingScreenMixin {

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

//    @Inject(method = "getCurrentDisplayed", at = @At("RETURN"), cancellable = true)
//    public void getCurrentDisplayed(CallbackInfoReturnable<List<DisplaySpec>> cir) {
//        System.out.println(cir.getReturnValue());
//    }
}
