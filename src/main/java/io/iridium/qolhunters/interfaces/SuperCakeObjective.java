package io.iridium.qolhunters.interfaces;

import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

public class SuperCakeObjective {

    public static Integer qol$colorIndex = 0;

    @Unique
    public static final Map<Integer, Integer> qOLHunters$colorMap = new java.util.HashMap<>() {{
        put(0, 0xFF77BA);
        put(1, 0x219ebc);
        put(2, 0xffc300);
    }};

}
