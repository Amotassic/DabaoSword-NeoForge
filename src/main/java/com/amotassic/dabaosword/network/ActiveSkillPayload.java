package com.amotassic.dabaosword.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ActiveSkillPayload(int id) implements CustomPacketPayload {
    public static final Type<ActiveSkillPayload> ID = new Type<>(ResourceLocation.parse("dabaosword:active_skill_target"));
    public static final StreamCodec<ByteBuf, ActiveSkillPayload> CODEC =
            StreamCodec.of(((b, v) -> b.writeInt(v.id())), buf -> new ActiveSkillPayload(buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() {return ID;}
}
