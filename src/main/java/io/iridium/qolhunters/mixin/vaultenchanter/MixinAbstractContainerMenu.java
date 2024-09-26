package io.iridium.qolhunters.mixin.vaultenchanter;

import com.google.common.collect.Sets;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot;
import iskallia.vault.container.VaultEnchanterContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

import static io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot.qol$canAddItemToSlot;
import static net.minecraft.world.inventory.AbstractContainerMenu.*;

@Mixin(AbstractContainerMenu.class)
public class MixinAbstractContainerMenu {


    @Inject(method="clicked", at=@At("HEAD"), cancellable=true)
    public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer, CallbackInfo ci) {

        boolean clientCheck = FMLEnvironment.dist.isClient() && QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get();

        if (VaultEnchanterEmeraldSlot.isSlotEnabled(pPlayer) && (((AbstractContainerMenu) (Object) this) instanceof VaultEnchanterContainer)) {
//            QOLHunters.LOGGER.info("VaultEnchanterContainer clicked");
            qol$clicked(pSlotId, pButton, pClickType, pPlayer);
            ci.cancel();
        }

    }

    private int dragMode = -1;
    private int dragEvent;
    private final Set<Slot> dragSlots = Sets.newHashSet();

    @Unique
    public void qol$clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {

        AbstractContainerMenu thisContainer = (AbstractContainerMenu)(Object)this;


        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int j1 = this.dragEvent;
            this.dragEvent = getQuickcraftHeader(dragType);
            if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
                qol$resetQuickCraft();
            } else if (thisContainer.getCarried().isEmpty()) {
                qol$resetQuickCraft();
            } else if (this.dragEvent == 0) {
                this.dragMode = getQuickcraftType(dragType);
                if (isValidQuickcraftType(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    qol$resetQuickCraft();
                }
            } else if (this.dragEvent == 1) {
                Slot slot = thisContainer.slots.get(slotId);
                ItemStack mouseStack = thisContainer.getCarried();
                if (slot != null
                        && qol$canAddItemToSlot(slot, mouseStack, true)
                        && slot.mayPlace(mouseStack)
                        && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size())
                        && thisContainer.canDragTo(slot)) {
                    this.dragSlots.add(slot);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack mouseStackCopy = thisContainer.getCarried().copy();
                    int k1 = thisContainer.getCarried().getCount();

                    for (Slot dragSlot : this.dragSlots) {
                        ItemStack mouseStack = thisContainer.getCarried();
                        if (dragSlot != null
                                && qol$canAddItemToSlot(dragSlot, mouseStack, true)
                                && dragSlot.mayPlace(mouseStack)
                                && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size())
                                && thisContainer.canDragTo(dragSlot)) {
                            ItemStack itemstack14 = mouseStackCopy.copy();
                            int j3 = dragSlot.hasItem() ? dragSlot.getItem().getCount() : 0;
                            getQuickCraftSlotCount(this.dragSlots, this.dragMode, itemstack14, j3);
                            int k3 = dragSlot.getMaxStackSize(itemstack14);
                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            dragSlot.set(itemstack14);
                        }
                    }

                    mouseStackCopy.setCount(k1);
                    thisContainer.setCarried(mouseStackCopy);
                }

                qol$resetQuickCraft();
            } else {
                qol$resetQuickCraft();
            }
        } else if (this.dragEvent != 0) {
            qol$resetQuickCraft();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!thisContainer.getCarried().isEmpty()) {
                    if (dragType == 0) {
                        player.drop(thisContainer.getCarried(), true);
                        thisContainer.setCarried(ItemStack.EMPTY);
                    }

                    if (dragType == 1) {
                        player.drop(thisContainer.getCarried().split(1), true);
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return;
                }

                Slot slot = thisContainer.slots.get(slotId);
                if (slot == null || !slot.mayPickup(player)) {
                    return;
                }

                ItemStack itemstack7 = thisContainer.quickMoveStack(player, slotId);

                while (!itemstack7.isEmpty() && ItemStack.isSame(slot.getItem(), itemstack7)) {
                    itemstack7 = thisContainer.quickMoveStack(player, slotId);
                }
            } else {
                if (slotId < 0) {
                    return;
                }

                Slot slot = thisContainer.slots.get(slotId);
                if (slot != null) {
                    ItemStack slotStack = slot.getItem();
                    ItemStack mouseStack = thisContainer.getCarried();
                    if (slotStack.isEmpty()) {
                        if (!mouseStack.isEmpty() && slot.mayPlace(mouseStack)) {
                            int i3 = dragType == 0 ? mouseStack.getCount() : 1;
                            if (i3 > slot.getMaxStackSize(mouseStack)) {
                                i3 = slot.getMaxStackSize(mouseStack);
                            }

                            slot.set(mouseStack.split(i3));
                        }
                    } else if (slot.mayPickup(player)) {
                        if (mouseStack.isEmpty()) {
                            if (slotStack.isEmpty()) {
                                slot.set(ItemStack.EMPTY);
                                thisContainer.setCarried(ItemStack.EMPTY);
                            } else {
                                int slotCount = Math.min(slotStack.getCount(), slotStack.getMaxStackSize());
                                int toMove = dragType == 0 ? slotCount : (slotCount + 1) / 2;
                                thisContainer.setCarried(slot.remove(toMove));
                                if (slotStack.isEmpty()) {
                                    slot.set(ItemStack.EMPTY);
                                }

                                slot.onTake(player, thisContainer.getCarried());
                            }
                        } else if (slot.mayPlace(mouseStack)) {
                            if (slotStack.getItem() == mouseStack.getItem() && ItemStack.tagMatches(slotStack, mouseStack)) {
                                int k2 = dragType == 0 ? mouseStack.getCount() : 1;
                                if (k2 > slot.getMaxStackSize(mouseStack) - slotStack.getCount()) {
                                    k2 = slot.getMaxStackSize(mouseStack) - slotStack.getCount();
                                }

                                mouseStack.shrink(k2);
                                slotStack.grow(k2);
                                slot.set(slotStack);
                            } else if (mouseStack.getCount() <= slot.getMaxStackSize(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
                                slot.set(mouseStack);
                                thisContainer.setCarried(slotStack);
                            }
                        } else if (slotStack.getItem() == mouseStack.getItem()
                                && mouseStack.getMaxStackSize() > 1
                                && ItemStack.tagMatches(slotStack, mouseStack)
                                && !slotStack.isEmpty()) {
                            int j2 = slotStack.getCount();
                            if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                                mouseStack.grow(j2);
                                slotStack = slot.remove(j2);
                                if (slotStack.isEmpty()) {
                                    slot.set(ItemStack.EMPTY);
                                }

                                slot.onTake(player, thisContainer.getCarried());
                            }
                        }
                    }

                    slot.setChanged();
                }
            }
        } else if (clickTypeIn != ClickType.SWAP || dragType < 0 || dragType >= 9) {
            if (clickTypeIn == ClickType.CLONE && player.getAbilities().instabuild && thisContainer.getCarried().isEmpty() && slotId >= 0) {
                Slot slot3 = thisContainer.slots.get(slotId);
                if (slot3 != null && slot3.hasItem()) {
                    ItemStack itemstack5 = slot3.getItem().copy();
                    itemstack5.setCount(itemstack5.getMaxStackSize());
                    thisContainer.setCarried(itemstack5);
                }
            } else if (clickTypeIn == ClickType.THROW && thisContainer.getCarried().isEmpty() && slotId >= 0) {
                Slot slot = thisContainer.slots.get(slotId);
                if (slot != null && slot.hasItem() && slot.mayPickup(player)) {
                    int removeCount = Math.min(dragType == 0 ? 1 : slot.getItem().getCount(), slot.getItem().getMaxStackSize());
                    ItemStack itemstack4 = slot.remove(removeCount);
                    slot.onTake(player, itemstack4);
                    player.drop(itemstack4, true);
                }
            } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
                Slot slot = thisContainer.slots.get(slotId);
                ItemStack mouseStack = thisContainer.getCarried();
                if (!mouseStack.isEmpty() && (slot == null || !slot.hasItem() || !slot.mayPickup(player))) {
                    int i = dragType == 0 ? 0 : thisContainer.slots.size() - 1;
                    int j = dragType == 0 ? 1 : -1;

                    for (int k = 0; k < 2; k++) {
                        for (int l = i; l >= 0 && l < thisContainer.slots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += j) {
                            Slot slot1 = thisContainer.slots.get(l);
                            if (slot1.hasItem()
                                    && qol$canAddItemToSlot(slot1, mouseStack, true)
                                    && slot1.mayPickup(player)
                                    && thisContainer.canTakeItemForPickAll(mouseStack, slot1)) {
                                ItemStack itemstack2 = slot1.getItem();
                                if (k != 0 || itemstack2.getCount() < slot1.getMaxStackSize(itemstack2)) {
                                    int i1 = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack2.getCount());
                                    ItemStack itemstack3 = slot1.remove(i1);
                                    mouseStack.grow(i1);
                                    if (itemstack3.isEmpty()) {
                                        slot1.set(ItemStack.EMPTY);
                                    }

                                    slot1.onTake(player, itemstack3);
                                }
                            }
                        }
                    }
                }

                thisContainer.broadcastChanges();
            }
        }
    }


    @Unique
    protected void qol$resetQuickCraft() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

}
