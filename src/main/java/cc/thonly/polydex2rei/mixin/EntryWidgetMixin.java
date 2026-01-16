package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.Polydex2REI;
import cc.thonly.polydex2rei.network.Action;
import lombok.extern.slf4j.Slf4j;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackProviderWidget;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Slf4j
@Pseudo
@Mixin(value = {EntryWidget.class})
public abstract class EntryWidgetMixin
        extends Slot
        implements DraggableStackProviderWidget {
    @Shadow(remap = false)
    protected boolean wasClicked;

    @Shadow(remap = false)
    public abstract EntryStack<?> getCurrentEntry();

    @Inject(method = {"mouseClicked"}, at = {@At(value = "HEAD")})
    public void click(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (containsMouse(mouseX, mouseY)) {
            EntryStack<?> stack = this.getCurrentEntry();
            if (stack != null && !stack.isEmpty()) {
                EntryStack<?> copy = stack.copy();
                Object value = copy.getValue();
                if (value instanceof ItemStack itemStack) {
                    Polydex2REI.TARGET_ITEM_STACK = itemStack;
                    Polydex2REI.ACTION = Action.ENUM_MAP.getOrDefault(button, Action.LEFT);
//                    ItemUtils.printfItemStack(itemStack);
                }
            }
        }
    }

    @Inject(
            method = "keyPressed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        EntryStack<?> stack = getCurrentEntry();
        if (stack == null || stack.isEmpty()) return;

        Object value = stack.getValue();
        if (!(value instanceof ItemStack itemStack)) return;

        Polydex2REI.TARGET_ITEM_STACK = itemStack;

        if (ConfigObject.getInstance().getRecipeKeybind().matchesKey(keyCode, scanCode)) {
            Polydex2REI.ACTION = Action.LEFT;
            return;
        }

        if (ConfigObject.getInstance().getUsageKeybind().matchesKey(keyCode, scanCode)) {
            Polydex2REI.ACTION = Action.RIGHT;
        }
    }
}
