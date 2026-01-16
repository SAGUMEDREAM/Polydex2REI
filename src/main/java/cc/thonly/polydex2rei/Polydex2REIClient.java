package cc.thonly.polydex2rei;

import cc.thonly.polydex2rei.network.NoticeOpenInventory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;

public class Polydex2REIClient
        implements ClientModInitializer {
    @SuppressWarnings("resource")
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->{
            ClientPlayNetworking.registerReceiver(NoticeOpenInventory.PACKET_ID, (payload, context) -> {
                context.client().execute(() -> {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    if (mc == null) {
                        return;
                    }

                    ClientPlayerEntity player = mc.player;
                    if (player == null) {
                        return;
                    }
                    mc.player.closeHandledScreen();
                    mc.setScreen(
                            new InventoryScreen(player)
                    );
                });
            });
        });
    }
}
