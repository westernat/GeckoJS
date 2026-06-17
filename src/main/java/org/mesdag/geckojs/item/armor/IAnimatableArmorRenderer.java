package org.mesdag.geckojs.item.armor;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;

@SuppressWarnings("unused")
public interface IAnimatableArmorRenderer {
    void setAllVisible(boolean pVisible);

    @Nullable GeoBone getHeadBone();

    @Nullable GeoBone getBodyBone();

    @Nullable GeoBone getRightArmBone();

    @Nullable GeoBone getLeftArmBone();

    @Nullable GeoBone getRightLegBone();

    @Nullable GeoBone getLeftLegBone();

    @Nullable GeoBone getRightBootBone();

    @Nullable GeoBone getLeftBootBone();

    void setBoneVisible(@Nullable GeoBone bone, boolean visible);
}
