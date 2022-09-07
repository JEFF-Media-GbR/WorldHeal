package com.jeff_media.worldheal.listener;

import com.jeff_media.worldheal.WorldHealPlugin;
import com.jeff_media.worldheal.manager.ExplosionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlacePreventListener implements Listener {
    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();
    private static final ExplosionManager manager = plugin.getExplosionManager();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent event) {
        if (manager.isAlreadyRegistered(event.getBlock())) {
            event.setCancelled(true);
        }
    }
}

