package org.mesdag.geckojs.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.block.entity.BlockEntityBuilder;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.mesdag.geckojs.ExtendedGeoModel;
import org.mesdag.geckojs.GeckoJS;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class AnimatableBlockBuilder extends BlockBuilder {
    public final AnimatableBlockEntityInfo blockEntityInfo = new AnimatableBlockEntityInfo(this);
    private final ExtendedGeoModel<AnimatableBlockEntity> blockModel = new ExtendedGeoModel<>();
    private DefaultedBlockGeoModel<AnimatableBlockEntity> defaultedBlockGeoModel;

    public AnimatableBlockBuilder(ResourceLocation id) {
        super(id);
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
        this.defaultedBlockGeoModel = new DefaultedBlockGeoModel<>(id);
        return this;
    }

    @Override
    public void createAdditionalObjects() {
        if (itemBuilder != null) {
            RegistryInfo.ITEM.addBuilder(itemBuilder);
        }
        RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(new BlockEntityBuilder(id, blockEntityInfo));
        if (defaultedBlockGeoModel != null) {
            GeckoJS.DEFAULTED.put(id, defaultedBlockGeoModel);
        } else {
            GeckoJS.REGISTERED.put(id, blockModel);
        }
    }

    @Override
    public Block createObject() {
        return new AnimatableBlock(this);
    }

    @HideFromJS
    @Override
    public BlockBuilder blockEntity(Consumer<BlockEntityInfo> callback) {
        return super.blockEntity(callback);
    }

    @HideFromJS
    @Override
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        return super.item(i);
    }

    @HideFromJS
    @Override
    public BlockBuilder renderType(String l) {
        return super.renderType(l);
    }

    @HideFromJS
    @Override
    public BlockBuilder texture(String id, String tex) {
        return super.texture(id, tex);
    }

    @HideFromJS
    @Override
    public BlockBuilder textureAll(String tex) {
        return super.textureAll(tex);
    }

    @HideFromJS
    @Override
    public BlockBuilder textureSide(Direction direction, String tex) {
        return super.textureSide(direction, tex);
    }

    @HideFromJS
    @Override
    public BlockBuilder model(String m) {
        return super.model(m);
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

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
    }
}