package org.mesdag.geckojs.item;

import net.minecraft.world.item.Item;
import org.mesdag.geckojs.ExtendedGeoModel;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class AnimatableItemRenderer<T extends Item & GeoItem> extends GeoItemRenderer<T> {
    public AnimatableItemRenderer(ExtendedGeoModel<T> model) {
        super(model);
        this.scaleWidth = model.builder.scaleWidth;
        this.scaleHeight = model.builder.scaleHeight;

        if (model.builder.autoGlowing) {
            addRenderLayer(new AutoGlowingGeoLayer<>(this));
        }
    }
}
