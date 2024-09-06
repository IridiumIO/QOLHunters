package io.iridium.qolhunters.mixin.vaultenchanter;

import io.iridium.qolhunters.interfaces.IModifiedInventory;
import iskallia.vault.block.entity.VaultEnchanterTileEntity;
import iskallia.vault.container.VaultEnchanterContainer;
import iskallia.vault.container.oversized.OverSizedInventory;
import iskallia.vault.util.InventoryUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(iskallia.vault.util.EnchantmentCost.class)
public abstract class MixinEnchantmentCost {

    @Shadow(remap = false)
    private int levels;

    @Shadow(remap = false)
    private List<ItemStack> items;


    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public boolean tryConsume(ServerPlayer player) {
        if (player.isCreative()) {
            return true;
        } else if (player.experienceLevel < this.levels) {
            return false;
        } else {

            VaultEnchanterContainer container = (VaultEnchanterContainer) player.containerMenu;
            VaultEnchanterTileEntity tileEntity = container.getTileEntity();
            OverSizedInventory overSizedInventory = ((IModifiedInventory) tileEntity).getOverSizedInventory();


            List<ItemStack> missing = InventoryUtil.getMissingInputs(this.items, player.getInventory(), overSizedInventory);
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
