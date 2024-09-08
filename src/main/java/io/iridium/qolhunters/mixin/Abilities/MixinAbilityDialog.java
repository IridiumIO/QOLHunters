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
import iskallia.vault.skill.ability.component.AbilityLabelBindingRegistry;
import iskallia.vault.skill.ability.component.AbilityLabelContext;
import iskallia.vault.skill.ability.component.AbilityLabelFactory;
import iskallia.vault.skill.base.LearnableSkill;
import iskallia.vault.skill.base.Skill;
import iskallia.vault.skill.base.SpecializedSkill;
import iskallia.vault.skill.base.TieredSkill;
import iskallia.vault.skill.tree.AbilityTree;
import iskallia.vault.util.calc.CooldownHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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


        try {
            HijackAbilityLabelFactory();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


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
        try {qOLHunters$buildDescriptionComponent(current, target);} catch (Exception e) {QOLHunters.LOGGER.error(e.getMessage());}
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
     * Builds the description component for the ability dialog. Includes all ability levels and overlevels.
     *
     * @param current The current specialization skill.
     * @param target The target specialization skill.
     */
    @Unique
    private void qOLHunters$buildDescriptionComponent(SpecializedSkill current, SpecializedSkill target) throws NoSuchFieldException, IllegalAccessException {
        // Update the description component if the selected ability or its level has changed
        int descriptionTier = ((TieredSkill) current.getSpecialization()).getActualTier();
        if (!Objects.equals(this.selectedAbility, this.prevSelectedAbility) || this.prevAbilityLevel != descriptionTier) {
            int descriptionMaxTier = Math.max(((TieredSkill) current.getSpecialization()).getMaxLearnableTier(), descriptionTier);
            this.descriptionComponent = new ScrollableContainer(this::renderDescriptions);
            this.descriptionContentComponent = AbilityDescriptionFactory.create(
                    (TieredSkill) target.getSpecialization(), descriptionTier, descriptionMaxTier, VaultBarOverlay.vaultLevel
            );
            this.descriptionContentComponent.append("\n\n");

            TieredSkill tieredSkill = (TieredSkill) target.getSpecialization();

            Field tiers = tieredSkill.getClass().getDeclaredField("tiers");
            tiers.setAccessible(true);
            List<LearnableSkill> childTiers = (List<LearnableSkill>) tiers.get(tieredSkill);


            int containerWidth =  this.getDescriptionsBounds().width;
            int charWidth = Minecraft.getInstance().font.width("⋮");
            int headerWidth = Minecraft.getInstance().font.width(" All Levels ");

            int numChars = (containerWidth - headerWidth) / (2 * charWidth) - 12;

            String separator = new String(new char[numChars]).replace('\0', '⋮');
            this.descriptionContentComponent.append(new TextComponent("\n" + separator).withStyle(ChatFormatting.DARK_GRAY));
            this.descriptionContentComponent.append(new TextComponent(" All Levels ").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.BOLD));
            this.descriptionContentComponent.append(new TextComponent( separator).withStyle(ChatFormatting.DARK_GRAY));

            for (int i = 1; i <= descriptionMaxTier; i++) {
                QOLHunters.LOGGER.info("Tier: " + i);
                List<String> keys = ModConfigs.ABILITIES_DESCRIPTIONS.getCurrent(tieredSkill.getId());
                TextComponent header = (TextComponent) new TextComponent("\n\nLevel " + i).withStyle(ChatFormatting.BOLD);
                qOLHunters$appendLabels(descriptionContentComponent, keys, header, new AbilityLabelContext<>(tieredSkill.getChild(i), VaultBarOverlay.vaultLevel));
            }

            for (int i = descriptionMaxTier+1; i <= childTiers.size(); i++) {
                QOLHunters.LOGGER.info("OverTier: " + i);
                List<String> keys = ModConfigs.ABILITIES_DESCRIPTIONS.getCurrent(tieredSkill.getId());
                TextComponent header = (TextComponent) new TextComponent("\n\n§kO§r ").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_RED).append(new TextComponent("Level " + i).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLACK));
                qOLHunters$appendLabels(descriptionContentComponent, keys, header, new AbilityLabelContext<>(tieredSkill.getChild(i), VaultBarOverlay.vaultLevel));
            }


            this.prevSelectedAbility = this.selectedAbility;
            this.prevAbilityLevel = descriptionTier;
        }
    }



    //TODO: None of this should be in this mixin at all. Move it to a separate class.
    void HijackAbilityLabelFactory() throws NoSuchFieldException, IllegalAccessException {
       Field FACTORY_MAP_FIELD = AbilityLabelFactory.class.getDeclaredField("FACTORY_MAP");
        FACTORY_MAP_FIELD.setAccessible(true);

        // Retrieve the value of the field
        Map<String, AbilityLabelFactory.IAbilityComponentFactory> factoryMap =
                (Map<String, AbilityLabelFactory.IAbilityComponentFactory>) FACTORY_MAP_FIELD.get(null);


        Player player = Minecraft.getInstance().player;

        QOLHunters.LOGGER.info("CooldownMultiplier: " + (player != null ? CooldownHelper.getCooldownMultiplier(player) : 0.0F));

        factoryMap.put("cooldown",
                context -> labelWithCooldownValue(
                        "\n Cooldown: ",
                        binding(context.config(), "cooldown"),
                        "cooldown",
                        player != null ? CooldownHelper.getCooldownMultiplier(player) : 0.0F
                ));

    }

    private static <C extends Skill> String binding(C config, String key) {
        return AbilityLabelBindingRegistry.getBindingValue(config, key);
    }

    private static MutableComponent labelWithCooldownValue(String label, String value, String colorKey, float abilityValue) {
        float result = Float.parseFloat(value.replace("s", ""));
        return new TextComponent(label)
                .withStyle(Style.EMPTY.withColor(ModConfigs.COLORS.getColor("text")))
                .append(text(value, colorKey).append(text(" (" + String.format("%.1f", result * (1.0 - abilityValue)) + "s)", colorKey)));
    }

    private static MutableComponent text(String text, String colorKey) {
        return text(text, ModConfigs.COLORS.getColor(colorKey));
    }

    private static MutableComponent text(String text, TextColor color) {
        return new TextComponent(text).withStyle(Style.EMPTY.withColor(color));
    }







     @Unique
     void qOLHunters$appendLabels(MutableComponent component, List<String> keys, TextComponent header, AbilityLabelContext<?> context) {
         if (keys.isEmpty()) return;
         component.append(header);
         for (String key : keys) {

             component.append(AbilityLabelFactory.create(key, context));
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
