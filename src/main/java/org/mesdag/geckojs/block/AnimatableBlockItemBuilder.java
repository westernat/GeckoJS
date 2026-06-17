package org.mesdag.geckojs.block;

import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.mesdag.geckojs.ExtendedGeoModel;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class AnimatableBlockItemBuilder extends ItemBuilder {
    private final AnimatableBlockBuilder blockBuilder;
    public final ExtendedGeoModel<AnimatableBlockItem> itemModel = new ExtendedGeoModel<>();
    public boolean hasModel = false;
    public transient boolean useEntityGuiLighting = false;

    public AnimatableBlockItemBuilder(ResourceLocation id, AnimatableBlockBuilder blockBuilder) {
        super(id);
        this.blockBuilder = blockBuilder;
    }

    public AnimatableBlockItemBuilder geoModel(Consumer<ExtendedGeoModel.Builder<AnimatableBlockItem>> consumer) {
        consumer.accept(itemModel.builder);
        this.hasModel = true;
        return this;
    }

    public AnimatableBlockItemBuilder defaultGeoModel() {
        itemModel.builder.setSimpleModel(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "geo/block/" + id.getPath() + ".geo.json"));
        itemModel.builder.setSimpleTexture(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/block/" + id.getPath() + ".png"));
        itemModel.builder.setSimpleAnimation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/block/" + id.getPath() + ".animation.json"));
        this.hasModel = true;
        return this;
    }

    public AnimatableBlockItemBuilder useEntityGuiLighting() {
        this.useEntityGuiLighting = true;
        return this;
    }

    @Override
    public Item createObject() {
        return new AnimatableBlockItem(blockBuilder.get(), createItemProperties(), this);
    }

    @Override
    public String getTranslationKeyGroup() {
        return "block";
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (modelJson == null) {
            generator.itemModel(id, model -> model.parent(parentModel.isEmpty() ? "geckojs:item/item" : parentModel));
        } else {
            generator.json(AssetJsonGenerator.asItemModelLocation(id), modelJson);
        }
    }
}
