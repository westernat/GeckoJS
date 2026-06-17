package org.mesdag.geckojs.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.mesdag.geckojs.item.AnimatableItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class AnimatableArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
    private final AnimatableArmorBuilder armorBuilder;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;

    public AnimatableArmorItem(AnimatableArmorBuilder armorBuilder) {
        super(armorBuilder.armorTier, armorBuilder.armorType, armorBuilder.createItemProperties());
        this.armorBuilder = armorBuilder;
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private AnimatableArmorRenderer armorRenderer;
            private AnimatableItemRenderer<AnimatableArmorItem> itemRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (armorRenderer == null) {
                    this.armorRenderer = new AnimatableArmorRenderer(armorBuilder.armorModel, armorBuilder.boneVisibilityCallback);
                }
                armorRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return armorRenderer;
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (armorBuilder.useGeoModel) {
                    if (itemRenderer == null) {
                        this.itemRenderer = new AnimatableItemRenderer<>(armorBuilder.armorModel);
                    }
                    return itemRenderer;
                }
                return IClientItemExtensions.super.getCustomRenderer();
            }
        });
    }

    private boolean modified = false;

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        if (equipmentSlot == type.getSlot()) {
            if (!modified) {
                this.modified = true;
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
                armorBuilder.attributes.forEach((r, m) -> builder.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
                this.attributeModifiers = builder.build();
            }
            return attributeModifiers;
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        armorBuilder.controllers.forEach(controller -> registrar.add(controller.build(this)));
        armorBuilder.animations.forEach(animation -> registrar.add(new AnimationController<>(this, animation::create)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }
}
