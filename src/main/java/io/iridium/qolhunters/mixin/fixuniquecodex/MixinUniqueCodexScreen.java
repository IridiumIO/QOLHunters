package io.iridium.qolhunters.mixin.fixuniquecodex;

import com.llamalad7.mixinextras.sugar.Local;
import iskallia.vault.client.gui.screen.UniqueCodexScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value = UniqueCodexScreen.class, remap = false)
public class MixinUniqueCodexScreen {
    // leftY + categoryHeight > 190 => leftY + categoryHeight > INT_MAX


    /*
    (!onRightSide && leftY + categoryHeight > 190) => (!onRightSide && leftY + categoryHeight > 190 && categoryHeight !> 190 )
    * */
    @ModifyConstant(method = "renderIndexEntries", constant = @Constant(intValue = 190),
        slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", ordinal = 0)))
    private int removeAssignment(int constant, @Local(name = "categoryHeight") int categoryHeight) {
        if (30 + categoryHeight > constant) {
            return Integer.MAX_VALUE;
        }
        return constant;
    }
}
