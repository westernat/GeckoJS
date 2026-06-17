package org.mesdag.geckojs.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.block.entity.BlockEntityBuilder;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.mesdag.geckojs.ExtendedGeoModel;
import org.mesdag.geckojs.GeckoJS;
import org.mesdag.geckojs.block.entity.AnimatableBlockEntity;
import org.mesdag.geckojs.block.entity.AnimatableBlockEntityInfo;

import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class AnimatableBlockBuilder extends BlockBuilder {
    public final transient AnimatableBlockEntityInfo blockEntityInfo = new AnimatableBlockEntityInfo(this);
    private final ExtendedGeoModel<AnimatableBlockEntity> blockModel = new ExtendedGeoModel<>();
    private transient AnimatableBlockItemBuilder itemBuilder;

    public AnimatableBlockBuilder(ResourceLocation id) {
        super(id);
        this.itemBuilder = new AnimatableBlockItemBuilder(id, this);
        this.opaque = false;
    }

    @Info("Creates a animatable Block Entity for this block")
    public AnimatableBlockBuilder animatableBlockEntity(Consumer<AnimatableBlockEntityInfo> consumer) {
        consumer.accept(blockEntityInfo);
        return this;
    }

    public AnimatableBlockBuilder geoModel(Consumer<ExtendedGeoModel.Builder<AnimatableBlockEntity>> consumer) {
        consumer.accept(blockModel.builder);
        return this;
    }

    public AnimatableBlockBuilder defaultGeoModel() {
        blockModel.builder.setSimpleModel(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "geo/block/" + id.getPath() + ".geo.json"));
        blockModel.builder.setSimpleTexture(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/block/" + id.getPath() + ".png"));
        blockModel.builder.setSimpleAnimation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/block/" + id.getPath() + ".animation.json"));
        return this;
    }

    public AnimatableBlockBuilder animatableItem(Consumer<AnimatableBlockItemBuilder> consumer) {
        consumer.accept(itemBuilder);
        return this;
    }

    @Override
    public BlockBuilder noItem() {
        this.itemBuilder = null;
        return super.noItem();
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        if (itemBuilder != null) {
            registry.add(Registries.ITEM, itemBuilder.hasModel ? itemBuilder : itemBuilder.defaultGeoModel());
        }

        registry.add(Registries.BLOCK_ENTITY_TYPE, new BlockEntityBuilder(id, blockEntityInfo));
        GeckoJS.REGISTERED_BLOCK.put(id, blockModel);
    }

    @Override
    public Block createObject() {
        return new AnimatableBlock(this);
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        generator.blockState(id, this::generateBlockState);
        generator.blockModel(id, model -> {
            if (modelGenerator != null) {
                modelGenerator.accept(model);
                return;
            }
            model.parent(ResourceLocation.withDefaultNamespace("block/block"));
            model.texture("particle", id.getNamespace() + ":block/" + id.getPath());
        });
    }

    @HideFromJS
    @Override
    public BlockBuilder blockEntity(Consumer<BlockEntityInfo> callback) {
        return super.blockEntity(callback);
    }

    @HideFromJS
    @Override
    public BlockBuilder item(@Nullable Consumer<ItemBuilder> i) {
        return super.item(i);
    }

    @HideFromJS
    @Override
    public BlockBuilder renderType(BlockRenderType l) {
        return super.renderType(l);
    }

    @HideFromJS
    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return super.texture(tex);
    }

    @HideFromJS
    @Override
    public ModelledBuilderBase<Block> textures(Map<String, String> map) {
        return super.textures(map);
    }

    @HideFromJS
    @Override
    public ModelledBuilderBase<Block> texture(String[] key, String tex) {
        return super.texture(key, tex);
    }

    @HideFromJS
    @Override
    public ModelledBuilderBase<Block> parentModel(ResourceLocation id) {
        return super.parentModel(id);
    }

    @HideFromJS
    @Override
    public BlockBuilder transparent(boolean b) {
        return super.transparent(b);
    }

    @HideFromJS
    @Override
    public BlockBuilder defaultCutout() {
        return super.defaultCutout();
    }

    @HideFromJS
    @Override
    public BlockBuilder defaultTranslucent() {
        return super.defaultTranslucent();
    }
}
