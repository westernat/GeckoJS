package org.mesdag.geckojs.item.tool;

import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.mesdag.geckojs.GeckoJS;
import org.mesdag.geckojs.item.AGeoItem;
import org.mesdag.geckojs.item.AbstractAnimatableItemBuilder;
import org.mesdag.geckojs.item.AnimatableItemRenderer;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class AnimatableShieldItem extends ShieldItem implements AGeoItem<AnimatableShieldItem.Builder> {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AnimatableShieldItem(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return itemBuilder().useDuration == null ? super.getUseDuration(stack, entity) : itemBuilder().useDuration.applyAsInt(stack, entity);
    }

    @Override
    public boolean isValidRepairItem(ItemStack shield, ItemStack repairItem) {
        return itemBuilder().validRepairItemCallback == null ? super.isValidRepairItem(shield, repairItem) : itemBuilder().validRepairItemCallback.is(shield, repairItem);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel && itemBuilder().usingAnimationCallback != null) {
            itemBuilder().usingAnimationCallback.call(this, serverLevel, (ServerPlayer) player, hand);
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (level instanceof ServerLevel serverLevel && itemBuilder().finishUsingAnimationCallback != null) {
            itemBuilder().finishUsingAnimationCallback.call(this, serverLevel, livingEntity);
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int tick) {
        if (level instanceof ServerLevel serverLevel && itemBuilder().releaseUsingAnimationCallback != null) {
            itemBuilder().releaseUsingAnimationCallback.call(this, serverLevel, livingEntity, tick);
        }
        super.releaseUsing(itemStack, level, livingEntity, tick);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private AnimatableItemRenderer<AnimatableShieldItem> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (renderer == null) {
                    AnimatableItemRenderer<AnimatableShieldItem> itemRenderer = new AnimatableItemRenderer<>(itemBuilder().itemModel);
                    if (itemBuilder().useEntityGuiLighting) itemRenderer.useAlternateGuiLighting();
                    this.renderer = itemRenderer;
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        itemBuilder().controllers.forEach(controller -> registrar.add(controller.build(this)));
        itemBuilder().animations.forEach(animation -> registrar.add(new AnimationController<>(this, animation::create)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @SuppressWarnings("unused")
    public static class Builder extends AbstractAnimatableItemBuilder<AnimatableShieldItem> {
        public ValidRepairItemCallback validRepairItemCallback;

        public Builder(ResourceLocation id) {
            super(id);
        }

        public Builder validRepairItem(ValidRepairItemCallback callback) {
            this.validRepairItemCallback = callback;
            return this;
        }

        @Override
        public void generateItemModels(KubeAssetGenerator generator) {
            generator.itemModel(id, model -> {
                if (modelGenerator != null) {
                    modelGenerator.accept(model);
                    return;
                }
                model.parent(parentModel == null
                        ? GeckoJS.asResource("item/shield_template")
                        : parentModel);
            });
        }

        @Override
        public AnimatableShieldItem createObject() {
            GeckoJS.REGISTERED_SHIELD.add(id);
            return new AnimatableShieldItem(createItemProperties());
        }

        @HideFromJS
        @Override
        public ModelledBuilderBase<Item> parentModel(ResourceLocation id) {
            return super.parentModel(id);
        }

        @HideFromJS
        @Override
        public ItemBuilder useAnimation(UseAnim animation) {
            return super.useAnimation(animation);
        }
    }

    @FunctionalInterface
    public interface ValidRepairItemCallback {
        boolean is(ItemStack shield, ItemStack repairItem);
    }
}
