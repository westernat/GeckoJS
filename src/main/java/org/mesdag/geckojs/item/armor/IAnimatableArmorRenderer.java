package org.mesdag.geckojs.item.armor;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;

@SuppressWarnings("unused")
public interface IAnimatableArmorRenderer {
    void setAllVisible(boolean pVisible);

    GeoBone getHeadBone();

    GeoBone getBodyBone();

    GeoBone getRightArmBone();

    GeoBone getLeftArmBone();

    GeoBone getRightLegBone();

    GeoBone getLeftLegBone();

    GeoBone getRightBootBone();

    GeoBone getLeftBootBone();

    void setBoneVisible(@Nullable GeoBone bone, boolean visible);
}
