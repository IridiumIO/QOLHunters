package io.iridium.qolhunters.mixin.vaultenchanter;

import io.iridium.qolhunters.interfaces.IModifiedInventory;
import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.block.base.FacedBlock;
import iskallia.vault.block.entity.VaultEnchanterTileEntity;
import iskallia.vault.container.oversized.OverSizedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(iskallia.vault.block.VaultEnchanterBlock.class)
public abstract class MixinVaultEnchanterBlock extends FacedBlock implements EntityBlock {

    protected MixinVaultEnchanterBlock(Properties properties) {
        super(properties);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        QOLHunters.LOGGER.info("onRemove called");
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof VaultEnchanterTileEntity enchanter) {

            OverSizedInventory ct = ((IModifiedInventory)enchanter).getOverSizedInventory();
            QOLHunters.LOGGER.info("Dropping oversized contents: " + ct.getContents().stream().count());
            ct.getOverSizedContents().forEach(
                            overSizedStack -> overSizedStack.splitByStackSize()
                                    .forEach(splitStack -> {

                                        QOLHunters.LOGGER.info("Dropping item: " + splitStack + " at " + pos);
                                        Containers.dropItemStack(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), splitStack);
                                    })
                    );


            enchanter.getInventory().clearContent();
            ct.clearContent();
            level.updateNeighbourForOutputSignal(pos, this);
        }

        if (IModifiedInventory.class.isAssignableFrom(level.getBlockEntity(pos).getClass())) {
            QOLHunters.LOGGER.info("Ah fuck");

        }

        super.onRemove(state, level, pos, newState, isMoving);

    }

}
