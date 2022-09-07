package com.jeff_media.worldheal.listener;

import com.jeff_media.jefflib.JeffLib;
import com.jeff_media.jefflib.data.SerializedEntity;
import com.jeff_media.worldheal.WorldHealPlugin;
import com.jeff_media.worldheal.data.ExplosionData;
import com.jeff_media.worldheal.data.SimpleLoc;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExplosionListener implements Listener {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExplode(EntityExplodeEvent event) {
        handleExplodeEvent(event.blockList(), event.getLocation());
        event.setYield(0.0F);
        explodeManually(event.blockList());
    }

    private static void handleExplodeEvent(List<Block> blockList, Location explosionLocation) {
        System.out.println("Handling Explode Event");
        List<Block> list = new ArrayList<>(blockList);
        plugin.getConfig().getRestoreOrder().apply(list, explosionLocation);
        plugin.getExplosionManager().register(list, new SimpleLoc(explosionLocation));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExplode(BlockExplodeEvent event) {
        handleExplodeEvent(event.blockList(), event.getBlock().getLocation());
        event.setYield(0.0F);
        explodeManually(event.blockList());
    }

    private void explodeManually(List<Block> blockList) {
        for (Block block : blockList) {
            block.setType(Material.AIR, false);
        }
        blockList.clear();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExplode(EntityDamageByEntityEvent event) {
        if(!isGoingToDie(event)) {
            System.out.println("not going to die");
            return;
        }
        if(!isRestorableEntity(event.getEntityType())) {
            System.out.println("no proper entity type");
            return;
        }
        if(!isValidDamageCause(event.getCause())) {
            System.out.println("no valid cause");
            return;
        }
        SimpleLoc deathLoc = new SimpleLoc(event.getEntity().getLocation());
        /*ExplosionData data = plugin.getExplosionManager().getExplosions()
                .stream()
                .filter(d -> d.getLocation().getWorld().equals(event.getEntity().getWorld().getUID()))
                .min(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(deathLoc)))
                .orElse(null);*/
        ExplosionData data = plugin.getExplosionManager().getEntityExplosions().computeIfAbsent(event.getDamager().getLocation(), __ -> new ExplosionData(null, , ));
        if(data == null || data.getLocation().distanceSquared(deathLoc) > 400) {
            System.out.println("data = null or distanceSquared > 400");
            return;
        }
        SerializedEntity serializedEntity = JeffLib.getNMSHandler().serialize(event.getEntity());
        event.getEntity().remove();
        data.getEntities().add(serializedEntity);
    }

    private boolean isValidDamageCause(EntityDamageEvent.DamageCause cause) {
        return cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
    }

    private boolean isRestorableEntity(EntityType entityType) {
        return plugin.getConfig().getRestorableEntityTypes().contains(entityType);
    }

    private boolean isGoingToDie(EntityDamageByEntityEvent event) {
        if(event instanceof LivingEntity) {
            System.out.println("Living entity!");
            LivingEntity entity = (LivingEntity) event;
            System.out.println("Health: " + entity.getHealth());
            System.out.println("Damage: " + event.getFinalDamage());
            return entity.getHealth() - event.getFinalDamage() <= 0;
        } else {
            System.out.println("Not living: " + event.getEntity().getClass() + ", " + event.getEntity().getType());
            return true;
        }
    }
}
