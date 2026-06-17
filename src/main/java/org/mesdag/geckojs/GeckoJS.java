package org.mesdag.geckojs;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;
import org.mesdag.geckojs.block.entity.AnimatableBlockEntity;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Mod(GeckoJS.MOD_ID)
public class GeckoJS {
    public static final String MOD_ID = "geckojs";
    public static final Map<ResourceLocation, ExtendedGeoModel<AnimatableBlockEntity>> REGISTERED_BLOCK = new ConcurrentHashMap<>();
    public static final Set<ResourceLocation> REGISTERED_SHIELD = ConcurrentHashMap.newKeySet();

    public GeckoJS() {}

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
