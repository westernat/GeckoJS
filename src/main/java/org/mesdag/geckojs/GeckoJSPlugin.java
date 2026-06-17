package org.mesdag.geckojs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import org.mesdag.geckojs.block.AnimatableBlockBuilder;
import org.mesdag.geckojs.item.AnimatableItem;
import org.mesdag.geckojs.item.armor.AnimatableArmorBuilder;
import org.mesdag.geckojs.item.tool.*;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DataTickets;

public class GeckoJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, reg -> {
            reg.add(GeckoJS.asResource("animatable"), AnimatableBlockBuilder.class, AnimatableBlockBuilder::new);
        });
        registry.of(Registries.ITEM, reg -> {
            reg.add(GeckoJS.asResource("animatable"), AnimatableItem.Builder.class, AnimatableItem.Builder::new);
            reg.add(GeckoJS.asResource("anim_helmet"), AnimatableArmorBuilder.class, id -> new AnimatableArmorBuilder(id, ArmorItem.Type.HELMET));
            reg.add(GeckoJS.asResource("anim_chestplate"), AnimatableArmorBuilder.class, id -> new AnimatableArmorBuilder(id, ArmorItem.Type.CHESTPLATE));
            reg.add(GeckoJS.asResource("anim_leggings"), AnimatableArmorBuilder.class, id -> new AnimatableArmorBuilder(id, ArmorItem.Type.LEGGINGS));
            reg.add(GeckoJS.asResource("anim_boots"), AnimatableArmorBuilder.class, id -> new AnimatableArmorBuilder(id, ArmorItem.Type.BOOTS));
            reg.add(GeckoJS.asResource("anim_axe"), AnimatableAxeItem.Builder.class, AnimatableAxeItem.Builder::new);
            reg.add(GeckoJS.asResource("anim_hoe"), AnimatableHoeItem.Builder.class, AnimatableHoeItem.Builder::new);
            reg.add(GeckoJS.asResource("anim_pickaxe"), AnimatablePickaxeItem.Builder.class, AnimatablePickaxeItem.Builder::new);
            reg.add(GeckoJS.asResource("anim_sword"), AnimatableSwordItem.Builder.class, AnimatableSwordItem.Builder::new);
            reg.add(GeckoJS.asResource("anim_shield"), AnimatableShieldItem.Builder.class, AnimatableShieldItem.Builder::new);
        });
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("RawAnimation", RawAnimation.class);
        bindings.add("GeoItem", GeoItem.class);
        bindings.add("EasingType", EasingType.class);
        bindings.add("DataTickets", DataTickets.class);
        bindings.add("EquipmentSlot", EquipmentSlot.class);
    }
}
