package io.iridium.qolhunters.features.vault_scavenger;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record NamedItem(Item item, String name) {
    public static NamedItem of(ItemStack itemStack) {
        return new NamedItem(itemStack.getItem(), itemStack.getHoverName().getString());
    }
}
