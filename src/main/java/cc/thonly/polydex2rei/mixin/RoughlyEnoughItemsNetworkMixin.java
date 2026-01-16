package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.util.ItemUtils;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.shedaniel.rei.RoughlyEnoughItemsNetwork;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.impl.common.transfer.InputSlotCrafter;
import me.shedaniel.rei.impl.common.transfer.NewInputSlotCrafter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(RoughlyEnoughItemsNetwork.class)
@SuppressWarnings("removal")
public abstract class RoughlyEnoughItemsNetworkMixin {

    // CREATE_ITEMS_PACKET
    @Inject(method = "lambda$onInitialize$1", at = @At("HEAD"), cancellable = true)
    private static void onInitializeGetItem(RegistryByteBuf buf, NetworkManager.PacketContext context, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        if (player.getPermissionLevel() < player.getWorld().getServer().getOpPermissionLevel()) {
            player.sendMessage(Text.translatable("text.rei.no_permission_cheat").formatted(Formatting.RED), false);
            ci.cancel();
            return;
        }
        ItemStack stack = buf.decodeAsJson(ItemStack.OPTIONAL_CODEC);
        stack = ItemUtils.getServerStack(stack);
        if (player.getInventory().insertStack(stack.copy())) {
            RegistryByteBuf newBuf = new RegistryByteBuf(Unpooled.buffer(), player.getRegistryManager());
            newBuf.encodeAsJson(ItemStack.OPTIONAL_CODEC, stack.copy());
            newBuf.writeString(player.getNameForScoreboard(), 32767);
            NetworkManager.sendToPlayer(player, RoughlyEnoughItemsNetwork.CREATE_ITEMS_MESSAGE_PACKET, newBuf);
        } else {
            player.sendMessage(Text.translatable("text.rei.failed_cheat_items"), false);
        }
        ci.cancel();

    }
}
