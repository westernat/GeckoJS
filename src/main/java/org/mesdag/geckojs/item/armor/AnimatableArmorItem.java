package org.mesdag.geckojs.item.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.geckojs.item.AnimatableItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.function.Consumer;

public class AnimatableArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AnimatableArmorItem(Holder<ArmorMaterial> material, Type armorType, Properties itemProperties) {
        super(material, armorType, itemProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    private AnimatableArmorBuilder itemBuilder() {
        return (AnimatableArmorBuilder) Objects.requireNonNull(kjs$getItemBuilder());
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private AnimatableArmorRenderer armorRenderer;
            private AnimatableItemRenderer<AnimatableArmorItem> itemRenderer;

            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (itemBuilder().useGeoModel) {
                    if (itemRenderer == null) {
                        this.itemRenderer = new AnimatableItemRenderer<>(itemBuilder().armorModel);
                    }
                    return itemRenderer;
                }
                return GeoRenderProvider.super.getGeoItemRenderer();
            }

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (armorRenderer == null) {
                    this.armorRenderer = new AnimatableArmorRenderer(itemBuilder().armorModel, itemBuilder().boneVisibilityCallback);
                }
                return armorRenderer;
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
}
