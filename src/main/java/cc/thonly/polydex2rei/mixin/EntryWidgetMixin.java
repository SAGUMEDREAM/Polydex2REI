package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.Polydex2REI;
import cc.thonly.polydex2rei.network.Action;
import cc.thonly.polydex2rei.util.ItemUtils;
import lombok.extern.slf4j.Slf4j;
import me.shedaniel.math.impl.PointHelper;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackProviderWidget;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

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
    public void click(Click event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (containsMouse(event.x(), event.y())) {
            EntryStack<?> stack = this.getCurrentEntry();
            if (stack != null && !stack.isEmpty()) {
                EntryStack<?> copy = stack.copy();
                Object value = copy.getValue();
                if (value instanceof ItemStack itemStack) {
                    Polydex2REI.TARGET_ITEM_STACK = itemStack;
                    int button = event.button();
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
    private void onKeyPressed(KeyInput event, CallbackInfoReturnable<Boolean> cir) {
        EntryStack<?> stack = getCurrentEntry();
        if (stack == null || stack.isEmpty()) return;

        Object value = stack.getValue();
        if (!(value instanceof ItemStack itemStack)) return;

        Polydex2REI.TARGET_ITEM_STACK = itemStack;

        if (ConfigObject.getInstance().getRecipeKeybind().matchesKey(event.key(), event.scancode())) {
            Polydex2REI.ACTION = Action.LEFT;
            return;
        }

        if (ConfigObject.getInstance().getUsageKeybind().matchesKey(event.key(), event.scancode())) {
            Polydex2REI.ACTION = Action.RIGHT;
        }
    }
}
