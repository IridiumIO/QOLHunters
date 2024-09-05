package io.iridium.qolhunters.mixin.vaultenchanter;

import io.iridium.qolhunters.interfaces.IModifiedInventory;
import iskallia.vault.client.gui.framework.ScreenTextures;
import iskallia.vault.client.gui.framework.element.*;
import iskallia.vault.client.gui.framework.render.TooltipDirection;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.text.LabelTextStyle;
import iskallia.vault.client.gui.screen.block.VaultEnchanterScreen;
import iskallia.vault.container.VaultEnchanterContainer;
import iskallia.vault.container.oversized.OverSizedInventory;
import iskallia.vault.util.EnchantmentEntry;
import iskallia.vault.util.InventoryUtil;
import iskallia.vault.util.RomanNumber;
import iskallia.vault.util.function.ObservableSupplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

import static iskallia.vault.client.gui.screen.block.VaultEnchanterScreen.VAULT_ENCHANTER_BOOK_TEXTURE;

@Mixin(VaultEnchanterScreen.class)
public abstract class MixinVaultEnchanterScreen extends AbstractElementContainerScreen<VaultEnchanterContainer> {


    @Shadow(remap = false) @Final
    private Inventory playerInventory;
    @Shadow(remap = false) @Final
    private ObservableSupplier<ItemStack> inputItemStack;

    @Shadow(remap = false) @Mutable @Final
    private TextInputElement<?> searchInput;

    @Shadow(remap = false) @Mutable @Final
    private  EnchantBookElement<?> bookElement;

    @Shadow(remap = false) @Mutable @Final
    private  EnchanterEnchantSelectorElement<?, ?> selectorElement;

    @Shadow(remap = false) @Mutable @Final
    private EnchantmentEntry selectedEnchantmentEntry;

