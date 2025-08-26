package cc.thonly.polydex2rei.mixin;


import cc.thonly.polydex2rei.network.Action;
import cc.thonly.polydex2rei.network.StackActionPayload;
import cc.thonly.polydex2rei.util.ClientPolymerItemUtils;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import me.shedaniel.math.impl.PointHelper;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.client.gui.ScreenOverlayImpl;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Slf4j
@Mixin(value = {ScreenOverlayImpl.class})
public class ScreenOverlayImplMixin {

    @Inject(method = {"keyPressed"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        EntryStack<?> stack = ScreenRegistry.getInstance().getFocusedStack(MinecraftClient.getInstance().currentScreen, PointHelper.ofMouse());
        if (stack == null) {
            return;
        }
        if (stack.isEmpty()) {
            return;
        }
        Object object = stack.getValue();
        if (!(object instanceof ItemStack itemStack)) {
            return;
        }
        if (!ClientPolymerItemUtils.isPolyItem(itemStack)) {
            return;
        }
        String realItemId = ClientPolymerItemUtils.getRealItemId(itemStack);
        if (ConfigObject.getInstance().getRecipeKeybind().matchesKey(keyCode, scanCode)) {
            this.sendPacket(Action.LEFT, realItemId);
            cir.setReturnValue(false);
            cir.cancel();
        } else if (ConfigObject.getInstance().getUsageKeybind().matchesKey(keyCode, scanCode)) {
            this.sendPacket(Action.RIGHT, realItemId);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Unique
    private void sendPacket(Action action, String realItemId) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) {
            log.warn("Cannot send payload: client not connected.");
            return;
        }
        StackActionPayload payload = new StackActionPayload(action, realItemId);
        ClientPlayNetworking.send(payload);
    }
}
