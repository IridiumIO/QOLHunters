package io.iridium.qolhunters.mixin.vaultenchanter;

import com.google.common.collect.Sets;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot;
import io.iridium.qolhunters.interfaces.IModifiedInventory;
import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.block.entity.VaultEnchanterTileEntity;
import iskallia.vault.container.oversized.OverSizedContainerSynchronizer;
import iskallia.vault.container.oversized.OverSizedTabSlot;
import iskallia.vault.container.slot.TabSlot;
import iskallia.vault.container.spi.AbstractElementContainer;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModSlotIcons;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot.qol$canAddItemToSlot;
import static io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot.qol$moveOverSizedItemStackTo;

@Mixin(iskallia.vault.container.VaultEnchanterContainer.class)
public abstract class MixinVaultEnchanterContainer extends AbstractElementContainer {

    protected MixinVaultEnchanterContainer(MenuType<?> menuType, int id, Player player) {
        super(menuType, id, player);
    }

    @Final
    @Shadow(remap = false)
    private VaultEnchanterTileEntity tileEntity;


    @Inject(method="initSlots", at=@At("HEAD"), cancellable=true, remap = false)
    public void initSlots(Inventory playerInventory, CallbackInfo ci) {
        if (VaultEnchanterEmeraldSlot.isSlotEnabled(playerInventory.player)) {
            qol$initSlots(playerInventory);
            ci.cancel();
        }

    }

    @Inject(method="quickMoveStack", at=@At("HEAD"), cancellable=true)
    public void quickMoveStack(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (VaultEnchanterEmeraldSlot.isSlotEnabled(player)) {
            cir.setReturnValue(qol$quickMoveStack(player, index));
        }

    }



