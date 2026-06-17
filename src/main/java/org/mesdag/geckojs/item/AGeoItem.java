package org.mesdag.geckojs.item;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.Objects;

public interface AGeoItem<T extends AbstractAnimatableItemBuilder<?>> extends GeoItem {
    @SuppressWarnings("unchecked")
    @HideFromJS
    default T itemBuilder() {
        return (T) Objects.requireNonNull(((Item) this).kjs$getItemBuilder());
    }
}
