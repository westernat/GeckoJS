package org.mesdag.geckojs;

import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

@OnlyIn(Dist.CLIENT)
public class RegisterItemClientExtensionsEvent extends Event implements IModBusEvent {
    private final Item item;
    private IClientItemExtensions extension;

    public RegisterItemClientExtensionsEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void register(IClientItemExtensions extension) {
        this.extension = extension;
    }

    @ApiStatus.Internal
    public IClientItemExtensions getExtension() {
        return extension;
    }
}
