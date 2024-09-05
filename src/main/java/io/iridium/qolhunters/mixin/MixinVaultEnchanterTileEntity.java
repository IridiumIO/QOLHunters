package io.iridium.qolhunters.mixin;

import io.iridium.qolhunters.IModifiedInventory;
import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.block.entity.VaultEnchanterTileEntity;
import iskallia.vault.block.entity.base.BookAnimatingTileEntity;
import iskallia.vault.container.oversized.OverSizedInventory;
import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(iskallia.vault.block.entity.VaultEnchanterTileEntity.class)
public abstract class MixinVaultEnchanterTileEntity extends BookAnimatingTileEntity implements IModifiedInventory {


    @Final
    @Shadow(remap = false)
    private SimpleContainer inventory;

    protected MixinVaultEnchanterTileEntity(BlockEntityType<?> tileEntityType, BlockPos blockPos, BlockState blockState) {
        super(tileEntityType, blockPos, blockState);
    }


    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {

    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.overSizedInventory.load(tag);
        NBTHelper.deserializeSimpleContainer(((VaultEnchanterTileEntity)(Object)this).getInventory(), tag.getList("inventory", 10));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.overSizedInventory.save(tag);
        tag.put("inventory", NBTHelper.serializeSimpleContainer(((VaultEnchanterTileEntity)(Object)this).getInventory()));
    }


    private final OverSizedInventory overSizedInventory = new OverSizedInventory(1, ((VaultEnchanterTileEntity)(Object)this));;
    @Override
    public OverSizedInventory getOverSizedInventory() {
        return this.overSizedInventory;
    }

}
