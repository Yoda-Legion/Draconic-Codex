package com.yoda.draconiccodex;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DraconicCodexClient {
    public static void init() {
        ItemProperties.register(ModItems.DRACONIC_CODEX.get(), new ResourceLocation("filled"),
            (ItemStack stack, ClientLevel level, LivingEntity entity, int seed) ->
                stack.getOrCreateTag().contains("StoredEnchant") ? 1.0F : 0.0F
        );
    }
}
