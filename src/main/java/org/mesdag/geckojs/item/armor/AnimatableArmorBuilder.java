package org.mesdag.geckojs.item.armor;

import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.custom.ArmorItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import org.mesdag.geckojs.AnimationControllerBuilder;
import org.mesdag.geckojs.ExtendedGeoModel;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class AnimatableArmorBuilder extends ArmorItemBuilder {
    public final ExtendedGeoModel<AnimatableArmorItem> armorModel = new ExtendedGeoModel<>();
    public final transient ArrayList<AnimationControllerBuilder<AnimatableArmorItem>> controllers = new ArrayList<>();
    public final transient ArrayList<AnimationStateCallback> animations = new ArrayList<>();
    public boolean useGeoModel = false;
    public BoneVisibilityCallback boneVisibilityCallback;

    public AnimatableArmorBuilder(ResourceLocation id, ArmorItem.Type type) {
        super(id, type);
    }

    public AnimatableArmorBuilder addAnimation(AnimationStateCallback callBack) {
        animations.add(callBack);
        return this;
    }

    public AnimatableArmorBuilder addController(Consumer<AnimationControllerBuilder<AnimatableArmorItem>> consumer) {
        AnimationControllerBuilder<AnimatableArmorItem> builder = new AnimationControllerBuilder<>();
        consumer.accept(builder);
        controllers.add(builder);
        return this;
    }

    public AnimatableArmorBuilder geoModel(Consumer<ExtendedGeoModel.Builder<AnimatableArmorItem>> consumer) {
        consumer.accept(armorModel.builder);
        return this;
    }

    public AnimatableArmorBuilder armorItemUseGeoModel() {
        this.useGeoModel = true;
        return this;
    }

    public AnimatableArmorBuilder defaultGeoModel() {
        armorModel.builder.setSimpleModel(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "geo/armor/" + id.getPath() + ".geo.json"));
        armorModel.builder.setSimpleTexture(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/armor/" + id.getPath() + ".png"));
        armorModel.builder.setSimpleAnimation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/armor/" + id.getPath() + ".animation.json"));
        return this;
    }

    public AnimatableArmorBuilder boneVisibility(BoneVisibilityCallback callback) {
        this.boneVisibilityCallback = callback;
        return this;
    }

    @Override
    public Item createObject() {
        return new AnimatableArmorItem(this);
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (useGeoModel) {
            String parent;
            if (parentModel.isEmpty()) {
                switch (armorType) {
                    case HELMET -> parent = "geckojs:item/helmet";
                    case CHESTPLATE -> parent = "geckojs:item/chestplate";
                    case LEGGINGS -> parent = "geckojs:item/leggings";
                    case BOOTS -> parent = "geckojs:item/boots";
                    default -> parent = "geckojs:item/item";
                }
            } else {
                parent = parentModel;
            }
            generator.itemModel(id, model -> model.parent(parent));
        } else {
            super.generateAssetJsons(generator);
        }
    }

    @FunctionalInterface
    public interface AnimationStateCallback {
        PlayState create(AnimationState<AnimatableArmorItem> state);
    }

    @FunctionalInterface
    public interface BoneVisibilityCallback {
        void apply(IAnimatableArmorRenderer renderer, EquipmentSlot slot);
    }
}
