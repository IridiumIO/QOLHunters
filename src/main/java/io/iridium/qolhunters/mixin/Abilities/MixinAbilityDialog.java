package io.iridium.qolhunters.mixin.Abilities;

import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.client.gui.component.ScrollableContainer;
import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.client.gui.screen.player.AbilitiesElementContainerScreen;
import iskallia.vault.client.gui.screen.player.legacy.tab.split.dialog.AbilityDialog;
import iskallia.vault.client.gui.screen.player.legacy.tab.split.spi.AbstractDialog;
import iskallia.vault.client.gui.screen.player.legacy.widget.AbilityNodeTextures;
import iskallia.vault.client.gui.screen.player.legacy.widget.AbilityWidget;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModTextureAtlases;
import iskallia.vault.skill.ability.component.AbilityDescriptionFactory;
import iskallia.vault.skill.base.Skill;
import iskallia.vault.skill.base.SpecializedSkill;
import iskallia.vault.skill.base.TieredSkill;
import iskallia.vault.skill.tree.AbilityTree;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbilityDialog.class)
public abstract class MixinAbilityDialog extends AbstractDialog<AbilitiesElementContainerScreen> {

    @Final
    @Shadow(remap = false)
    private AbilityTree abilityTree;

    @Shadow(remap = false)
    private MutableComponent descriptionContentComponent;

    @Shadow(remap = false)
    private String selectedAbility;

    @Shadow(remap = false)
    private String prevSelectedAbility = null;

    @Shadow(remap = false)
    private int prevAbilityLevel = -1;

    @Shadow(remap = false)
    private AbilityWidget selectedAbilityWidget;




