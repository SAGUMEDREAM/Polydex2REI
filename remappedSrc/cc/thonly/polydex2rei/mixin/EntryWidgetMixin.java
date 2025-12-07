package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.network.Action;
import cc.thonly.polydex2rei.network.StackActionPayload;
import cc.thonly.polydex2rei.util.ClientPolymerItemUtils;
import lombok.extern.slf4j.Slf4j;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackProviderWidget;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Slf4j
@Mixin(value = { EntryWidget.class })
public abstract class EntryWidgetMixin
        extends Slot
        implements DraggableStackProviderWidget {
    @Shadow(remap = false)
    protected boolean wasClicked;

    @Shadow(remap = false)
    public abstract EntryStack<?> getCurrentEntry();

    @Inject(method = { "mouseClicked" }, at = { @At(value = "RETURN") }, cancellable = true)
    public void click(Click clickData, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (ConfigObject.getInstance().isCheating()) {
            return;
        }
        if (this.wasClicked) {
            EntryStack<?> currentEntry = this.getCurrentEntry();
            if (currentEntry == null) {
                return;
            }
            EntryStack<?> itemStackEntryStack = currentEntry.cheatsAs();
            if (itemStackEntryStack.isEmpty()) {
                return;
            }
            Object object = itemStackEntryStack.getValue();
            if (!(object instanceof ItemStack itemStack)) {
                return;
            }
            if (ClientPolymerItemUtils.isPolyItem(itemStack)) {
                String realItemId = ClientPolymerItemUtils.getRealItemId(itemStack);
                int button = clickData.button();
                Action action = Action.ENUM_MAP.getOrDefault(button, Action.LEFT);
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.getNetworkHandler() == null) {
                    log.warn("Cannot send payload: client not connected.");
                    return;
                }
                StackActionPayload payload = new StackActionPayload(action, realItemId);
                ClientPlayNetworking.send(payload);
                cir.setReturnValue(false);
            }
        }
    }
}
