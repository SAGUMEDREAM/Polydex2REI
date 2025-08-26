package cc.thonly.polydex2rei;


import cc.thonly.polydex2rei.network.Action;
import cc.thonly.polydex2rei.network.StackActionPayload;
import eu.pb4.polydex.api.v1.recipe.PolydexEntry;
import eu.pb4.polydex.api.v1.recipe.PolydexPageUtils;
import eu.pb4.polydex.impl.PolydexImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Polydex2REI
        implements ModInitializer {
    public static final String MOD_ID = "polydex2rei";
    public static final Logger LOGGER = LoggerFactory.getLogger("polydex2rei");

    public void onInitialize() {
        LOGGER.info("Loading Polydex2REI!");
        PayloadTypeRegistry.playS2C().register(StackActionPayload.PACKET_ID, StackActionPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(StackActionPayload.PACKET_ID, StackActionPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(StackActionPayload.PACKET_ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            Action action = payload.action();
            String stringId = payload.id();
            Identifier id = Identifier.of(stringId);
            Item item = Registries.ITEM.get(id);
            if (item == Items.AIR) {
                return;
            }
            ItemStack itemStack = item.getDefaultStack();
            PolydexEntry entry = PolydexImpl.getEntry(itemStack);
            if (entry == null) {
                entry = PolydexImpl.ITEM_ENTRIES.nonEmptyById().get(id);
            }
            if (entry != null) {
                if (action == Action.RIGHT) {
                    PolydexPageUtils.openUsagesListUi(player, entry, null);
                } else if (action == Action.LEFT) {
                    PolydexPageUtils.openRecipeListUi(player, entry, null);
                }
            }
        });
    }

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }
}