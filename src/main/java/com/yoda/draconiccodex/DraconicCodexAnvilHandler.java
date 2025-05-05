package com.yoda.draconiccodex;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class DraconicCodexAnvilHandler {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new DraconicCodexAnvilHandler());
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        // Case 1: Use Draconic Codex on enchanted item to upgrade +1 ➜ +2
        if (!left.isEmpty() && !right.isEmpty() && right.getItem() instanceof DraconicCodexItem) {
            CompoundTag tag = right.getOrCreateTag().getCompound("StoredEnchant");

            if (tag.contains("id") && tag.contains("lvl")) {
                ResourceLocation enchantId = new ResourceLocation(tag.getString("id"));
                int targetLevel = tag.getInt("lvl");

                Enchantment enchantment = BuiltInRegistries.ENCHANTMENT.get(enchantId);
                int currentLevel = EnchantmentHelper.getEnchantments(left).getOrDefault(enchantment, 0);

                if (enchantment != null && currentLevel == targetLevel - 1) {
                    ItemStack output = left.copy();
                    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(output);
                    enchants.put(enchantment, targetLevel);
                    EnchantmentHelper.setEnchantments(enchants, output);

                    event.setOutput(output);
                    event.setCost(50);
                    return;
                }
            }
        }

        // Case 2: Load enchant from Ancient Tome ➜ blank Draconic Codex
        if (!left.isEmpty() && !right.isEmpty()
                && left.getItem() instanceof DraconicCodexItem
                && !left.getOrCreateTag().contains("StoredEnchant")) {

            ResourceLocation rightId = BuiltInRegistries.ITEM.getKey(right.getItem());
            if (rightId != null && rightId.toString().equals("quark:ancient_tome")) {

                CompoundTag tag = right.getTag();
                if (tag != null && tag.contains("StoredEnchantments")) {
                    var enchantList = EnchantmentHelper.deserializeEnchantments(tag.getList("StoredEnchantments", 10));

                    if (enchantList.size() == 1) {
                        Map.Entry<Enchantment, Integer> entry = enchantList.entrySet().iterator().next();
                        Enchantment enchant = entry.getKey();

                        ItemStack output = left.copy();
                        CompoundTag stored = new CompoundTag();
                        stored.putString("id", BuiltInRegistries.ENCHANTMENT.getKey(enchant).toString());
                        stored.putInt("lvl", enchant.getMaxLevel() + 2);
                        output.getOrCreateTag().put("StoredEnchant", stored);

                        event.setOutput(output);
                        event.setCost(30);
                    }
                }
            }
        }
    }
}