    protected MixinAbilityDialog(AbilitiesElementContainerScreen skillTreeScreen) {
        super(skillTreeScreen);
    }


    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void update() {
        // Check if an ability is selected
        if (this.selectedAbility == null) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // Retrieve the selected ability from the ability tree
        TieredSkill ability = (TieredSkill)this.abilityTree.getForId(this.selectedAbility).orElse(null);
        if (ability == null) return;

        // Get the parent skill of the selected ability
        SpecializedSkill parentAbility = (SpecializedSkill)ability.getParent();
        boolean isParentAbilitySpecialized = parentAbility.getIndex() != 0;

        // Create a new AbilityWidget for the selected ability
        this.selectedAbilityWidget = new AbilityWidget(
                this.selectedAbility,
                this.abilityTree,
                0,
                0,
                isParentAbilitySpecialized ? AbilityNodeTextures.SECONDARY_NODE : AbilityNodeTextures.PRIMARY_NODE,
                TextureAtlasRegion.of(ModTextureAtlases.ABILITIES, ModConfigs.ABILITIES_GUI.getIcon(this.selectedAbility))
        );

        // Get the current and target specialization skills
        SpecializedSkill current = this.selectedAbilityWidget.getAbilityGroup();
        SpecializedSkill target = this.selectedAbilityWidget.makeAbilityNode();

        Button.OnPress pressAction;
        String buttonText;
        boolean activeState;




        // Determine the button text and action based on whether the target is a specialization
        if (target.getIndex() != 0) {


            int cost = current.getSpecialization().getLearnPointCost();
            int regretCost = parentAbility.isUnlocked() ? parentAbility.getRegretPointCost() : 0;

            if (parentAbility.getIndex() == 0) {
                pressAction = button -> this.selectSpecialization();
                buttonText = "Select Specialization";

                activeState = parentAbility.getIndex() == 0
                        && (parentAbility.isUnlocked() || target.isUnlocked())
                        && VaultBarOverlay.vaultLevel >= target.getUnlockLevel();

            } else {
                pressAction = button -> this.upgradeAbility();
                buttonText = ((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier()
                        >= ((TieredSkill)target.getSpecialization()).getMaxLearnableTier()
                        ? "Fully Learned"
                        : "Upgrade (" + current.getSpecialization().getLearnPointCost() + ")";

                if((TieredSkill)parentAbility.getSpecialization() != ability) {
                    buttonText =  "Already using " + parentAbility.getSpecialization().getName();
                }

                activeState = cost <= VaultBarOverlay.unspentSkillPoints
                        && parentAbility.isUnlocked() && ability.isUnlocked() && ability.isUnlocked()
                        && ((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier() < ((TieredSkill)target.getSpecialization()).getMaxLearnableTier()
                        && ((TieredSkill)target.getSpecialization()).getUnmodifiedTier() < ((TieredSkill)target.getSpecialization()).getMaxLearnableTier() + 1
                        && VaultBarOverlay.vaultLevel >= current.getUnlockLevel();
            }



            // Determine the regret button text and state
            String regretButtonText = !parentAbility.isUnlocked() ? "Unlearn" : "Unlearn (" + regretCost + ")";
            boolean hasDependants = false;

            // Check if the ability has dependants that are unlocked
            if (((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier() == 1) {
                for (String dependent : ModConfigs.SKILL_GATES.getGates().getAbilitiesDependingOn(parentAbility.getId())) {
                    if (this.abilityTree.getForId(dependent).map(Skill::isUnlocked).orElse(false)) {
                        hasDependants = true;
                        break;
                    }
                }
            }

            // Create the regret button with the determined text and action
            this.regretButton = new Button(0, 0, 0, 0, new TextComponent(regretButtonText), button -> this.downgradeAbility(), Button.NO_TOOLTIP);
            this.regretButton.active = parentAbility.isUnlocked()
                    && regretCost <= VaultBarOverlay.unspentRegretPoints
                    && ((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier() > 0
                    && !hasDependants;


        } else {
            // If the target is not a specialization, determine the button text based on whether the target is unlocked

            // Set the button action to upgrade the ability
            pressAction = button -> this.upgradeAbility();
            int cost = current.getSpecialization().getLearnPointCost();
            int regretCost = parentAbility.isUnlocked() ? parentAbility.getRegretPointCost() : 0;


            if (!target.isUnlocked()) {
                buttonText = "Learn (" + current.getLearnPointCost() + ")";

                // Determine if the button should be active based on various conditions
                activeState = cost <= VaultBarOverlay.unspentSkillPoints
                        && ((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier()
                        < ((TieredSkill)target.getSpecialization()).getMaxLearnableTier()
                        && ((TieredSkill)target.getSpecialization()).getUnmodifiedTier()
                        < ((TieredSkill)target.getSpecialization()).getMaxLearnableTier() + 1
                        && VaultBarOverlay.vaultLevel >= current.getUnlockLevel();


            } else {
                buttonText = ((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier()
                        >= ((TieredSkill)target.getSpecialization()).getMaxLearnableTier()
                        ? "Fully Learned"
                        : "Upgrade (" + current.getSpecialization().getLearnPointCost() + ")";

                if((TieredSkill)parentAbility.getSpecialization() != ability) {
                    buttonText =  "Already using " + parentAbility.getSpecialization().getName();
                }

                // Determine if the button should be active based on various conditions
                activeState = cost <= VaultBarOverlay.unspentSkillPoints
                        && ((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier()
                        < ((TieredSkill)target.getSpecialization()).getMaxLearnableTier()
                        && ((TieredSkill)target.getSpecialization()).getUnmodifiedTier()
                        < ((TieredSkill)target.getSpecialization()).getMaxLearnableTier() + 1
                        && VaultBarOverlay.vaultLevel >= current.getUnlockLevel()
                        && !isParentAbilitySpecialized;

            }





            // Determine the regret button text and state
            String regretButtonText = !parentAbility.isUnlocked() ? "Unlearn" : "Unlearn (" + regretCost + ")";
            boolean hasDependants = false;

            // Check if the ability has dependants that are unlocked
            if (((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier() == 1) {
                for (String dependent : ModConfigs.SKILL_GATES.getGates().getAbilitiesDependingOn(parentAbility.getId())) {
                    if (this.abilityTree.getForId(dependent).map(Skill::isUnlocked).orElse(false)) {
                        hasDependants = true;
                        break;
                    }
                }
            }

            // Create the regret button with the determined text and action
            this.regretButton = new Button(0, 0, 0, 0, new TextComponent(regretButtonText), button -> this.downgradeAbility(), Button.NO_TOOLTIP);
            this.regretButton.active = parentAbility.isUnlocked()
                    && regretCost <= VaultBarOverlay.unspentRegretPoints
                    && ((TieredSkill)parentAbility.getSpecialization()).getUnmodifiedTier() > 0
                    && !hasDependants;
        }

        // Update the description component if the selected ability or its level has changed
        int descriptionTier = ((TieredSkill)current.getSpecialization()).getActualTier();
        if (!Objects.equals(this.selectedAbility, this.prevSelectedAbility) || this.prevAbilityLevel != descriptionTier) {
            int descriptionMaxTier = Math.max(((TieredSkill)current.getSpecialization()).getMaxLearnableTier(), descriptionTier);
            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);
            this.descriptionContentComponent = AbilityDescriptionFactory.create(
                    (TieredSkill)target.getSpecialization(), descriptionTier, descriptionMaxTier, VaultBarOverlay.vaultLevel
            );
            this.prevSelectedAbility = this.selectedAbility;
            this.prevAbilityLevel = descriptionTier;
        }

        // Create the learn button with the determined text and action
        this.learnButton = new Button(0, 0, 0, 0, new TextComponent(buttonText), pressAction, Button.NO_TOOLTIP);
        this.learnButton.active = activeState;


    }

    @Shadow(remap = false)
    protected abstract void selectSpecialization();

    @Shadow(remap = false)
    protected abstract void upgradeAbility();

    @Shadow(remap = false)
    protected abstract void downgradeAbility();

    @Shadow(remap = false)
    protected abstract void renderDescriptions(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks);

}
