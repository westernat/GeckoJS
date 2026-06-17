package org.mesdag.geckojs.item.armor;

import net.minecraft.world.entity.Entity;
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
    public @Nullable GeoBone getHeadBone() {
        return getHeadBone(model);
    }

    @Override
    public @Nullable GeoBone getBodyBone() {
        return getBodyBone(model);
    }

    @Override
    public @Nullable GeoBone getRightArmBone() {
        return getRightArmBone(model);
    }

    @Override
    public @Nullable GeoBone getLeftArmBone() {
        return getLeftArmBone(model);
    }

    @Override
    public @Nullable GeoBone getRightLegBone() {
        return getRightLegBone(model);
    }

    @Override
    public @Nullable GeoBone getLeftLegBone() {
        return getLeftLegBone(model);
    }

    @Override
    public @Nullable GeoBone getRightBootBone() {
        return getRightBootBone(model);
    }

    @Override
    public @Nullable GeoBone getLeftBootBone() {
        return getLeftBootBone(model);
    }

    @Override
    public void setBoneVisible(@Nullable GeoBone bone, boolean visible) {
        super.setBoneVisible(bone, visible);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
}
