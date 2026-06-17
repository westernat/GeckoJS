package org.mesdag.geckojs.item.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.mesdag.geckojs.item.AnimatableItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class AnimatableSwordItem extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
    private final Builder swordItemBuilder;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;

    public AnimatableSwordItem(Builder swordItemBuilder) {
        super(swordItemBuilder.toolTier, (int) swordItemBuilder.attackDamageBaseline, swordItemBuilder.speedBaseline, swordItemBuilder.createItemProperties());
        this.swordItemBuilder = swordItemBuilder;
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel && swordItemBuilder.usingAnimationCallback != null) {
            swordItemBuilder.usingAnimationCallback.call(this, serverLevel, (ServerPlayer) player, hand);
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (level instanceof ServerLevel serverLevel && swordItemBuilder.finishUsingAnimationCallback != null) {
            swordItemBuilder.finishUsingAnimationCallback.call(this, serverLevel, livingEntity);
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int tick) {
        if (level instanceof ServerLevel serverLevel && swordItemBuilder.releaseUsingAnimationCallback != null) {
            swordItemBuilder.releaseUsingAnimationCallback.call(this, serverLevel, livingEntity, tick);
        }
        super.releaseUsing(itemStack, level, livingEntity, tick);
    }

    private boolean modified = false;

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            if (!modified) {
                this.modified = true;
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
                swordItemBuilder.attributes.forEach((r, m) -> builder.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
                this.attributeModifiers = builder.build();
            }
            return attributeModifiers;
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private AnimatableItemRenderer<AnimatableSwordItem> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    AnimatableItemRenderer<AnimatableSwordItem> itemRenderer = new AnimatableItemRenderer<>(swordItemBuilder.itemModel);
                    if (swordItemBuilder.useEntityGuiLighting) itemRenderer.useAlternateGuiLighting();
                    this.renderer = itemRenderer;
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        swordItemBuilder.controllers.forEach(controller -> registrar.add(controller.build(this)));
        swordItemBuilder.animations.forEach(animation -> registrar.add(new AnimationController<>(this, animation::create)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }

    public static class Builder extends AnimatableTiredItemBuilder<AnimatableSwordItem> {
        public Builder(ResourceLocation id) {
            super(id, 3F, -2.4F);
        }

        @Override
        public AnimatableSwordItem createObject() {
            return new AnimatableSwordItem(this);
        }
    }
}
