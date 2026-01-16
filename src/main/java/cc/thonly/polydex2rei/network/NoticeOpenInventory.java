package cc.thonly.polydex2rei.network;

import cc.thonly.polydex2rei.Polydex2REI;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

@Slf4j
public record NoticeOpenInventory() implements CustomPayload {
    public static final Identifier ID = Polydex2REI.id("open_inventory");
    public static final Id<NoticeOpenInventory> PACKET_ID = new Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, NoticeOpenInventory> CODEC = PacketCodec.of(
            NoticeOpenInventory::write,
            NoticeOpenInventory::read);

    private static NoticeOpenInventory read(RegistryByteBuf buf) {
        return new NoticeOpenInventory();
    }

    private void write(RegistryByteBuf buf) {
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}