    protected MixinVaultEnchanterScreen(VaultEnchanterContainer container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<VaultEnchanterContainer>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @Inject(method = "<init>(Liskallia/vault/container/VaultEnchanterContainer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/chat/Component;)V", at = @At("TAIL"), remap = false)
    public void VaultEnchanterScreen(VaultEnchanterContainer container, Inventory inventory, Component title, CallbackInfo ci) {

        OverSizedInventory modifiedInventory = ((IModifiedInventory) container.getTileEntity()).getOverSizedInventory();

        this.removeAllElements();

        this.addElement(
                (NineSliceElement)new NineSliceElement(this.getGuiSpatial(), ScreenTextures.DEFAULT_WINDOW_BACKGROUND)
                        .layout((screen, gui, parent, world) -> world.translateXY(gui).size(Spatials.copy(gui)))
        );
        this.addElement(
                (LabelElement)new LabelElement(
                        Spatials.positionXY(8, 7),
                        this.getMenu().getTileEntity().getDisplayName().copy().withStyle(Style.EMPTY.withColor(-12632257)),
                        LabelTextStyle.defaultStyle()
                )
                        .layout((screen, gui, parent, world) -> world.translateXY(gui))
        );
        MutableComponent inventoryName = inventory.getDisplayName().copy();
        inventoryName.withStyle(Style.EMPTY.withColor(-12632257));
        this.addElement(
                (LabelElement)new LabelElement(Spatials.positionXY(8, 120), inventoryName, LabelTextStyle.defaultStyle())
                        .layout((screen, gui, parent, world) -> world.translateXY(gui))
        );
        this.addElement((SlotsElement)new SlotsElement(this).layout((screen, gui, parent, world) -> world.positionXY(gui)));
        this.addElement(
                this.searchInput = (TextInputElement<?>) new TextInputElement(Spatials.positionXY(110, 5).size(60, 12), Minecraft.getInstance().font)
                        .layout((screen, gui, parent, world) -> world.translateXY(gui))
        );
        this.addElement(
                this.selectorElement = (EnchanterEnchantSelectorElement<?, ?>) new EnchanterEnchantSelectorElement(
                        Spatials.positionXY(8, 19).height(97), ObservableSupplier.ofIdentity(() -> this.getMenu().getInput()), this.searchInput::getInput
                )
                        .layout((screen, gui, parent, world) -> world.translateXY(gui))
        );
        ButtonElement<?> craftButton;
        this.addElement(
                craftButton = (ButtonElement<?>) new ButtonElement(Spatials.positionXY(145, 68), ScreenTextures.BUTTON_CRAFT_TEXTURES, this::tryCraft)
                        .layout((screen, gui, parent, world) -> world.translateXY(gui))
        );
        craftButton.tooltip(
                (tooltipRenderer, poseStack, mouseX, mouseY, tooltipFlag) -> {
                    if (this.selectedEnchantmentEntry == null) return false;

                    ItemStack gear = this.getMenu().getInput();
                    if (gear.isEmpty()) return false;

                    List<ItemStack> itemCost = this.selectedEnchantmentEntry.getCost().getItems();
                    List<ItemStack> missing = InventoryUtil.getMissingInputs(itemCost, this.playerInventory, modifiedInventory);
                    int levelCost = this.selectedEnchantmentEntry.getCost().getLevels();
                    boolean playerHasLevels = this.playerInventory.player.experienceLevel >= levelCost;
                    List<Component> tooltip = new LinkedList<>();
                    if (missing.isEmpty() && playerHasLevels) {
                        tooltip.add(new TextComponent("Enchant ").append(gear.getHoverName().copy()));
                        tooltip.add(
                                new TextComponent("with ")
                                        .append(
                                                new TranslatableComponent(this.selectedEnchantmentEntry.getEnchantment().getDescriptionId())
                                                        .append(" " + RomanNumber.toRoman(this.selectedEnchantmentEntry.getLevel()))
                                                        .withStyle(ChatFormatting.LIGHT_PURPLE)
                                        )
                                        .append(" !")
                        );
                    } else {
                        tooltip.add(new TextComponent("Missing ingredients.").withStyle(ChatFormatting.RED));
                        tooltip.add(new TextComponent(""));

                        for (ItemStack costStack : this.selectedEnchantmentEntry.getCost().getItems()) {
                            tooltip.add(
                                    new TextComponent(costStack.getCount() + "x ")
                                            .append(costStack.getHoverName().copy())
                                            .withStyle(missing.contains(costStack) ? ChatFormatting.RED : ChatFormatting.GREEN)
                            );
                        }

                        if (levelCost != 0) {
                            tooltip.add(new TextComponent(levelCost + " EXP Levels").withStyle(playerHasLevels ? ChatFormatting.GREEN : ChatFormatting.RED));
                        }
                    }

                    tooltipRenderer.renderTooltip(poseStack, tooltip, mouseX, mouseY, ItemStack.EMPTY, TooltipDirection.RIGHT);
                    return true;


                }
        );
        craftButton.setDisabled(() -> {
            ItemStack gear = this.getMenu().getInput();
            if (gear.isEmpty()) {
                return true;
            } else if (this.selectedEnchantmentEntry != null) {
                if (this.playerInventory.player.isCreative()) {
                    return false;
                } else {
                    List<ItemStack> inputs = this.selectedEnchantmentEntry.getCost().getItems();
                    List<ItemStack> missing = InventoryUtil.getMissingInputs(inputs, this.playerInventory, modifiedInventory);
                    int levelCost = this.selectedEnchantmentEntry.getCost().getLevels();
                    return !missing.isEmpty() || this.playerInventory.player.experienceLevel < levelCost;
                }
            } else {
                return true;
            }
        });
        this.bookElement = (EnchantBookElement<?>) this.addElement(
                new EnchantBookElement(
                        Spatials.zero(),
                        Spatials.size(100, 100),
                        () -> this.selectedEnchantmentEntry == null ? 0 : this.selectedEnchantmentEntry.getCost().getLevels(),
                        () -> !container.getInput().isEmpty() && this.selectedEnchantmentEntry != null
                )
                        .withCustomTexture(VAULT_ENCHANTER_BOOK_TEXTURE)
                        .layout((screen, gui, parent, world) -> world.translateXYZ(gui).translateX(149).translateY(45))
        );
        this.selectorElement.onSelect(option -> this.selectedEnchantmentEntry = option);
        this.searchInput.onTextChanged(text -> this.selectorElement.refreshElements());
    }

    @Shadow(remap = false)
    abstract void tryCraft();

}
