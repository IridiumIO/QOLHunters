package io.iridium.qolhunters.mixin.abilities;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.overlay.VaultMapOverlay;
import iskallia.vault.client.gui.screen.AbilitySelectionScreen;
import iskallia.vault.client.gui.screen.PrestigePowerSelectionScreen;
import iskallia.vault.client.gui.screen.achievements.AchievementScreen;
import iskallia.vault.client.gui.screen.bestiary.BestiaryScreen;
import iskallia.vault.client.gui.screen.map.FullScreenMapScreen;
import iskallia.vault.client.gui.screen.quest.QuestOverviewElementScreen;
import iskallia.vault.client.render.IVaultOptions;
import iskallia.vault.core.vault.ClientVaults;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.objective.RoyaleObjective;
import iskallia.vault.core.vault.stat.DiscoveredRoomStat;
import iskallia.vault.core.vault.stat.DiscoveredTunnelStat;
import iskallia.vault.core.vault.stat.StatCollector;
import iskallia.vault.event.InputEvents;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.*;
import iskallia.vault.network.message.bounty.ServerboundBountyProgressMessage;
import iskallia.vault.research.StageManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Mixin(value = InputEvents.class, remap = false)
public class MixinInputEvents {


    @Inject(method="onInput", at=@At("HEAD"), cancellable = true)
    private static void onInput(Minecraft minecraft, InputConstants.Key key, int action, CallbackInfo ci) {

        if (QOLHuntersClientConfigs.ABILITY_MULTICAST.get()) {
            qol$onInput(minecraft, key, action);
            ci.cancel();
        }

    }

    @Shadow
    private static boolean scrollingAbilities;

    @Shadow
    private static boolean isShiftDown;

    @Shadow
    private static void checkAndReleaseHoldAbility(InputConstants.Key key) {
    }

    @Shadow
    private static final java.util.Set<InputConstants.Key> KEY_DOWN_SET = new java.util.HashSet<>();

    @Shadow
    private static boolean isKeyDown(InputConstants.Key key) {
        return KEY_DOWN_SET.contains(key);
    }


    //I'm going to regret reorganizing this later
    @Unique
    private static void qol$onInput(Minecraft minecraft, InputConstants.Key key, int action) {
        if (key == InputConstants.UNKNOWN) return;


        if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
            KEY_DOWN_SET.add(key);
        } else if (action == GLFW.GLFW_RELEASE) {
            KEY_DOWN_SET.remove(key);
        }


        if (minecraft.screen != null) {
            if (action == GLFW.GLFW_RELEASE) {
                checkAndReleaseHoldAbility(key);
                scrollingAbilities = false;
            }
            return;
        }


        if (action == GLFW.GLFW_RELEASE){
            if ((!isKeyDown(ModKeybinds.abilityKey.getKey()) || !ModKeybinds.abilityKey.getKeyModifier().matches(key)) && !ModKeybinds.abilityKey.getKey().equals(key)) {
                for (Map.Entry<String, KeyMapping> entry : ModKeybinds.abilityQuickfireKey.entrySet()) {
                    KeyMapping keyMapping = entry.getValue();
                    if ((isKeyDown(keyMapping.getKey()) && keyMapping.getKeyModifier().matches(key)) || keyMapping.getKey().equals(key)) {
                        ModNetwork.CHANNEL.sendToServer(new AbilityQuickselectMessage(entry.getKey(), 0));
                    }
                }
            } else {
                if (!scrollingAbilities) {
                    ServerboundAbilityKeyMessage.send(iskallia.vault.network.message.ServerboundAbilityKeyMessage.Opcode.KeyUp);
                } else {
                    scrollingAbilities = false;
                }
            }
            return;
        }


