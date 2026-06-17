package org.mesdag.geckojs.item.tool;

import dev.latvian.mods.kubejs.item.MutableToolTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import org.mesdag.geckojs.item.AbstractAnimatableItemBuilder;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class AnimatableTiredItemBuilder<T extends TieredItem & GeoItem> extends AbstractAnimatableItemBuilder<T> {
    public transient MutableToolTier toolTier;
    public transient float attackDamageBaseline;
    public transient float speedBaseline;

    public AnimatableTiredItemBuilder(ResourceLocation id, float d, float s) {
        super(id);
        this.toolTier = new MutableToolTier(Tiers.IRON);
        this.attackDamageBaseline = d;
        this.speedBaseline = s;
        unstackable();
    }

    public AnimatableTiredItemBuilder<T> tier(Tier t) {
        toolTier = new MutableToolTier(t);
        return this;
    }

    public AnimatableTiredItemBuilder<T> attackDamageBaseline(float f) {
        attackDamageBaseline = f;
        return this;
    }

    public AnimatableTiredItemBuilder<T> speedBaseline(float f) {
        speedBaseline = f;
        return this;
    }

    public AnimatableTiredItemBuilder<T> modifyTier(Consumer<MutableToolTier> callback) {
        callback.accept(toolTier);
        return this;
    }

    public AnimatableTiredItemBuilder<T> attackDamageBonus(float f) {
        toolTier.setAttackDamageBonus(f);
        return this;
    }

    public AnimatableTiredItemBuilder<T> speed(float f) {
        toolTier.setSpeed(f);
        return this;
    }
}
