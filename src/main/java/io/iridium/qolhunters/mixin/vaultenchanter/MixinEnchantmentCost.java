package io.iridium.qolhunters.mixin.vaultenchanter;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot;
import io.iridium.qolhunters.interfaces.IModifiedInventory;
import iskallia.vault.block.entity.VaultEnchanterTileEntity;
import iskallia.vault.container.VaultEnchanterContainer;
import iskallia.vault.container.oversized.OverSizedInventory;
import iskallia.vault.util.InventoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(iskallia.vault.util.EnchantmentCost.class)
public abstract class MixinEnchantmentCost {

    @Shadow(remap = false)
    private int levels;

    @Shadow(remap = false)
    private List<ItemStack> items;


    @Inject(method="tryConsume", at=@At("HEAD"), cancellable=true, remap = false)
    public void tryConsume(ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        QOLHunters.LOGGER.info("Entering tryConsume method");
        try {
            if ( VaultEnchanterEmeraldSlot.isSlotEnabled(player)) {
                QOLHunters.LOGGER.info("TRYING TO CONSUME");
                boolean retVal = qol$tryConsume(player);
                cir.setReturnValue(retVal);
            }
        } catch (Exception e) {
            QOLHunters.LOGGER.error("Error in tryConsume method: " + e);
        }


    }






    @Unique
    private boolean qol$tryConsume(ServerPlayer player) {
        if (player.isCreative()) {
            return true;
        } else if (player.experienceLevel < this.levels) {
            return false;
        } else {

            QOLHunters.LOGGER.error("Made it past zero checks");
            VaultEnchanterContainer container = (VaultEnchanterContainer) player.containerMenu;
            QOLHunters.LOGGER.error("Made it past first checks");

            VaultEnchanterTileEntity tileEntity = container.getTileEntity();
            QOLHunters.LOGGER.error("Made it past second checks");

            OverSizedInventory overSizedInventory = ((IModifiedInventory) tileEntity).getOverSizedInventory();

            QOLHunters.LOGGER.error("Made it past third checks");

            List<ItemStack> missing = InventoryUtil.getMissingInputs(this.items, player.getInventory(), overSizedInventory);
            QOLHunters.LOGGER.error("Made it past fourth checks");
            if (!missing.isEmpty()) {
                return false;
            } else if (!InventoryUtil.consumeInputs(this.items, player.getInventory(), overSizedInventory, true)) {
                return false;
            } else {
                InventoryUtil.consumeInputs(this.items, player.getInventory(), overSizedInventory, false);
                player.setExperienceLevels(player.experienceLevel - this.levels);
                return true;
            }
        }
    }

}
