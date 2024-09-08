package io.iridium.qolhunters.mixin.Abilities;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import java.util.Objects;

@Mixin(AbilityDialog.class)
public abstract class MixinAbilityDialog extends AbstractDialog<AbilitiesElementContainerScreen> {

    @Shadow(remap = false) @Final private AbilityTree abilityTree;
    @Shadow(remap = false) private MutableComponent descriptionContentComponent;
    @Shadow(remap = false) private String selectedAbility;
    @Shadow(remap = false) private String prevSelectedAbility = null;
    @Shadow(remap = false) private int prevAbilityLevel = -1;
    @Shadow(remap = false) private AbilityWidget selectedAbilityWidget;

    @Shadow(remap = false) protected abstract void selectSpecialization();
    @Shadow(remap = false) protected abstract void upgradeAbility();
    @Shadow(remap = false) protected abstract void downgradeAbility();
    @Shadow(remap = false) protected abstract void renderDescriptions(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks);


    protected MixinAbilityDialog(AbilitiesElementContainerScreen skillTreeScreen) {super(skillTreeScreen);}


    /**
     * Overwrites the update() method.
     * @author IridiumIO
     * @reason Large changes to the method to improve the abilities tab.
     */
    @Overwrite(remap = false)
    public void update() {

        if (this.selectedAbility == null || Minecraft.getInstance().player == null) return;

        TieredSkill activelySelectedAbility = (TieredSkill)this.abilityTree.getForId(this.selectedAbility).orElse(null);
        if (activelySelectedAbility == null) return;

        // Get the parent skill of the activelySelectedAbility
        SpecializedSkill parentAbility = (SpecializedSkill)activelySelectedAbility.getParent();

        boolean isParentAbilitySpecialized = parentAbility.getIndex() != 0;

        // Create a new AbilityWidget for the activelySelectedAbility
        this.selectedAbilityWidget = qOLHunters$createAbilityWidget(isParentAbilitySpecialized);


        // Get the current and target specialization skills (I think this refers to the skill levels?)
        SpecializedSkill current = this.selectedAbilityWidget.getAbilityGroup();
        SpecializedSkill target = this.selectedAbilityWidget.makeAbilityNode();

        boolean isTargetSpecialized = target.getIndex() != 0;


        Button.OnPress pressAction;
        String buttonText;
        boolean activeState;


        int cost = current.getSpecialization().getLearnPointCost();
        int regretCost = parentAbility.isUnlocked() ? parentAbility.getRegretPointCost() : 0;


        boolean hasEnoughSkillPoints = cost <= VaultBarOverlay.unspentSkillPoints;
        boolean isParentTierBelowMaxLearnable = ((TieredSkill) parentAbility.getSpecialization()).getUnmodifiedTier() < ((TieredSkill) target.getSpecialization()).getMaxLearnableTier();
        boolean isTargetTierBelowMaxLearnable = ((TieredSkill) target.getSpecialization()).getUnmodifiedTier() <= ((TieredSkill) target.getSpecialization()).getMaxLearnableTier();
        boolean isVaultLevelSufficient = VaultBarOverlay.vaultLevel >= current.getUnlockLevel();


        // Determine the button text and action based on whether the target is a specialization
        if (isTargetSpecialized) {

            if (!isParentAbilitySpecialized) {
                pressAction = button -> this.selectSpecialization();
                buttonText = "Select Specialization";
                activeState = parentAbility.getIndex() == 0
                        && (parentAbility.isUnlocked() || target.isUnlocked())
                        && VaultBarOverlay.vaultLevel >= target.getUnlockLevel();

            } else {
                pressAction = button -> this.upgradeAbility();
                buttonText = qOLHunters$determineButtonText(parentAbility, target, activelySelectedAbility, current);
                activeState = hasEnoughSkillPoints
                        && parentAbility.isUnlocked() && activelySelectedAbility.isUnlocked()
                        && isParentTierBelowMaxLearnable
                        && isTargetTierBelowMaxLearnable
                        && isVaultLevelSufficient;
            }

        } else { //The target is not specialized

            pressAction = button -> this.upgradeAbility();
            buttonText = "Learn (" + current.getLearnPointCost() + ")";

            activeState = hasEnoughSkillPoints
                        && isParentTierBelowMaxLearnable
                        && isTargetTierBelowMaxLearnable
                        && isVaultLevelSufficient;

            if (target.isUnlocked())  {
                buttonText = qOLHunters$determineButtonText(parentAbility, target, activelySelectedAbility, current);
                activeState = activeState && !isParentAbilitySpecialized;
            }
        }

        // Finally, Build the regret button, description component, and learn button
        qOLHunters$buildRegretButton(parentAbility, regretCost);
        qOLHunters$buildDescriptionComponent(current, target);
        qOLHunters$buildLearnButton(buttonText, pressAction, activeState);

    }


