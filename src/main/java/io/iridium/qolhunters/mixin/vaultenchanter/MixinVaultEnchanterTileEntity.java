package io.iridium.qolhunters.mixin.vaultenchanter;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot;
import io.iridium.qolhunters.interfaces.IModifiedInventory;
import iskallia.vault.block.entity.VaultEnchanterTileEntity;
import iskallia.vault.block.entity.base.BookAnimatingTileEntity;
import iskallia.vault.container.oversized.OverSizedInventory;
import iskallia.vault.util.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
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



    // We don't want an IF check here since it's on the serverside only
    @Inject(method = "load", at = @At("HEAD"), cancellable = true)
    public void load(CompoundTag tag, CallbackInfo ci) {
        super.load(tag);
        this.overSizedInventory.load(tag);
        NBTHelper.deserializeSimpleContainer(((VaultEnchanterTileEntity)(Object)this).getInventory(), tag.getList("inventory", 10));
        ci.cancel();

    }


    @Inject(method="saveAdditional", at=@At("HEAD"), cancellable=true)
    protected void saveAdditional(CompoundTag tag, CallbackInfo ci) {

        super.saveAdditional(tag);
        this.overSizedInventory.save(tag);
        tag.put("inventory", NBTHelper.serializeSimpleContainer(((VaultEnchanterTileEntity)(Object)this).getInventory()));
        ci.cancel();

    }


    @Unique
    private final OverSizedInventory overSizedInventory = new OverSizedInventory(1, ((VaultEnchanterTileEntity)(Object)this));;
    @Override
    public OverSizedInventory getOverSizedInventory() {
        return this.overSizedInventory;
    }

}
