package io.iridium.qolhunters.interfaces;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

public class SuperCakeObjective {

    public static Integer qol$colorIndex = QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.get();

    public static Integer qol$overlayStyle = QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.get();

    @Unique
    public static final Map<Integer, Integer> COLORMAP = new java.util.HashMap<>() {{
        put(0, 0xFF77BA);  //Default Pink
        put(1, 0x219ebc);  //Blue
        put(2, 0xffc300);  //Yellow
        put(3, 0x77ba77);  //Green
    }};

}
