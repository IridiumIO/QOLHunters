package io.iridium.qolhunters.features.searchablevaultstations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface for screens that can be searched.
 * Always get the search text using {@link #getSearchText()} to ensure compatibility with different implementations.
 * Classes implementing this interface can override either {@link #qolhunters$getSearchBox()} or {@link #getSearchText()} to provide custom search functionality.
 * First override should be used to provide a custom search box element.
 * Second override should be used to provide custom search handling - for example JEI search.
 */
public interface SearchableScreen {
    default @Nullable QOLSearchElement qolhunters$getSearchBox(){
        return null;
    }
    default @Nonnull String getSearchText(){
        if (qolhunters$getSearchBox() == null) {
            return "";
        }
        String text = qolhunters$getSearchBox().getInput();
        return text == null ? "" : text;
    }
}
