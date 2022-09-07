package com.jeff_media.worldheal.listener;

import com.jeff_media.jefflib.BlockFaceUtils;
import com.jeff_media.worldheal.WorldHealPlugin;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsFixListener implements Listener {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();

//    @EventHandler
//    public void onPhysics(BlockPhysicsEvent event) {
//        Block block = event.getSourceBlock();
//        System.out.println("BlockPhysicsEvent: " + block);
//
//        if (plugin.getExplosionManager().isAlreadyRegistered(block)) {
//            plugin.debug("Cancelling physics event for " + block);
//            event.setCancelled(true);
//            return;
//        }
//
//        if(block.getBlockData() instanceof Bisected) {
//            Block otherHalf = BlockFaceUtils.getOppositeOfBisected(block);
//            if(plugin.getExplosionManager().isAlreadyRegistered(otherHalf)) {
//                plugin.debug("Cancelling physics event for other half of " + block + ", which is " + otherHalf);
//                event.setCancelled(true);
//                return;
//            }
//        }
//    }
}
