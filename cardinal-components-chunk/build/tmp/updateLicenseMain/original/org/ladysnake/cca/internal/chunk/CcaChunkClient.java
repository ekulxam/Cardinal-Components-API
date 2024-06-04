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
package org.ladysnake.cca.internal.chunk;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.ladysnake.cca.api.v3.component.ComponentProvider;
import org.ladysnake.cca.internal.base.CcaClientInternals;

import java.util.Objects;

public class CcaChunkClient {
    public static void initClient() {
        if (FabricLoader.getInstance().isModLoaded("fabric-networking-api-v1")) {
            CcaClientInternals.registerComponentSync(
                CardinalComponentsChunk.PACKET_ID,
                (payload, ctx) -> payload.componentKey().flatMap(key -> key.maybeGet(Objects.requireNonNull(ctx.client().world).getChunk(
                    payload.targetData().x,
                    payload.targetData().z
                ))
            ));
        }
        if (FabricLoader.getInstance().isModLoaded("fabric-lifecycle-events-v1")) {
            ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> ((ComponentProvider) chunk).getComponentContainer().onServerLoad());
            ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> ((ComponentProvider) chunk).getComponentContainer().onServerUnload());
        }
    }
}
