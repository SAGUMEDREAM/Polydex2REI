package cc.thonly.polydex2rei;

import cc.thonly.polydex2rei.network.Action;
import cc.thonly.polydex2rei.network.NoticeOpenInventory;
import cc.thonly.polydex2rei.network.StackActionPayload;
import cc.thonly.polydex2rei.util.ServerTimeEvent;
import eu.pb4.polydex.api.v1.recipe.PolydexEntry;
import eu.pb4.polydex.api.v1.recipe.PolydexPageUtils;
import eu.pb4.polydex.impl.PolydexImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
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
    public static ItemStack TARGET_ITEM_STACK = ItemStack.EMPTY;
    public static Action ACTION = Action.LEFT;

    public void onInitialize() {
        LOGGER.info("Loading Polydex2REI!");
        PayloadTypeRegistry.playS2C().register(StackActionPayload.PACKET_ID, StackActionPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(StackActionPayload.PACKET_ID, StackActionPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(NoticeOpenInventory.PACKET_ID, NoticeOpenInventory.CODEC);
        PayloadTypeRegistry.playC2S().register(NoticeOpenInventory.PACKET_ID, NoticeOpenInventory.CODEC);

        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> ServerTimeEvent.tick());
        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> {
            ServerTimeEvent.tick();
        });

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
                PolydexEntry finalEntry = entry;
                if (action == Action.RIGHT) {
                    ServerTimeEvent.add(new ServerTimeEvent(() -> {
                        PolydexPageUtils.openUsagesListUi(player, finalEntry, () -> {
                            if (ServerPlayNetworking.canSend(player, NoticeOpenInventory.ID)) {
                                player.closeHandledScreen();
                                ServerPlayNetworking.send(player, new NoticeOpenInventory());
                            }
                        });
                    }, 1));
                } else if (action == Action.LEFT) {
                    ServerTimeEvent.add(new ServerTimeEvent(() -> {
                        PolydexPageUtils.openRecipeListUi(player, finalEntry, () -> {
                            if (ServerPlayNetworking.canSend(player, NoticeOpenInventory.ID)) {
                                player.closeHandledScreen();
                                ServerPlayNetworking.send(player, new NoticeOpenInventory());
                            }
                        });
                    }, 1));
                }
            }
        });

    }

    public static void sendQueryPolydexPagePacket(Action action, String realItemId) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            return;
        }
        if (realItemId == null) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) {
            LOGGER.warn("Cannot send payload: client not connected.");
            return;
        }
        StackActionPayload payload = new StackActionPayload(action, realItemId);
        ClientPlayNetworking.send(payload);
    }

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }
}