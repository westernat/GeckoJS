package org.mesdag.geckojs.item.armor;

import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.mesdag.geckojs.ExtendedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class AnimatableArmorRenderer extends GeoArmorRenderer<AnimatableArmorItem> implements IAnimatableArmorRenderer {
    private final AnimatableArmorBuilder.BoneVisibilityCallback boneVisibilityCallback;

    public AnimatableArmorRenderer(ExtendedGeoModel<AnimatableArmorItem> model, AnimatableArmorBuilder.BoneVisibilityCallback boneVisibilityCallback) {
        super(model);
        this.scaleWidth = model.builder.scaleWidth;
        this.scaleHeight = model.builder.scaleHeight;
        this.boneVisibilityCallback = boneVisibilityCallback;
        if (model.builder.autoGlowing) {
            addRenderLayer(new AutoGlowingGeoLayer<>(this));
        }
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        if (boneVisibilityCallback == null) {
            super.applyBoneVisibilityBySlot(currentSlot);
        } else {
            boneVisibilityCallback.apply(this, currentSlot);
        }
    }

    @Override
    public void setBoneVisible(@Nullable GeoBone bone, boolean visible) {
        super.setBoneVisible(bone, visible);
    }
}
