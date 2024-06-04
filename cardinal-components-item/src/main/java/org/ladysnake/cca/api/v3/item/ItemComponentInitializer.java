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
package org.ladysnake.cca.api.v3.item;

import net.minecraft.item.ItemStack;
import org.ladysnake.cca.internal.base.ComponentRegistrationInitializer;

/**
 * Entrypoint getting invoked to register item component migrations.
 *
 * <p>The entrypoint is exposed as either {@code "cardinal-components"} or {@code "cardinal-components-item"} in the mod json and runs for any environment.
 * It usually executes right before the first {@link ItemStack} instance is created.
 *
 * @since 6.0.0
 */
public interface ItemComponentInitializer extends ComponentRegistrationInitializer {
    /**
     * Called to register component migrations from CCA to {@link net.minecraft.component.ComponentType}.
     *
     * @param registry an {@link ItemComponentMigrationRegistry} for component migrations
     * @since 7.0.0
     */
    void registerItemComponentMigrations(ItemComponentMigrationRegistry registry);
}
