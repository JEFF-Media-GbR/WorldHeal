package com.jeff_media.worldheal.listener;

import com.jeff_media.jefflib.JeffLib;
import com.jeff_media.jefflib.data.SerializedEntity;
import com.jeff_media.worldheal.WorldHealPlugin;
import com.jeff_media.worldheal.data.ExplosionData;
import com.jeff_media.worldheal.data.SimpleLoc;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.EntityEquipment;

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

    }

    @EventHandler
    public void onDeath(EntityDamageEvent damageEvent) {
        System.out.println("DeathEvent: " + damageEvent.getEntityType());
//        EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
//        if(damageEvent == null || !isValidDamageCause(damageEvent.getCause())) {
//            System.out.println("no valid cause");
//            return;
//        }
        SimpleLoc damagerLoc = null;
        if(damageEvent instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
            damagerLoc = new SimpleLoc(entityDamageByEntityEvent.getDamager().getLocation());
        } else if(damageEvent instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent entityDamageByBlockEvent = (EntityDamageByBlockEvent) damageEvent;
            Block damagerBlock = entityDamageByBlockEvent.getDamager();
            if(damagerBlock != null) {
                damagerLoc = new SimpleLoc(entityDamageByBlockEvent.getDamager().getLocation());
            }
        }
        if(damagerLoc == null) {
            return;
        }
        handleExplodeEntity(damageEvent.getEntity(), damagerLoc);
    }

    private void handleExplodeEntity(Entity entity, SimpleLoc origin) {
        if(!isRestorableEntity(entity.getType())) {
            System.out.println("no proper entity type");
            return;
        }
        plugin.getExplosionManager().register(entity, origin);
        stripEntity(entity);
        entity.remove();
    }

    private void stripEntity(Entity entity) {
        if(entity instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) entity;
            itemFrame.setItem(null);
        }
        if(entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            EntityEquipment equipment = livingEntity.getEquipment();
            if(equipment != null) equipment.clear();
        }
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
