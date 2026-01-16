package cc.thonly.polydex2rei.mixin;

import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.core.impl.client.compat.ReiCompatibility;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.packettweaker.PacketContext;

@Mixin(ReiCompatibility.class)
@Pseudo
public class ReiCompatibilityMixin {
    @Inject(method = "registerEntries", at = @At("HEAD"), cancellable = true, remap = false)
    public void registerEntries(EntryRegistry registry, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getServer() != null) {
            ci.cancel();
        }
    }

    @Inject(method = "lambda$update$2", at = @At("HEAD"), cancellable = true)
    private static void update(EntryRegistry registry, ItemStack stack, CallbackInfo ci) {
        ItemStack clientItemStack = PolymerItemUtils.getClientItemStack(stack, PacketContext.get());
        Text text = clientItemStack.get(DataComponentTypes.ITEM_NAME);
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getServer() != null || text == null) {
            registry.addEntry(EntryStack.of(VanillaEntryTypes.ITEM, clientItemStack));
            return;
        }
        TextContent content = text.getContent();
        if (content instanceof TranslatableTextContent translatableTextContent) {
            String key = translatableTextContent.getKey();
            clientItemStack.set(DataComponentTypes.ITEM_NAME, Text.translatable(key));
        }
        registry.addEntry(EntryStack.of(VanillaEntryTypes.ITEM, clientItemStack));
        ci.cancel();
    }

    @Inject(method = "update", at = @At("HEAD"), cancellable = true, remap = false)
    private static void update(EntryRegistry registry, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getServer() != null) {
            ci.cancel();
        }
    }
}