    //===========================================================================================================
    // Support Methods
    //===========================================================================================================


    /**
     * Builds the learn button with the specified parameters.
     *
     * @param buttonText The text to display on the button.
     * @param pressAction The action to perform when the button is pressed.
     * @param activeState The active state of the button.
     */
    @Unique
    private void qOLHunters$buildLearnButton(String buttonText, Button.OnPress pressAction, boolean activeState) {
        this.learnButton = new Button(0, 0, 0, 0, new TextComponent(buttonText), pressAction, Button.NO_TOOLTIP);
        this.learnButton.active = activeState;
    }



    /**
     * Builds the description component for the ability dialog.
     *
     * @param current The current specialization skill.
     * @param target The target specialization skill.
     */
    @Unique
    private void qOLHunters$buildDescriptionComponent(SpecializedSkill current, SpecializedSkill target) {
        // Update the description component if the selected ability or its level has changed
        int descriptionTier = ((TieredSkill) current.getSpecialization()).getActualTier();
        if (!Objects.equals(this.selectedAbility, this.prevSelectedAbility) || this.prevAbilityLevel != descriptionTier) {
            int descriptionMaxTier = Math.max(((TieredSkill) current.getSpecialization()).getMaxLearnableTier(), descriptionTier);
            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);
            this.descriptionContentComponent = AbilityDescriptionFactory.create(
                    (TieredSkill) target.getSpecialization(), descriptionTier, descriptionMaxTier, VaultBarOverlay.vaultLevel
            );
            this.prevSelectedAbility = this.selectedAbility;
            this.prevAbilityLevel = descriptionTier;
        }
    }

    /**
     * Builds the regret button for the ability dialog, and whether it is active.
     *
     * @param parentAbility The parent ability of the selected ability.
     * @param regretCost The cost to regret the ability.
     */
    @Unique
    private void qOLHunters$buildRegretButton(SpecializedSkill parentAbility, int regretCost) {

        String regretButtonText = !parentAbility.isUnlocked() ? "Unlearn" : "Unlearn (" + regretCost + ")";
        boolean hasDependants = false;

        // Check if the ability has dependants that are unlocked
        if (((TieredSkill) parentAbility.getSpecialization()).getUnmodifiedTier() == 1) {
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
                && ((TieredSkill) parentAbility.getSpecialization()).getUnmodifiedTier() > 0
                && !hasDependants;
    }


    /**
     * Determines the main button text (Learn, Upgrade) based on the state of the abilities and unlocks
     *
     * @param parentAbility The parent ability of the selected ability.
     * @param target The target specialization skill.
     * @param ability The selected ability.
     * @param current The current specialization skill.
     * @return The text to display on the button.
     */
    @Unique
    private static String qOLHunters$determineButtonText(SpecializedSkill parentAbility, SpecializedSkill target, TieredSkill ability, SpecializedSkill current) {
        String buttonText;
        boolean isMaxLevel = ((TieredSkill) parentAbility.getSpecialization()).getUnmodifiedTier() >= ((TieredSkill) target.getSpecialization()).getMaxLearnableTier();


        if (parentAbility.getSpecialization() != ability) {
            buttonText = "Already using " + parentAbility.getSpecialization().getName();
        } else if (isMaxLevel){
            buttonText = "Fully Learned";
        } else {
            buttonText = "Upgrade (" + current.getSpecialization().getLearnPointCost() + ")";
        }
        return buttonText;
    }


    /**
     * Creates a new AbilityWidget for the selected ability.
     *
     * @param isParentAbilitySpecialized Whether the parent ability is specialized.
     * @return The created AbilityWidget.
     */
    @Unique
    private AbilityWidget qOLHunters$createAbilityWidget(boolean isParentAbilitySpecialized) {
        return new AbilityWidget(
                this.selectedAbility,
                this.abilityTree,
                0,
                0,
                isParentAbilitySpecialized ? AbilityNodeTextures.SECONDARY_NODE : AbilityNodeTextures.PRIMARY_NODE,
                TextureAtlasRegion.of(ModTextureAtlases.ABILITIES, ModConfigs.ABILITIES_GUI.getIcon(this.selectedAbility))
        );
    }

}
