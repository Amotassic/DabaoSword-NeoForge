package com.amotassic.dabaosword.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ShensuPayload(float f) implements CustomPacketPayload {
    public static final Type<ShensuPayload> ID = new Type<>(ResourceLocation.parse("dabaosword:shensu_speed"));
    public static final StreamCodec<ByteBuf, ShensuPayload> CODEC =
            StreamCodec.of(((b, v) -> {ByteBuf buf1 = b.writeFloat(v.f);}), buf -> new ShensuPayload(buf.readFloat()));

    @Override
    public Type<? extends CustomPacketPayload> type() {return ID;}
}
