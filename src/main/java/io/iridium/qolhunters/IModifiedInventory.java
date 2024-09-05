package io.iridium.qolhunters;

import iskallia.vault.block.entity.VaultEnchanterTileEntity;
import iskallia.vault.container.oversized.OverSizedInventory;

public interface IModifiedInventory {
     OverSizedInventory overSizedInventory = null;;
     default OverSizedInventory getOverSizedInventory() {
            return this.overSizedInventory;
     }
}