    @Unique
    private void qol$initSlots(Inventory playerInventory) {

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new TabSlot(playerInventory, column + row * 9 + 9, 8 + column * 18, 130 + row * 18));
            }
        }

        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            this.addSlot(new TabSlot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 188));
        }

        SimpleContainer ct = this.tileEntity.getInventory();
        this.addSlot(new Slot(ct, 0, 146, 78) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (stack.isEmpty()) {
                    return false;
                } else if (stack.getItem() == Items.EMERALD) {
                    return false;
                } else {
                    return stack.getItem() instanceof VaultGearItem ? VaultGearData.read(stack).getState() == VaultGearState.IDENTIFIED : true;
                }
            }
        });

        if (this.tileEntity == null) {
            QOLHunters.LOGGER.error("tileEntity is null in initSlots");
            return;
        }

        Container overSizedInventory = ((IModifiedInventory) (Object)this.tileEntity).getOverSizedInventory();

        if(overSizedInventory == null){
            QOLHunters.LOGGER.error("overSizedInventory is null in initSlots");
            return;
        }

        this.addSlot(
                new OverSizedTabSlot(overSizedInventory, 0, 146, 57)
                        .setFilter(stack -> stack.getItem() == Items.EMERALD).setBackground(InventoryMenu.BLOCK_ATLAS, ModSlotIcons.JEWEL_NO_ITEM)
        );

    }




    @Unique
    public ItemStack qol$quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index >= 0 && index < 36 && qol$moveOverSizedItemStackTo(slotStack, slot, 36, this.slots.size(), false, this.slots)) {
                return itemstack;
            }

            if (index >= 0 && index < 27) {
                if (!qol$moveOverSizedItemStackTo(slotStack, slot, 27, 36, false, this.slots)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 27 && index < 36) {
                if (!qol$moveOverSizedItemStackTo(slotStack, slot, 0, 27, false, this.slots)) {
                    return ItemStack.EMPTY;
                }
            } else if (!qol$moveOverSizedItemStackTo(slotStack, slot, 0, 36, false, this.slots)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemstack;
    }


    @Shadow
    public abstract boolean stillValid(Player pPlayer);

//    private int dragMode = -1;
//    private int dragEvent;
//    private final Set<Slot> dragSlots = Sets.newHashSet();
//

//    @Override
//    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
//        if (clickTypeIn == ClickType.QUICK_CRAFT) {
//            int j1 = this.dragEvent;
//            this.dragEvent = getQuickcraftHeader(dragType);
//            if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
//                this.resetQuickCraft();
//            } else if (this.getCarried().isEmpty()) {
//                this.resetQuickCraft();
//            } else if (this.dragEvent == 0) {
//                this.dragMode = getQuickcraftType(dragType);
//                if (isValidQuickcraftType(this.dragMode, player)) {
//                    this.dragEvent = 1;
//                    this.dragSlots.clear();
//                } else {
//                    this.resetQuickCraft();
//                }
//            } else if (this.dragEvent == 1) {
//                Slot slot = this.slots.get(slotId);
//                ItemStack mouseStack = this.getCarried();
//                if (slot != null
//                        && qol$canAddItemToSlot(slot, mouseStack, true)
//                        && slot.mayPlace(mouseStack)
//                        && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size())
//                        && this.canDragTo(slot)) {
//                    this.dragSlots.add(slot);
//                }
//            } else if (this.dragEvent == 2) {
//                if (!this.dragSlots.isEmpty()) {
//                    ItemStack mouseStackCopy = this.getCarried().copy();
//                    int k1 = this.getCarried().getCount();
//
//                    for (Slot dragSlot : this.dragSlots) {
//                        ItemStack mouseStack = this.getCarried();
//                        if (dragSlot != null
//                                && qol$canAddItemToSlot(dragSlot, mouseStack, true)
//                                && dragSlot.mayPlace(mouseStack)
//                                && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size())
//                                && this.canDragTo(dragSlot)) {
//                            ItemStack itemstack14 = mouseStackCopy.copy();
//                            int j3 = dragSlot.hasItem() ? dragSlot.getItem().getCount() : 0;
//                            getQuickCraftSlotCount(this.dragSlots, this.dragMode, itemstack14, j3);
//                            int k3 = dragSlot.getMaxStackSize(itemstack14);
//                            if (itemstack14.getCount() > k3) {
//                                itemstack14.setCount(k3);
//                            }
//
//                            k1 -= itemstack14.getCount() - j3;
//                            dragSlot.set(itemstack14);
//                        }
//                    }
//
//                    mouseStackCopy.setCount(k1);
//                    this.setCarried(mouseStackCopy);
//                }
//
//                this.resetQuickCraft();
//            } else {
//                this.resetQuickCraft();
//            }
//        } else if (this.dragEvent != 0) {
//            this.resetQuickCraft();
//        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
//            if (slotId == -999) {
//                if (!this.getCarried().isEmpty()) {
//                    if (dragType == 0) {
//                        player.drop(this.getCarried(), true);
//                        this.setCarried(ItemStack.EMPTY);
//                    }
//
//                    if (dragType == 1) {
//                        player.drop(this.getCarried().split(1), true);
//                    }
//                }
//            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
//                if (slotId < 0) {
//                    return;
//                }
//
//                Slot slot = this.slots.get(slotId);
//                if (slot == null || !slot.mayPickup(player)) {
//                    return;
//                }
//
//                ItemStack itemstack7 = this.quickMoveStack(player, slotId);
//
//                while (!itemstack7.isEmpty() && ItemStack.isSame(slot.getItem(), itemstack7)) {
//                    itemstack7 = this.quickMoveStack(player, slotId);
//                }
//            } else {
//                if (slotId < 0) {
//                    return;
//                }
//
//                Slot slot = this.slots.get(slotId);
//                if (slot != null) {
//                    ItemStack slotStack = slot.getItem();
//                    ItemStack mouseStack = this.getCarried();
//                    if (slotStack.isEmpty()) {
//                        if (!mouseStack.isEmpty() && slot.mayPlace(mouseStack)) {
//                            int i3 = dragType == 0 ? mouseStack.getCount() : 1;
//                            if (i3 > slot.getMaxStackSize(mouseStack)) {
//                                i3 = slot.getMaxStackSize(mouseStack);
//                            }
//
//                            slot.set(mouseStack.split(i3));
//                        }
//                    } else if (slot.mayPickup(player)) {
//                        if (mouseStack.isEmpty()) {
//                            if (slotStack.isEmpty()) {
//                                slot.set(ItemStack.EMPTY);
//                                this.setCarried(ItemStack.EMPTY);
//                            } else {
//                                int slotCount = Math.min(slotStack.getCount(), slotStack.getMaxStackSize());
//                                int toMove = dragType == 0 ? slotCount : (slotCount + 1) / 2;
//                                this.setCarried(slot.remove(toMove));
//                                if (slotStack.isEmpty()) {
//                                    slot.set(ItemStack.EMPTY);
//                                }
//
//                                slot.onTake(player, this.getCarried());
//                            }
//                        } else if (slot.mayPlace(mouseStack)) {
//                            if (slotStack.getItem() == mouseStack.getItem() && ItemStack.tagMatches(slotStack, mouseStack)) {
//                                int k2 = dragType == 0 ? mouseStack.getCount() : 1;
//                                if (k2 > slot.getMaxStackSize(mouseStack) - slotStack.getCount()) {
//                                    k2 = slot.getMaxStackSize(mouseStack) - slotStack.getCount();
//                                }
//
//                                mouseStack.shrink(k2);
//                                slotStack.grow(k2);
//                                slot.set(slotStack);
//                            } else if (mouseStack.getCount() <= slot.getMaxStackSize(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
//                                slot.set(mouseStack);
//                                this.setCarried(slotStack);
//                            }
//                        } else if (slotStack.getItem() == mouseStack.getItem()
//                                && mouseStack.getMaxStackSize() > 1
//                                && ItemStack.tagMatches(slotStack, mouseStack)
//                                && !slotStack.isEmpty()) {
//                            int j2 = slotStack.getCount();
//                            if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
//                                mouseStack.grow(j2);
//                                slotStack = slot.remove(j2);
//                                if (slotStack.isEmpty()) {
//                                    slot.set(ItemStack.EMPTY);
//                                }
//
//                                slot.onTake(player, this.getCarried());
//                            }
//                        }
//                    }
//
//                    slot.setChanged();
//                }
//            }
//        } else if (clickTypeIn != ClickType.SWAP || dragType < 0 || dragType >= 9) {
//            if (clickTypeIn == ClickType.CLONE && player.getAbilities().instabuild && this.getCarried().isEmpty() && slotId >= 0) {
//                Slot slot3 = this.slots.get(slotId);
//                if (slot3 != null && slot3.hasItem()) {
//                    ItemStack itemstack5 = slot3.getItem().copy();
//                    itemstack5.setCount(itemstack5.getMaxStackSize());
//                    this.setCarried(itemstack5);
//                }
//            } else if (clickTypeIn == ClickType.THROW && this.getCarried().isEmpty() && slotId >= 0) {
//                Slot slot = this.slots.get(slotId);
//                if (slot != null && slot.hasItem() && slot.mayPickup(player)) {
//                    int removeCount = Math.min(dragType == 0 ? 1 : slot.getItem().getCount(), slot.getItem().getMaxStackSize());
//                    ItemStack itemstack4 = slot.remove(removeCount);
//                    slot.onTake(player, itemstack4);
//                    player.drop(itemstack4, true);
//                }
//            } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
//                Slot slot = this.slots.get(slotId);
//                ItemStack mouseStack = this.getCarried();
//                if (!mouseStack.isEmpty() && (slot == null || !slot.hasItem() || !slot.mayPickup(player))) {
//                    int i = dragType == 0 ? 0 : this.slots.size() - 1;
//                    int j = dragType == 0 ? 1 : -1;
//
//                    for (int k = 0; k < 2; k++) {
//                        for (int l = i; l >= 0 && l < this.slots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += j) {
//                            Slot slot1 = this.slots.get(l);
//                            if (slot1.hasItem()
//                                    && qol$canAddItemToSlot(slot1, mouseStack, true)
//                                    && slot1.mayPickup(player)
//                                    && this.canTakeItemForPickAll(mouseStack, slot1)) {
//                                ItemStack itemstack2 = slot1.getItem();
//                                if (k != 0 || itemstack2.getCount() < slot1.getMaxStackSize(itemstack2)) {
//                                    int i1 = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack2.getCount());
//                                    ItemStack itemstack3 = slot1.remove(i1);
//                                    mouseStack.grow(i1);
//                                    if (itemstack3.isEmpty()) {
//                                        slot1.set(ItemStack.EMPTY);
//                                    }
//
//                                    slot1.onTake(player, itemstack3);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                this.broadcastChanges();
//            }
//        }
//    }

    @Override
    protected boolean moveItemStackTo(@NotNull ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        return qol$moveOverSizedItemStackTo(stack, null, startIndex, endIndex, reverseDirection, this.slots);
    }




//    @Override
//    protected void resetQuickCraft() {
//        this.dragEvent = 0;
//        this.dragSlots.clear();
//    }

    @Override
    public void setSynchronizer(ContainerSynchronizer sync) {
        if (this.getPlayer() instanceof ServerPlayer sPlayer) {
            super.setSynchronizer(new OverSizedContainerSynchronizer(sync, sPlayer));
        } else {
            super.setSynchronizer(sync);
        }
    }

}
