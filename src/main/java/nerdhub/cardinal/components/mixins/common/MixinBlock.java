package nerdhub.cardinal.components.mixins.common;

import nerdhub.cardinal.components.api.provider.BlockComponentProvider;
import nerdhub.cardinal.components.api.provider.SidedProviderCompound;
import nerdhub.cardinal.components.api.util.Components;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class MixinBlock implements BlockComponentProvider {

    @Shadow public abstract boolean hasBlockEntity();

    @Override
    public SidedProviderCompound getComponents(BlockView view, BlockPos pos) {
        return this.hasBlockEntity() ? BlockComponentProvider.super.getComponents(view, pos) : Components.emptySidedProvider();
    }

}
