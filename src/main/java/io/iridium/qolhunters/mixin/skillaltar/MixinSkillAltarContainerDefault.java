package io.iridium.qolhunters.mixin.skillaltar;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.config.SkillAltarConfig;
import iskallia.vault.container.SkillAltarContainer;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.world.data.SkillAltarData;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(SkillAltarContainer.Default.class)
public abstract class MixinSkillAltarContainerDefault  extends SkillAltarContainer{

    protected MixinSkillAltarContainerDefault(MenuType<?> menuType, int id, Inventory playerInventory, BlockPos pos, @Nullable SkillAltarData.SkillTemplate template, int templateIndex, List<SkillAltarData.SkillIcon> skillIcons) {
        super(menuType, id, playerInventory, pos, template, templateIndex, skillIcons);
    }



    @Inject(method="saveTemplate", at= @At(value = "INVOKE", target = "Lnet/minecraftforge/network/simple/SimpleChannel;sendToServer(Ljava/lang/Object;)V", shift = At.Shift.AFTER), remap = false)
    private void saveTemplate(CallbackInfo ci) {
        if (!QOLHuntersClientConfigs.SAVE_KEYBINDS_WITH_SKILL_ALTAR.get()) return;

        Map<String, Integer> keyBindsBackup = new HashMap<>();
        KeyMapping[] keyBindings = Minecraft.getInstance().options.keyMappings;

        for (KeyMapping keyBinding : keyBindings) {
            if (!keyBinding.getCategory().equals(ModKeybinds.QUICKFIRE_CATEGORY)) continue;

            int keyVal = keyBinding.getKey().getValue();
            if(keyBinding.getKey().getType() == InputConstants.Type.MOUSE) {
                keyVal = -keyBinding.getKey().getValue() - 10;
            }

            keyBindsBackup.put(keyBinding.getName(), keyVal);
        }


        String saveName;

        if(Minecraft.getInstance().hasSingleplayerServer()){
            saveName = Objects.requireNonNull(Objects.requireNonNull(Minecraft.getInstance().getSingleplayerServer()).getWorldData().getLevelName());
        } else {
            saveName = Objects.requireNonNull(Minecraft.getInstance().getCurrentServer()).name + "_" + Minecraft.getInstance().getCurrentServer().ip;
        }

        if (QOLHunters.SKILL_ALTAR_CONFIG.KEYBINDINGS.containsKey(saveName)) {
            QOLHunters.SKILL_ALTAR_CONFIG.KEYBINDINGS.get(saveName).put(this.templateIndex, keyBindsBackup);
        } else {
            Map<Integer, Map<String, Integer>> keybinds = new HashMap<>();
            keybinds.put(this.templateIndex, keyBindsBackup);
            QOLHunters.SKILL_ALTAR_CONFIG.KEYBINDINGS.put(saveName, keybinds);
        }

        QOLHunters.SKILL_ALTAR_CONFIG.save();

    }

    @Inject(method="setPlayerAbilitiesAndTalentsFromTemplate()V", at= @At(value = "INVOKE", target = "Lnet/minecraftforge/network/simple/SimpleChannel;sendToServer(Ljava/lang/Object;)V", shift = At.Shift.AFTER), remap = false)
    private void loadTemplate(CallbackInfo ci) {
        if (!QOLHuntersClientConfigs.SAVE_KEYBINDS_WITH_SKILL_ALTAR.get()) return;
        QOLHunters.SKILL_ALTAR_CONFIG = SkillAltarConfig.load();

        String saveName;

        if(Minecraft.getInstance().hasSingleplayerServer()){
            saveName = Objects.requireNonNull(Objects.requireNonNull(Minecraft.getInstance().getSingleplayerServer()).getWorldData().getLevelName());
        } else {
            saveName = Objects.requireNonNull(Minecraft.getInstance().getCurrentServer()).name + "_" + Minecraft.getInstance().getCurrentServer().ip;
        }

        Map<Integer, Map<String, Integer>> keyBindsBackupIndexed = QOLHunters.SKILL_ALTAR_CONFIG.KEYBINDINGS
                .getOrDefault(saveName, Collections.emptyMap());

        if(keyBindsBackupIndexed == null) return;
        Map<String, Integer> keyBindsBackup = keyBindsBackupIndexed.get(this.templateIndex);

        if (keyBindsBackup == null) return;

        KeyMapping[] keyBindings = Minecraft.getInstance().options.keyMappings;

        for (KeyMapping keyBinding : keyBindings) {
            if (!keyBinding.getCategory().equals(ModKeybinds.QUICKFIRE_CATEGORY)) continue;
            Integer key = keyBindsBackup.get(keyBinding.getName());
            if (key == null) continue;
            if (key < -9) {
                key = -key -10;
                keyBinding.setKey(InputConstants.Type.MOUSE.getOrCreate(key));
            } else {
                keyBinding.setKey(InputConstants.Type.KEYSYM.getOrCreate(key));
            }

        }

    }

}
