package org.mesdag.geckojs;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.mesdag.geckojs.block.entity.AnimatableBlockEntityRenderer;

import static org.mesdag.geckojs.GeckoJS.REGISTERED_BLOCK;

@Mod.EventBusSubscriber(modid = GeckoJS.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GeckoJSClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> GeckoJS.REGISTERED_SHIELD.forEach(id -> ItemProperties.register(
            RegistryInfo.ITEM.getValue(id),
            new ResourceLocation("blocking"),
            (stack, world, living, itemId) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F
            )));

        REGISTERED_BLOCK.forEach((id, model) -> {
            BlockEntityRenderers.register(RegistryInfo.BLOCK_ENTITY_TYPE.getValue(id), context -> new AnimatableBlockEntityRenderer(model));
        });
        //REGISTERED_BLOCK.keySet().forEach(id -> ItemBlockRenderTypes.setRenderLayer(RegistryInfo.BLOCK.getValue(id), RenderType.translucent()));
    }
}
