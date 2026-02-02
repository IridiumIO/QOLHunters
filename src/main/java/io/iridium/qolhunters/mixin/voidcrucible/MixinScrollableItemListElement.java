package io.iridium.qolhunters.mixin.voidcrucible;

import io.iridium.qolhunters.features.searchablevaultstations.SearchableScreen;
import iskallia.vault.client.gui.screen.void_stone.elements.ScrollableItemListElement;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = ScrollableItemListElement.class, remap = false)
public class MixinScrollableItemListElement {


    // terms are separated by spaces
    // every term must match to show the itemstack
    // <word> => match in name or resource location path
    // @<word> => match in resource location namespace
    // $<word> => match in item tag
    @ModifyVariable(method = "<init>", at = @At(value = "STORE", ordinal = 0), name = "asItem")
    private Item hideNonMatchingSearch(Item asItem) {
        if (Minecraft.getInstance().screen instanceof SearchableScreen searchableScreen) {
            String filterText = searchableScreen.getSearchText().toLowerCase();

            String[] filters = filterText.split(" ");
            String name = asItem.getName(new ItemStack(asItem)).getString().toLowerCase();
            ResourceLocation id = asItem.getRegistryName();
            for (String filter : filters) {
                boolean matches = false;
                if (name.contains(filter)) {
                    matches = true;
                }
                if (!matches && id != null) {
                    if (id.getPath().toLowerCase().contains(filter)) {
                        matches = true;
                    }
                    if (filter.startsWith("@") && id.getNamespace().toLowerCase().contains(filter.substring(1))) {
                        matches = true;
                    }
                }
                if (filter.startsWith("$") &&
                    new ItemStack(asItem)
                        .getTags()
                        .map(TagKey::location)
                        .map(ResourceLocation::toString)
                        .map(String::toLowerCase)
                        .anyMatch( x-> x.contains(filter.substring(1)))
                ) {
                    matches = true;
                }
                if (!matches) {
                    return null;
                }
            }

            // every filter matched
            return asItem;
        }
        return null;
    }
}
