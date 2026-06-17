package org.mesdag.geckojs.block.entity;

import org.mesdag.geckojs.ExtendedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AnimatableBlockEntityRenderer extends GeoBlockRenderer<AnimatableBlockEntity> {
    public AnimatableBlockEntityRenderer(ExtendedGeoModel<AnimatableBlockEntity> model) {
        super(model);
        this.scaleWidth = model.builder.scaleWidth;
        this.scaleHeight = model.builder.scaleHeight;
    }
}
