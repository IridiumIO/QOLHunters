package io.iridium.qolhunters.mixin.multilinestatlabels;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.screen.player.element.StatLabelElementBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(value = StatLabelElementBuilder.class, remap = false)
public abstract class MixinStatLabelElementBuilder<V extends Comparable<V>> {
    @Shadow @Final private Supplier<String> labelSupplier;

    @Shadow protected abstract Component buildCapDescription(MutableComponent value, MutableComponent valueCap);

    @Shadow @Final private Function<V, MutableComponent> valueFormatFunction;

    @Shadow @Final private Supplier<V> valueSupplier;

    @Shadow private Function<V, MutableComponent> valueCapFormatFunction;

    @Shadow private Supplier<V> valueCapSupplier;

    @Shadow @Final private Supplier<String> descriptionSupplier;

    @Redirect(method = "lambda$build$5", at = @At(value = "INVOKE", target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;"))
    private List<Component> splitCapped(Object e1, Object e2, Object e3) {
        if(QOLHuntersClientConfigs.MULTILINE_STAT_LABELS.get()){
            List<Component> compList = new ArrayList<>();
            compList.add(new TextComponent(this.labelSupplier.get()));
            compList.add(this.buildCapDescription(
                this.valueFormatFunction.apply(this.valueSupplier.get()), this.valueCapFormatFunction.apply(this.valueCapSupplier.get())
            ));

            String descComponent = this.descriptionSupplier.get();
            String[] descLines = descComponent.split("\n");
            for (String line : descLines) {
                compList.add(new TextComponent(line).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(11184810))));
            }
            return Collections.unmodifiableList(compList);
        }
        // Fallback to original behavior if the config is not enabled
        return List.of(
            new TextComponent(this.labelSupplier.get()),
            this.buildCapDescription(
                this.valueFormatFunction.apply(this.valueSupplier.get()), this.valueCapFormatFunction.apply(this.valueCapSupplier.get())
            ),
            new TextComponent(this.descriptionSupplier.get()).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(11184810)))
        );
    }

    @Redirect(method = "lambda$build$5", at = @At(value = "INVOKE", target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;"))
    private List<Component> splitCapped(Object e1, Object e2) {
        if(QOLHuntersClientConfigs.MULTILINE_STAT_LABELS.get()) {


            List<Component> compList = new ArrayList<>();
            compList.add(new TextComponent(this.labelSupplier.get()));
            String descComponent = this.descriptionSupplier.get();
            String[] descLines = descComponent.split("\n");
            for (String line : descLines) {
                compList.add(new TextComponent(line).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(11184810))));
            }
            return Collections.unmodifiableList(compList);
        }
        // Fallback to original behavior if the config is not enabled
        return List.of(
            new TextComponent(this.labelSupplier.get()),
            new TextComponent(this.descriptionSupplier.get()).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(11184810)))
        );
    }
}
