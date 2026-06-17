package org.mesdag.geckojs;

import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

@SuppressWarnings("unused")
public class ExtendedGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
    static final ResourceLocation EMPTY = ResourceLocation.fromNamespaceAndPath("geckojs", "empty");
    public final Builder<T> builder = new Builder<>();

    @Override
    public ResourceLocation getModelResource(T animatable) {
        if (builder.simpleModel != null) return builder.simpleModel;
        if (builder.model == null) return EMPTY;
        return builder.model.create(animatable);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        if (builder.simpleTexture != null) return builder.simpleTexture;
        if (builder.texture == null) return EMPTY;
        return builder.texture.create(animatable);
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        if (builder.simpleAnimation != null) return builder.simpleAnimation;
        if (builder.animation == null) return EMPTY;
        return builder.animation.create(animatable);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    public static class Builder<T extends GeoAnimatable> {
        ResourceCallback<T> model;
        ResourceCallback<T> texture;
        ResourceCallback<T> animation;
        ResourceLocation simpleModel;
        ResourceLocation simpleTexture;
        ResourceLocation simpleAnimation;
        public float scaleWidth = 1.0F;
        public float scaleHeight = 1.0F;
        public boolean autoGlowing = false;

        @Info("This method should return the ResourceLocation path to your .geo.json model file for this animatable")
        public void setModel(ResourceCallback<T> model) {
            this.model = model;
        }

        @Info("The ResourceLocation path to your .geo.json model file for this animatable")
        public void setSimpleModel(ResourceLocation model) {
            this.simpleModel = model;
        }

        @Info("This method should return the ResourceLocation path to your .png texture file for this animatable")
        public void setTexture(ResourceCallback<T> texture) {
            this.texture = texture;
        }

        @Info("The ResourceLocation path to your .png texture file for this animatable")
        public void setSimpleTexture(ResourceLocation texture) {
            this.simpleTexture = texture;
        }

        @Info("This method should return the ResourceLocation path to your .animation.json animation file for this animatable")
        public void setAnimation(ResourceCallback<T> animation) {
            this.animation = animation;
        }

        @Info("The ResourceLocation path to your .animation.json animation file for this animatable")
        public void setSimpleAnimation(ResourceLocation animation) {
            this.simpleAnimation = animation;
        }

        public void setScale(float scale) {
            this.scaleWidth = scale;
            this.scaleHeight = scale;
        }
    }

    @FunctionalInterface
    public interface ResourceCallback<T extends GeoAnimatable> {
        ResourceLocation create(T animatable);
    }
}
