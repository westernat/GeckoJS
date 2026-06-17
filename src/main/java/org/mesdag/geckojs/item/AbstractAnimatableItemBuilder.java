package org.mesdag.geckojs.item;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.mesdag.geckojs.AnimationControllerBuilder;
import org.mesdag.geckojs.ExtendedGeoModel;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class AbstractAnimatableItemBuilder<T extends Item & GeoItem> extends ItemBuilder {
    public final ExtendedGeoModel<T> itemModel = new ExtendedGeoModel<>();
    public UsingAnimationCallback usingAnimationCallback;
    public FinishUsingAnimationCallback finishUsingAnimationCallback;
    public ReleaseUsingAnimationCallback releaseUsingAnimationCallback;
    public final transient ArrayList<AnimationControllerBuilder<T>> controllers = new ArrayList<>();
    public final transient ArrayList<AnimationStateCallback> animations = new ArrayList<>();
    public transient boolean useEntityGuiLighting = false;

    public AbstractAnimatableItemBuilder(ResourceLocation id) {
        super(id);
    }

    public AbstractAnimatableItemBuilder<T> usingAnimation(UsingAnimationCallback callback) {
        this.usingAnimationCallback = callback;
        return this;
    }

    public AbstractAnimatableItemBuilder<T> finishUsingAnimation(FinishUsingAnimationCallback callback) {
        this.finishUsingAnimationCallback = callback;
        return this;
    }

    public AbstractAnimatableItemBuilder<T> releaseUsingAnimation(ReleaseUsingAnimationCallback callback) {
        this.releaseUsingAnimationCallback = callback;
        return this;
    }

    public AbstractAnimatableItemBuilder<T> addAnimation(AnimationStateCallback callBack) {
        animations.add(callBack);
        return this;
    }

    public AbstractAnimatableItemBuilder<T> addController(Consumer<AnimationControllerBuilder<T>> consumer) {
        AnimationControllerBuilder<T> builder = new AnimationControllerBuilder<>();
        consumer.accept(builder);
        controllers.add(builder);
        return this;
    }

    public AbstractAnimatableItemBuilder<T> geoModel(Consumer<ExtendedGeoModel.Builder<T>> consumer) {
        consumer.accept(itemModel.builder);
        return this;
    }

    public AbstractAnimatableItemBuilder<T> defaultGeoModel() {
        itemModel.builder.setSimpleModel(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "geo/item/" + id.getPath() + ".geo.json"));
        itemModel.builder.setSimpleTexture(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/item/" + id.getPath() + ".png"));
        itemModel.builder.setSimpleAnimation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/item/" + id.getPath() + ".animation.json"));
        return this;
    }

    public AbstractAnimatableItemBuilder<T> useEntityGuiLighting() {
        this.useEntityGuiLighting = true;
        return this;
    }

    @HideFromJS
    @Override
    public ItemBuilder texture(String key, String tex) {
        return super.texture(key, tex);
    }

    @HideFromJS
    @Override
    public ItemBuilder texture(String tex) {
        return super.texture(tex);
    }

    @HideFromJS
    @Override
    public ItemBuilder textureJson(JsonObject json) {
        return super.textureJson(json);
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (modelJson == null) {
            generator.itemModel(id, model -> model.parent(parentModel.isEmpty() ? "geckojs:item/item" : parentModel));
        } else {
            generator.json(AssetJsonGenerator.asItemModelLocation(id), modelJson);
        }
    }

    @FunctionalInterface
    public interface UsingAnimationCallback {
        void call(GeoItem self, ServerLevel serverLevel, ServerPlayer player, InteractionHand hand);
    }

    @FunctionalInterface
    public interface FinishUsingAnimationCallback {
        void call(GeoItem self, ServerLevel serverLevel, LivingEntity livingEntity);
    }

    @FunctionalInterface
    public interface ReleaseUsingAnimationCallback {
        void call(GeoItem self, ServerLevel serverLevel, LivingEntity livingEntity, int tick);
    }

    @FunctionalInterface
    public interface AnimationStateCallback {
        PlayState create(AnimationState<GeoItem> state);
    }
}
