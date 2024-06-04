/*
 * Cardinal-Components-API
 * Copyright (C) 2019-2024 Ladysnake
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ladysnake.cca.internal.base;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

import java.util.Optional;

public record ComponentUpdatePayload<T>(
    Id<ComponentUpdatePayload<T>> id,
    T targetData,
    boolean required,
    Identifier componentKeyId,
    RegistryByteBuf buf
) implements CustomPayload {
    public static <T> CustomPayload.Id<ComponentUpdatePayload<T>> id(String path) {
        return new CustomPayload.Id<>(Identifier.of("cardinal-components:" + path));
    }

    public static <T> void register(Id<ComponentUpdatePayload<T>> id, PacketCodec<? super RegistryByteBuf, T> targetDataCodec) {
        PayloadTypeRegistry.playS2C().register(id, codec(id, targetDataCodec));
    }

    public static <T> PacketCodec<RegistryByteBuf, ComponentUpdatePayload<T>> codec(Id<ComponentUpdatePayload<T>> id, PacketCodec<? super RegistryByteBuf, T> targetDataCodec) {
        return PacketCodec.tuple(
            PacketCodec.unit(id), ComponentUpdatePayload::id,
            targetDataCodec, ComponentUpdatePayload::targetData,
            PacketCodecs.BOOL, ComponentUpdatePayload::required,
            Identifier.PACKET_CODEC, ComponentUpdatePayload::componentKeyId,
            MorePacketCodecs.REG_BYTE_BUF, ComponentUpdatePayload::buf,
            ComponentUpdatePayload::new
        );
    }

    public Optional<ComponentKey<?>> componentKey() {
        ComponentKey<?> key = ComponentRegistry.get(this.componentKeyId());
        if (key == null && this.required()) {
            throw new UnknownComponentException("Unknown component " + this.componentKeyId());
        }
        return Optional.ofNullable(key);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return id;
    }
}
