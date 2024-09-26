package io.iridium.qolhunters.mixin.vaultenchanter;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot;
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
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(iskallia.vault.block.VaultEnchanterBlock.class)
public abstract class MixinVaultEnchanterBlock extends FacedBlock implements EntityBlock {

    protected MixinVaultEnchanterBlock(Properties properties) {
        super(properties);
    }

    @Inject(method="onRemove", at=@At("HEAD"), cancellable=true)
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
        if (true) {
            try {
                qol$onRemove(state, level, pos, newState, isMoving);
                ci.cancel();
            } catch (Exception e) {
                QOLHunters.LOGGER.error("Error in onRemove", e);
            }

        }

    }





    @Unique
    public void qol$onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof VaultEnchanterTileEntity enchanter) {

            OverSizedInventory ct = ((IModifiedInventory)enchanter).getOverSizedInventory();
            ct.getOverSizedContents().forEach(
                            overSizedStack -> overSizedStack.splitByStackSize()
                                    .forEach(splitStack -> {
                                        Containers.dropItemStack(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), splitStack);
                                    })
                    );
            Containers.dropContents(level, pos, enchanter.getInventory());
            enchanter.getInventory().clearContent();
            ct.clearContent();
            level.updateNeighbourForOutputSignal(pos, this);
        }


        super.onRemove(state, level, pos, newState, isMoving);

    }

}
