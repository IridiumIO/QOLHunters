package io.iridium.qolhunters.mixin.betterlootstats;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.screen.summary.element.LootStatsContainerElement;
import iskallia.vault.client.gui.screen.summary.element.StatLabelListElement;
import iskallia.vault.core.vault.stat.VaultChestType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;
import java.util.Map;

@Mixin(value = LootStatsContainerElement.class, remap = false)
public class MixinLootStatsContainerElement {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Liskallia/vault/core/vault/stat/VaultChestType;equals(Ljava/lang/Object;)Z"))
    private boolean keepRawChests(VaultChestType instance, Object o, Operation<Boolean> original) {
        var orig = original.call(instance, o);
        if (orig && QOLHuntersClientConfigs.BETTER_LOOT_STATS.get()) {
            if (instance == VaultChestType.ENIGMA|| instance == VaultChestType.FLESH || instance == VaultChestType.HARDENED) {
                return false; // don't skip them
            }
        }
        return orig;
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0),slice =
    @Slice(
        from = @At(value = "INVOKE", target = "Liskallia/vault/core/vault/stat/VaultChestType;getIcon()Liskallia/vault/client/atlas/TextureAtlasRegion;"),
        to = @At(value = "INVOKE", target = "Liskallia/vault/core/vault/stat/VaultChestType;getDefaultChests()[Liskallia/vault/core/vault/stat/VaultChestType;")))
    private boolean smartChests(List<StatLabelListElement.Stat<?>> instance, Object e, Operation<Boolean> original,
                                @Local(name = "chestType") VaultChestType chestType,
                                @Local(name = "totalCounts") Map<VaultChestType, Integer> totalCounts,
                                @Local(name = "totalBarrelCounts") Map<VaultChestType, Integer> totalBarrelCounts) {
        if (!QOLHuntersClientConfigs.BETTER_LOOT_STATS.get()) {
            return original.call(instance, e);
        }

        if (chestType == VaultChestType.HARDENED || chestType == VaultChestType.ENIGMA || chestType == VaultChestType.FLESH) {
            var count = totalCounts.get(chestType);
            if (count == 0) {
                return false; // skip - non raw
            }
        }

        if (chestType == VaultChestType.WOODEN || chestType == VaultChestType.GILDED || chestType == VaultChestType.LIVING || chestType == VaultChestType.ORNATE) {
            var woodenChestCount = totalCounts.get(VaultChestType.WOODEN);
            var gildedChestCount = totalCounts.get(VaultChestType.GILDED);
            var livingChestCount = totalCounts.get(VaultChestType.LIVING);
            var ornateChestCount = totalCounts.get(VaultChestType.ORNATE);

            var woodenBarrelCount = totalBarrelCounts.get(VaultChestType.WOODEN);
            var gildedBarrelCount = totalBarrelCounts.get(VaultChestType.GILDED);
            var livingBarrelCount = totalBarrelCounts.get(VaultChestType.LIVING);
            var ornateBarrelCount = totalBarrelCounts.get(VaultChestType.ORNATE);

            var normalChestCount = woodenChestCount + gildedChestCount + livingChestCount + ornateChestCount;
            var normalBarrelCount = woodenBarrelCount + gildedBarrelCount + livingBarrelCount + ornateBarrelCount;
            var normalCount = normalChestCount + normalBarrelCount;

            var hardenedCount = totalCounts.get(VaultChestType.HARDENED);
            var enigmaCount = totalCounts.get(VaultChestType.ENIGMA);
            var fleshCount = totalCounts.get(VaultChestType.FLESH);
            var rawCount = hardenedCount + enigmaCount + fleshCount;
            if (normalCount == 0 && rawCount > 0) {
                return false; // skip normal - only raw chests
            }
        }
        return original.call(instance, e);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0),slice =
    @Slice(
        from = @At(value = "FIELD", target = "Liskallia/vault/client/gui/framework/ScreenTextures;WOODEN_BARREL_SMALL_ICON:Liskallia/vault/client/atlas/TextureAtlasRegion;", opcode = Opcodes.GETSTATIC),
        to = @At(value = "INVOKE", target = "Liskallia/vault/client/gui/screen/summary/element/LootStatsContainerElement;addElements(Liskallia/vault/client/gui/framework/element/spi/IElement;[Liskallia/vault/client/gui/framework/element/spi/IElement;)V")))
    private boolean smartBarrels(List<StatLabelListElement.Stat<?>> instance, Object e, Operation<Boolean> original,
                                 @Local(name = "type") VaultChestType type,
                                 @Local(name = "totalCounts") Map<VaultChestType, Integer> totalCounts,
                                 @Local(name = "totalBarrelCounts") Map<VaultChestType, Integer> totalBarrelCounts) {
        if (!QOLHuntersClientConfigs.BETTER_LOOT_STATS.get()) {
            return original.call(instance, e);
        }

        if (type == VaultChestType.WOODEN || type == VaultChestType.GILDED || type == VaultChestType.LIVING || type == VaultChestType.ORNATE) {
            var woodenChestCount = totalCounts.get(VaultChestType.WOODEN);
            var gildedChestCount = totalCounts.get(VaultChestType.GILDED);
            var livingChestCount = totalCounts.get(VaultChestType.LIVING);
            var ornateChestCount = totalCounts.get(VaultChestType.ORNATE);

            var woodenBarrelCount = totalBarrelCounts.get(VaultChestType.WOODEN);
            var gildedBarrelCount = totalBarrelCounts.get(VaultChestType.GILDED);
            var livingBarrelCount = totalBarrelCounts.get(VaultChestType.LIVING);
            var ornateBarrelCount = totalBarrelCounts.get(VaultChestType.ORNATE);

            var normalChestCount = woodenChestCount + gildedChestCount + livingChestCount + ornateChestCount;
            var normalBarrelCount = woodenBarrelCount + gildedBarrelCount + livingBarrelCount + ornateBarrelCount;
            var normalCount = normalChestCount + normalBarrelCount;

            var hardenedCount = totalCounts.get(VaultChestType.HARDENED);
            var enigmaCount = totalCounts.get(VaultChestType.ENIGMA);
            var fleshCount = totalCounts.get(VaultChestType.FLESH);
            var rawCount = hardenedCount + enigmaCount + fleshCount;
            if (normalCount == 0 && rawCount > 0) {
                return false; // skip normal - only raw chests
            }
        }
        return original.call(instance, e);
    }


    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 125))
    private int dynamicOffset(int original, @Local(name = "chestStats")  List<StatLabelListElement.Stat<?>> chestStats) {
        if (!QOLHuntersClientConfigs.BETTER_LOOT_STATS.get()) {
            return original;
        }
        return 10 + chestStats.size() * 11 + 10; // 10 + 5 * 10 + 10 = 75
    }
}
