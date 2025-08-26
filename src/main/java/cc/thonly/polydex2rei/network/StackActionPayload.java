package cc.thonly.polydex2rei.network;

import cc.thonly.polydex2rei.Polydex2REI;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

@Slf4j
public record StackActionPayload(Action action, String id) implements CustomPayload {
    public static final Identifier ID = Polydex2REI.id("stack_action");
    public static final Id<StackActionPayload> PACKET_ID = new Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, StackActionPayload> CODEC = PacketCodec.of(
            StackActionPayload::write,
            StackActionPayload::read
    );

    private static StackActionPayload read(RegistryByteBuf buf) {
        try {
            return new StackActionPayload(Action.get(buf.readInt()), buf.readString());
        } catch (Exception e) {
            log.error("Can't read Stack Action Payload: ", e);
            return new StackActionPayload(Action.LEFT, "minecraft:stone");
        }
    }

    private void write(RegistryByteBuf buf) {
        buf.writeInt(this.action.getId());
        buf.writeString(this.id);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}