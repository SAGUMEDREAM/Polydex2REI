package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.Polydex2REI;
import cc.thonly.polydex2rei.network.Action;
import cc.thonly.polydex2rei.network.StackActionPayload;
import lombok.extern.slf4j.Slf4j;
import me.shedaniel.math.impl.PointHelper;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.client.gui.ScreenOverlayImpl;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.KeyInput;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnstableApiUsage")
@Slf4j
@Mixin(value = {ScreenOverlayImpl.class})
public class ScreenOverlayImplMixin {

    @Inject(method = "keyPressed",
            at = @At(value = "HEAD")
    )
    public void onKeyPressed(KeyInput event, CallbackInfoReturnable<Boolean> cir) {
        EntryStack<?> stack = ScreenRegistry.getInstance().getFocusedStack(MinecraftClient.getInstance().currentScreen, PointHelper.ofMouse());
        if (stack != null && !stack.isEmpty()) {
            EntryStack<?> copy = stack.copy();
            Object value = copy.getValue();
            if (value instanceof ItemStack itemStack) {
                Polydex2REI.TARGET_ITEM_STACK = itemStack;
                Polydex2REI.ACTION = Action.ENUM_MAP.getOrDefault(event.key(), Action.LEFT);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(KeyInput event, CallbackInfoReturnable<Boolean> cir) {
        EntryStack<?> stack = ScreenRegistry.getInstance().getFocusedStack(MinecraftClient.getInstance().currentScreen, PointHelper.ofMouse());
        if (stack != null && !stack.isEmpty()) {
            EntryStack<?> copy = stack.copy();
            Object value = copy.getValue();
            if (value instanceof ItemStack itemStack) {
                Polydex2REI.TARGET_ITEM_STACK = itemStack;
                if (ConfigObject.getInstance().getRecipeKeybind().matchesKey(event.key(), event.scancode())) {
                    Polydex2REI.ACTION = Action.LEFT;
                }
                if (ConfigObject.getInstance().getUsageKeybind().matchesKey(event.key(), event.scancode())) {
                    Polydex2REI.ACTION = Action.RIGHT;
                }
            }
        }
    }

}