        if (action == GLFW.GLFW_PRESS) {
            for (Map.Entry<String, KeyMapping> entry : ModKeybinds.abilityQuickfireKey.entrySet()) {
                if (entry.getValue().isActiveAndMatches(key)) {
                    ModNetwork.CHANNEL.sendToServer(new AbilityQuickselectMessage(entry.getKey(), 1));
                }
            }

            if (isShiftDown && ToolMessage.Offset.isKey(key.getValue())) {
                ToolMessage.sendOffset(key.getValue());
                return;
            }

            if (isShiftDown) {
                if (key.getValue() == GLFW.GLFW_KEY_LEFT) {
                    ServerboundPickaxeOffsetKeyMessage.send(ServerboundPickaxeOffsetKeyMessage.Opcode.LEFT);
                    return;
                }

                if (key.getValue() == GLFW.GLFW_KEY_RIGHT) {
                    ServerboundPickaxeOffsetKeyMessage.send(ServerboundPickaxeOffsetKeyMessage.Opcode.RIGHT);
                    return;
                }

                if (key.getValue() == GLFW.GLFW_KEY_UP) {
                    ServerboundPickaxeOffsetKeyMessage.send(ServerboundPickaxeOffsetKeyMessage.Opcode.UP);
                    return;
                }

                if (key.getValue() == GLFW.GLFW_KEY_DOWN) {
                    ServerboundPickaxeOffsetKeyMessage.send(ServerboundPickaxeOffsetKeyMessage.Opcode.DOWN);
                    return;
                }
            }

            if (ModKeybinds.abilityKey.isActiveAndMatches(key)) {
                ServerboundAbilityKeyMessage.send(ServerboundAbilityKeyMessage.Opcode.KeyDown);
            } else if (ModKeybinds.abilityWheelKey.isActiveAndMatches(key)) {
                minecraft.setScreen(new AbilitySelectionScreen());
                ServerboundAbilityKeyMessage.send(ServerboundAbilityKeyMessage.Opcode.CancelKeyDown);
            } else if (ModKeybinds.prestigeActivationScreenKey.isActiveAndMatches(key)) {
                minecraft.setScreen(new PrestigePowerSelectionScreen());
            }  else if (ModKeybinds.openAbilityTree.isActiveAndMatches(key)) {
                ModNetwork.CHANNEL.sendToServer(ServerboundOpenStatisticsMessage.INSTANCE);
            } else if (ModKeybinds.bountyStatusKey.isActiveAndMatches(key)) {
                ModNetwork.CHANNEL.sendToServer(ServerboundBountyProgressMessage.INSTANCE);
            } else if (ModKeybinds.angelToggleKey.isActiveAndMatches(key)) {
                ModNetwork.CHANNEL.sendToServer(AngelToggleMessage.INSTANCE);
            } else if (ModKeybinds.magnetToggleKey.isActiveAndMatches(key)) {
                ModNetwork.CHANNEL.sendToServer(ServerboundMagnetToggleMessage.INSTANCE);
            } else if (ModKeybinds.useCompanionTemporal.isActiveAndMatches(key)) {
                ModNetwork.CHANNEL.sendToServer(ServerboundUseCompanionTemporalMessage.INSTANCE);
            } else if (ModKeybinds.openQuestScreen.isActiveAndMatches(key)) {
                Minecraft.getInstance().setScreen(new QuestOverviewElementScreen());
            } else if (ModKeybinds.openBestiary.isActiveAndMatches(key)) {
                Minecraft.getInstance().setScreen(new BestiaryScreen());
            } else if (ModKeybinds.openAchievements.isActiveAndMatches(key)) {
                Minecraft.getInstance().setScreen(new AchievementScreen());
            } else if (ModKeybinds.openCardDeck.isActiveAndMatches(key)) {
                ModNetwork.CHANNEL.sendToServer(new OpenCardDeckMessage());
            } else if (ModKeybinds.openVaultMap.isActiveAndMatches(key)) {
                Optional<Vault> vaultOpt = ClientVaults.getActive();
                if (vaultOpt.isPresent()
                    && (StageManager.RESEARCH_TREE.isResearched("Vault Map") || !vaultOpt.get().get(Vault.OBJECTIVES).getAll(RoyaleObjective.class).isEmpty())
                )
                {
                    if (minecraft.player != null && minecraft.player.getInventory().hasAnyOf(Set.of(ModItems.OPEN_MAP))) {
                        Vault vault = vaultOpt.get();
                        StatCollector statCollector = vault.get(Vault.STATS).get(minecraft.player.getUUID());
                        DiscoveredRoomStat roomStat = statCollector.get(StatCollector.ROOMS_DISCOVERED);
                        DiscoveredTunnelStat tunnelStat = statCollector.get(StatCollector.DISCOVERED_TUNNELS);
                        minecraft.setScreen(new FullScreenMapScreen(vault, roomStat, tunnelStat));
                    } else if (minecraft.player != null) {
                        minecraft.player.displayClientMessage(new TextComponent("You need a Vault Map to view the vault map!"), true);
                    }
                }
            } else if (ModKeybinds.holdVaultMap.isActiveAndMatches(key) && ClientVaults.getActive().isPresent()) {
                Vault vault = ClientVaults.getActive().get();
                if (StageManager.RESEARCH_TREE.isResearched("Vault Map") || !vault.get(Vault.OBJECTIVES).getAll(RoyaleObjective.class).isEmpty()) {
                    if ((minecraft.player == null || !minecraft.player.getInventory().hasAnyOf(Set.of(ModItems.OPEN_MAP)))
                        && !VaultMapOverlay.showLargeOverlay) {
                        if (minecraft.player != null) {
                            minecraft.player.displayClientMessage(new TextComponent("You need a Vault Map to view the vault map!"), true);
                        }
                    } else {
                        IVaultOptions options = (IVaultOptions)Minecraft.getInstance().options;
                        if (options.isToggleMapOverlay()) {
                            VaultMapOverlay.showLargeOverlay = !VaultMapOverlay.showLargeOverlay;
                        }
                    }
                }
            } else if (ModKeybinds.cycleSpectatorKey.isActiveAndMatches(key) && minecraft.player.isSpectator()) {
                ModNetwork.CHANNEL.sendToServer(new ServerboundChangeSpectatorMessage());
            }
        }

    }

}
