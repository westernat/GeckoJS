package org.mesdag.geckojs.block.entity;

import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.mesdag.geckojs.block.AnimatableBlockBuilder;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class AnimatableBlockEntityInfo extends BlockEntityInfo {
    public final transient ArrayList<AnimationStateCallBack> animations = new ArrayList<>();

    public AnimatableBlockEntityInfo(AnimatableBlockBuilder blockBuilder) {
        super(blockBuilder);
    }

    public AnimatableBlockEntityInfo addAnimation(AnimationStateCallBack callBack) {
        animations.add(callBack);
        return this;
    }

    @Override
    public AnimatableBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AnimatableBlockEntity(pos, state, this);
    }

    @FunctionalInterface
    public interface AnimationStateCallBack {
        PlayState create(AnimationState<AnimatableBlockEntity> state);
    }
}
