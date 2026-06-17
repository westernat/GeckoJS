package org.mesdag.geckojs;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Hashtable;
import java.util.function.Function;

@SuppressWarnings("unused")
public class AnimationControllerBuilder<T extends GeoAnimatable> {
    private transient String name = "base_controller";
    private transient int transitionTickTime = 0;
    private transient AnimationController.AnimationStateHandler<T> animationStateHandler = state -> PlayState.STOP;
    private transient AnimationController.SoundKeyframeHandler<T> soundKeyframeHandler;
    private transient AnimationController.ParticleKeyframeHandler<T> particleKeyframeHandler;
    private transient AnimationController.CustomKeyframeHandler<T> customKeyframeHandler;
    private transient Function<T, Double> speedModFunction;
    private transient Function<T, EasingType> overrideEasingTypeFunction;
    private final transient Hashtable<String, RawAnimation> animations = new Hashtable<>();

    public AnimationControllerBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public AnimationControllerBuilder<T> transitionTickTime(int tick) {
        this.transitionTickTime = tick;
        return this;
    }

    public AnimationControllerBuilder<T> animationState(AnimationController.AnimationStateHandler<T> handler) {
        this.animationStateHandler = handler;
        return this;
    }

    @Info("Applies the given SoundKeyframeHandler to this controller, for handling sound keyframe instructions.")
    public AnimationControllerBuilder<T> soundKeyframe(AnimationController.SoundKeyframeHandler<T> handler) {
        this.soundKeyframeHandler = handler;
        return this;
    }

    @Info("Applies the given ParticleKeyframeHandler to this controller, for handling particle keyframe instructions.")
    public AnimationControllerBuilder<T> particleKeyframe(AnimationController.ParticleKeyframeHandler<T> handler) {
        this.particleKeyframeHandler = handler;
        return this;
    }

    @Info("Applies the given CustomKeyframeHandler to this controller, for handling sound keyframe instructions.")
    public AnimationControllerBuilder<T> customKeyframe(AnimationController.CustomKeyframeHandler<T> handler) {
        this.customKeyframeHandler = handler;
        return this;
    }

    @Info("""
            Applies the given modifier function to this controller, for handling the speed that the controller should play its animations at.
            
            An output value of 1 is considered neutral, with 2 playing an animation twice as fast, 0.5 playing half as fast, etc.
            
            @param speedModFunction The function to apply to this controller to handle animation speed
            """)
    public AnimationControllerBuilder<T> animationSpeedHandler(Function<T, Double> function) {
        this.speedModFunction = function;
        return this;
    }

    @Info("""
            Applies the given modifier value to this controller, for handling the speed that the controller should play its animations at.
            
            An output value of 1 is considered neutral, with 2 playing an animation twice as fast, 0.5 playing half as fast, etc.
            
            @param speed The speed modifier to apply to this controller to handle animation speed.
            """)
    public AnimationControllerBuilder<T> animationSpeed(double speed) {
        return animationSpeedHandler(animatable -> speed);
    }

    @Info("""
            Sets the controller's EasingType override function for animations.
            
            By default, the controller will use whatever EasingType was defined in the animation json
            
            @param easingType The new EasingType to use
            """)
    public AnimationControllerBuilder<T> overrideEasingTypeFunction(Function<T, EasingType> function) {
        this.overrideEasingTypeFunction = function;
        return this;
    }

    @Info("""
            Sets the controller's EasingType override for animations.
            
            By default, the controller will use whatever EasingType was defined in the animation json
            
            @param easingTypeFunction The new EasingType to use
            """)
    public AnimationControllerBuilder<T> overrideEasingType(EasingType easingType) {
        return overrideEasingTypeFunction(animatable -> easingType);
    }

    @Info("""
            Registers a triggerable RawAnimation with the controller.
            
            These can then be triggered by the various triggerAnim methods in GeoAnimatable's subclasses
            
            @param name The name of the triggerable animation
            
            @param animation The RawAnimation for this triggerable animation
            """)
    public AnimationControllerBuilder<T> triggerableAnim(String animName, RawAnimation animation) {
        animations.put(animName, animation);
        return this;
    }

    @HideFromJS
    public AnimationController<T> build(T animatable) {
        AnimationController<T> controller = new AnimationController<>(animatable, name, transitionTickTime, animationStateHandler);
        if (soundKeyframeHandler != null) controller.setSoundKeyframeHandler(soundKeyframeHandler);
        if (particleKeyframeHandler != null) controller.setParticleKeyframeHandler(particleKeyframeHandler);
        if (customKeyframeHandler != null) controller.setCustomInstructionKeyframeHandler(customKeyframeHandler);
        if (speedModFunction != null) controller.setAnimationSpeedHandler(speedModFunction);
        if (overrideEasingTypeFunction != null) controller.setOverrideEasingTypeFunction(overrideEasingTypeFunction);
        animations.forEach(controller::triggerableAnim);
        return controller;
    }
}
