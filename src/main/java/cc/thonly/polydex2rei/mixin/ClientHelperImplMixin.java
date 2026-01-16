package cc.thonly.polydex2rei.mixin;

import cc.thonly.polydex2rei.util.ItemUtils;
import me.shedaniel.rei.impl.client.ClientHelperImpl;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ClientHelperImpl.class)
public class ClientHelperImplMixin {
    @ModifyVariable(method = "tryCheatingEntry",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"),
            name = "cheatedStack"
    )
    public ItemStack modifyStack(ItemStack value) {
        return ItemUtils.getServerStack(value);
    }
}
