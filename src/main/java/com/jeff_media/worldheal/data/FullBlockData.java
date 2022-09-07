package com.jeff_media.worldheal.data;

import com.jeff_media.worldheal.WorldHealPlugin;
import com.jeff_media.worldheal.util.ItemStackNotEmptyPredicate;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FullBlockData implements ConfigurationSerializable {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();
    @Getter
    private final SimpleLoc loc;
    private final BlockData data;
    private final ItemStack[] inventoryItems;

    public FullBlockData(Block block) {
        this.loc = new SimpleLoc(block);
        this.data = block.getBlockData();
        this.inventoryItems = getOrDropInventoryItems(block);
    }

    @Nullable
    private ItemStack[] getOrDropInventoryItems(Block block) {
        ItemStack[] items = getInventoryItems(block);
        if(plugin.getConfig().isRestoreInventories()) {
            return items;
        } else {
            if(items != null) {
                drop(items);
            }
            return null;
        }

    }

    @Nullable
    private ItemStack[] getInventoryItems(Block block) {
        if (!(block.getState() instanceof InventoryHolder)) {
            return null;
        }
        InventoryHolder holder = (InventoryHolder) block.getState();
        ItemStack[] cloned = new ItemStack[holder.getInventory().getSize()];
        ItemStack[] contents = holder.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null) {
                cloned[i] = item.clone();
            }
        }
        holder.getInventory().setContents(new ItemStack[holder.getInventory().getSize()]);
        return cloned;
    }



    public FullBlockData(Map<String, Object> map) {
        this.data = Bukkit.createBlockData((String) map.get("data"));
        this.loc = new SimpleLoc((Map<String, Object>) map.get("loc"));
        this.inventoryItems = (ItemStack[]) map.get("inventoryItems");
    }

    public static FullBlockData deserialize(Map<String, Object> map) {
        return new FullBlockData(map);
    }

    private void drop(ItemStack[] items) {
        Block block = loc.asBlock();
        if (block != null) {
            for (ItemStack item : items) {
                if (item != null) {
                    block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 1, 0.5), item);
                }
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("loc", loc);
        map.put("data", data.getAsString(true));
        map.put("inventoryItems", inventoryItems);
        return map;
    }

    public void restore() {
        Block block = loc.asBlock();
        if (block == null) return;
        block.setBlockData(data, false);
        if (inventoryItems != null) {
            //System.out.println("Restoring inventory items: " + Arrays.toString(inventoryItems));
            InventoryHolder holder = (InventoryHolder) block.getState();
            holder.getInventory().setContents(inventoryItems);
        }
    }
}
