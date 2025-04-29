package io.iridium.qolhunters.mixin.jewelapplicationtable;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.block.entity.VaultJewelApplicationStationTileEntity;
import iskallia.vault.container.VaultJewelApplicationStationContainer;
import iskallia.vault.container.oversized.OverSizedSlotContainer;
import iskallia.vault.container.slot.TabSlot;
import iskallia.vault.init.ModSlotIcons;
import iskallia.vault.item.tool.JewelItem;
import iskallia.vault.item.tool.ToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VaultJewelApplicationStationContainer.class)
public class MixinVaultJewelApplicationStationContainer extends OverSizedSlotContainer{

    protected MixinVaultJewelApplicationStationContainer(MenuType<?> menuType, int id, Player player) {
        super(menuType, id, player);
    }

    @Shadow(remap = false) @Final private VaultJewelApplicationStationTileEntity tileEntity;

    @Shadow(remap = false) @Final private BlockPos tilePos;


    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private void initSlots(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new TabSlot(playerInventory, column + row * 9 + 9, 58 + column * 18, 108 + row * 18));
            }
        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            this.addSlot(new TabSlot(playerInventory, hotbarSlot, 58 + hotbarSlot * 18, 166));
        }

        Container invContainer = this.tileEntity.getInventory();
        this.addSlot((new TabSlot(invContainer, 0, QOLHuntersClientConfigs.BETTER_SCREEN_JEWEL_APPLICATION.get() ? 5: 120, 73) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof ToolItem;
            }
        }).setBackground(InventoryMenu.BLOCK_ATLAS, ModSlotIcons.TOOL_NO_ITEM));

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new TabSlot(invContainer, i * 3 + j + 1, -999 + j * 18, 50 + i * 18) {
                    @Override public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof JewelItem;
                    }

                    //Increases Rendering FPS by ~60%
                    @Override public boolean isActive() { return false;}
                });
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.tileEntity == null ? false : this.tileEntity.stillValid(this.player);
    }
}
