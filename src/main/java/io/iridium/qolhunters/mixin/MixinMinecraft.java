//package io.iridium.qolhunters.mixin;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.client.gui.screens.TitleScreen;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(Minecraft.class)
//public class MixinMinecraft {
//    @Unique
//    private static boolean wasLoaded;
//
//    @Inject(method = "setScreen", at = @At("TAIL"))
//    private void autoLoadLevel(Screen pGuiScreen, CallbackInfo ci) {
//        if (pGuiScreen instanceof TitleScreen && !wasLoaded) {
//            wasLoaded = true;
//            Minecraft client = Minecraft.getInstance();
//
//            if (client.getLevelSource().levelExists("Creative")) {
//                client.loadLevel("Creative");
//            }
//        }
//    }
//}
