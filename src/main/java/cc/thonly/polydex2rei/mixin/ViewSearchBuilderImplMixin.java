package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.util.ItemUtils;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.impl.client.ClientHelperImpl;
import me.shedaniel.rei.impl.display.DisplaySpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ClientHelperImpl.ViewSearchBuilderImpl.class)
@Pseudo
public class ViewSearchBuilderImplMixin {
    @Inject(method = "buildMapInternal", at = @At("RETURN"), cancellable = true)
    public void buildMapInternal(CallbackInfoReturnable<Map<DisplayCategory<?>, List<DisplaySpec>>> cir) {
        Map<DisplayCategory<?>, List<DisplaySpec>> returnValue = cir.getReturnValue();
        cir.setReturnValue(ItemUtils.filter(returnValue));
    }
}
