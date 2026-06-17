package org.mesdag.geckojs.block;

import dev.latvian.mods.kubejs.block.custom.BasicKubeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatableBlock extends BasicKubeBlock.WithEntity {
    private final AnimatableBlockBuilder blockBuilder;

    public AnimatableBlock(AnimatableBlockBuilder builder) {
        super(builder);
        this.blockBuilder = builder;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return blockBuilder.blockEntityInfo.createBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockBuilder.blockEntityInfo.getTicker(level);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
