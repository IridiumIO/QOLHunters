package io.iridium.qolhunters.mixin;

import io.iridium.qolhunters.IModifiedInventory;
import iskallia.vault.block.entity.VaultEnchanterTileEntity;
import iskallia.vault.block.entity.base.BookAnimatingTileEntity;
import iskallia.vault.container.oversized.OverSizedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(iskallia.vault.block.entity.VaultEnchanterTileEntity.class)
public abstract class MixinVaultEnchanterTileEntity extends BookAnimatingTileEntity implements IModifiedInventory {


    protected MixinVaultEnchanterTileEntity(BlockEntityType<?> tileEntityType, BlockPos blockPos, BlockState blockState) {
        super(tileEntityType, blockPos, blockState);
    }


    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {

    }



    @Inject(at = @At("TAIL"), method = "load", remap = false)
    public void load(CompoundTag tag, CallbackInfo ci){
        this.overSizedInventory.load(tag);
    }

    @Inject(at = @At("TAIL"), method = "saveAdditional", remap = false)
    public void saveAdditional(CompoundTag tag, CallbackInfo ci){
        this.overSizedInventory.save(tag);
    }



    private final OverSizedInventory overSizedInventory = new OverSizedInventory(1, ((VaultEnchanterTileEntity)(Object)this));;
    @Override
    public OverSizedInventory getOverSizedInventory() {
        return this.overSizedInventory;
    }

}
