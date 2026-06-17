package org.mesdag.geckojs;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.mesdag.geckojs.block.entity.AnimatableBlockEntity;
import org.mesdag.geckojs.block.entity.AnimatableBlockEntityRenderer;

import static org.mesdag.geckojs.GeckoJS.REGISTERED_BLOCK;

@EventBusSubscriber(modid = GeckoJS.MOD_ID, value = Dist.CLIENT)
public class GeckoJSClient {
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> GeckoJS.REGISTERED_SHIELD.forEach(id -> ItemProperties.register(
                BuiltInRegistries.ITEM.get(id),
                ResourceLocation.withDefaultNamespace("blocking"),
                (stack, world, living, itemId) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F
        )));

        REGISTERED_BLOCK.forEach((id, model) -> {
            BlockEntityType<? extends AnimatableBlockEntity> type = (BlockEntityType<? extends AnimatableBlockEntity>) BuiltInRegistries.BLOCK_ENTITY_TYPE.get(id);
            assert type != null : "no entity type found for id: " + id;
            BlockEntityRenderers.register(type, context -> new AnimatableBlockEntityRenderer(model));
        });
        //REGISTERED_BLOCK.keySet().forEach(id -> ItemBlockRenderTypes.setRenderLayer(RegistryInfo.BLOCK.getValue(id), RenderType.translucent()));
    }
}
