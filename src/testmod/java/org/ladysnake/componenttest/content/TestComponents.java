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
package org.ladysnake.componenttest.content;

import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.item.ItemComponentInitializer;
import org.ladysnake.cca.api.v3.item.ItemComponentMigrationRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import org.ladysnake.cca.test.base.BaseVita;
import org.ladysnake.cca.test.base.SyncedVita;
import org.ladysnake.cca.test.base.Vita;
import org.ladysnake.cca.test.entity.CcaEntityTestMod;
import org.ladysnake.componenttest.content.vita.ItemVita;
import org.ladysnake.componenttest.content.vita.TeamVita;

public final class TestComponents implements
    EntityComponentInitializer,
    BlockComponentInitializer,
    ItemComponentInitializer,
    ScoreboardComponentInitializer {

    public static final Identifier CUSTOM_PROVIDER_1 = Identifier.of("componenttest:custom/1");
    public static final Identifier CUSTOM_PROVIDER_2 = Identifier.of("componenttest:custom/2");

    public static final ComponentKey<Vita> ALT_VITA = ComponentRegistryV3.INSTANCE.getOrCreate(TestStaticComponentInitializer.ALT_VITA_ID, Vita.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(HostileEntity.class, ALT_VITA).after(Vita.KEY).end(e -> new BaseVita());
        registry.beginRegistration(PlayerEntity.class, ALT_VITA).after(Vita.KEY).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(e -> new BaseVita());
        registry.registerFor(VitalityZombieEntity.class, Vita.KEY, VitalityZombieEntity::createVitaComponent);
        registry.beginRegistration(LivingEntity.class, ALT_VITA).filter(CowEntity.class::isAssignableFrom).end(CcaEntityTestMod::createForEntity);
    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.beginRegistration(EndPortalBlockEntity.class, ALT_VITA).after(Vita.KEY).impl(SyncedVita.class).end(SyncedVita::new);
        registry.registerFor(EndPortalBlockEntity.class, Vita.KEY, SyncedVita::new);
    }

    @Override
    public void registerItemComponentMigrations(ItemComponentMigrationRegistry registry) {
        registry.registerMigration(ALT_VITA.getId(), ItemVita.Data.ALT_COMPONENT_TYPE);
        registry.registerMigration(Vita.KEY.getId(), ItemVita.Data.COMPONENT_TYPE);
    }

    @Override
    public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {
        registry.registerTeamComponent(Vita.KEY, (t, sc, s) -> new TeamVita(t));
    }
}
