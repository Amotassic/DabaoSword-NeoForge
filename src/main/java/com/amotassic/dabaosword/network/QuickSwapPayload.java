package com.amotassic.dabaosword.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record QuickSwapPayload(int id) implements CustomPacketPayload {
    public static final Type<QuickSwapPayload> ID = new Type<>(ResourceLocation.parse("dabaosword:quick_swap"));
    public static final StreamCodec<ByteBuf, QuickSwapPayload> CODEC =
            StreamCodec.of(((b, v) -> b.writeInt(v.id())), buf -> new QuickSwapPayload(buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() {return ID;}
}
