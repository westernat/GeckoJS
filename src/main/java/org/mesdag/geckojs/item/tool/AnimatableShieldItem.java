package org.mesdag.geckojs.item.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
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
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.mesdag.geckojs.GeckoJS;
import org.mesdag.geckojs.item.AbstractAnimatableItemBuilder;
import org.mesdag.geckojs.item.AnimatableItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static dev.latvian.mods.rhino.mod.util.JsonUtils.GSON;

public class AnimatableShieldItem extends ShieldItem implements GeoItem {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
    private final Builder shieldItemBuilder;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;

    public AnimatableShieldItem(Builder shieldItemBuilder) {
        super(shieldItemBuilder.createItemProperties());
        this.shieldItemBuilder = shieldItemBuilder;
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public int getUseDuration(ItemStack shield) {
        return shieldItemBuilder.useDuration == null ? super.getUseDuration(shield) : shieldItemBuilder.useDuration.applyAsInt(shield);
    }

    @Override
    public boolean isValidRepairItem(ItemStack shield, ItemStack repairItem) {
        return shieldItemBuilder.validRepairItemCallback == null ? super.isValidRepairItem(shield, repairItem) : shieldItemBuilder.validRepairItemCallback.is(shield, repairItem);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel && shieldItemBuilder.usingAnimationCallback != null) {
            shieldItemBuilder.usingAnimationCallback.call(this, serverLevel, (ServerPlayer) player, hand);
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (level instanceof ServerLevel serverLevel && shieldItemBuilder.finishUsingAnimationCallback != null) {
            shieldItemBuilder.finishUsingAnimationCallback.call(this, serverLevel, livingEntity);
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int tick) {
        if (level instanceof ServerLevel serverLevel && shieldItemBuilder.releaseUsingAnimationCallback != null) {
            shieldItemBuilder.releaseUsingAnimationCallback.call(this, serverLevel, livingEntity, tick);
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
                shieldItemBuilder.attributes.forEach((r, m) -> builder.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
                this.attributeModifiers = builder.build();
            }
            return attributeModifiers;
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private AnimatableItemRenderer<AnimatableShieldItem> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    AnimatableItemRenderer<AnimatableShieldItem> itemRenderer = new AnimatableItemRenderer<>(shieldItemBuilder.itemModel);
                    if (shieldItemBuilder.useEntityGuiLighting) itemRenderer.useAlternateGuiLighting();
                    this.renderer = itemRenderer;
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        shieldItemBuilder.controllers.forEach(controller -> registrar.add(controller.build(this)));
        shieldItemBuilder.animations.forEach(animation -> registrar.add(new AnimationController<>(this, animation::create)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }

    @SuppressWarnings("unused")
    public static class Builder extends AbstractAnimatableItemBuilder<AnimatableShieldItem> {
        private static final JsonObject shieldModel = GSON.fromJson("{\"parent\":\"geckojs:item/shield\",\"overrides\":[{\"predicate\":{\"blocking\":1},\"model\":\"geckojs:item/shield_blocking\"}]}", JsonObject.class);
        public ValidRepairItemCallback validRepairItemCallback;

        public Builder(ResourceLocation id) {
            super(id);
        }

        public Builder validRepairItem(ValidRepairItemCallback callback) {
            this.validRepairItemCallback = callback;
            return this;
        }

        @Override
        public void generateAssetJsons(AssetJsonGenerator generator) {
            ResourceLocation path = AssetJsonGenerator.asItemModelLocation(id);
            if (modelJson == null) {
                generator.json(path, shieldModel);
            } else {
                generator.json(path, modelJson);
            }
        }

        @Override
        public AnimatableShieldItem createObject() {
            GeckoJS.REGISTERED_SHIELD.add(id);
            return new AnimatableShieldItem(this);
        }

        @HideFromJS
        @Override
        public ItemBuilder parentModel(String m) {
            return super.parentModel(m);
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
