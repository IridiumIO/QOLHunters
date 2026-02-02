package io.iridium.qolhunters.mixin.jewelapplicationtable;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.container.VaultJewelApplicationStationContainer;
import iskallia.vault.container.oversized.OverSizedSlotContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value = VaultJewelApplicationStationContainer.class, remap = false)
public abstract class MixinVaultJewelApplicationStationContainer extends OverSizedSlotContainer{

    protected MixinVaultJewelApplicationStationContainer(MenuType<?> menuType, int id, Player player) {
        super(menuType, id, player);
    }

    @ModifyArg(method = "initSlots", at = @At(value = "INVOKE", target = "Liskallia/vault/container/VaultJewelApplicationStationContainer$1;<init>(Liskallia/vault/container/VaultJewelApplicationStationContainer;Lnet/minecraft/world/Container;III)V"), index = 3, slice =
    @Slice(
        from = @At(value = "INVOKE", target = "Liskallia/vault/block/entity/VaultJewelApplicationStationTileEntity;getInventory()Liskallia/vault/container/oversized/OverSizedInventory;"),
        to = @At(value = "INVOKE", target = "Liskallia/vault/container/VaultJewelApplicationStationContainer$1;setBackground(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/world/inventory/Slot;")))
    private int modifyTabSlotPos(int x) {
        return QOLHuntersClientConfigs.BETTER_SCREEN_JEWEL_APPLICATION.get() ? 5: x;
    }
}
