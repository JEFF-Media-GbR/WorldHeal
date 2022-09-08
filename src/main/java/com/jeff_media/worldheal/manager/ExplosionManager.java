package com.jeff_media.worldheal.manager;

import com.jeff_media.jefflib.Tasks;
import com.jeff_media.worldheal.WorldHealPlugin;
import com.jeff_media.worldheal.data.ExplosionData;
import com.jeff_media.worldheal.data.SimpleLoc;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ExplosionManager {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();
    //@Getter private final List<ExplosionData> explosions = new ArrayList<>();
    @Getter private final Map<SimpleLoc, ExplosionData> explosions = new HashMap<>();
    @Getter private final List<ExplosionData> oldExplosions = new ArrayList<>();

    public ExplosionData get(SimpleLoc loc) {
        return explosions.computeIfAbsent(loc, __ -> {
            //System.out.println("Getting new ExplosionData for " + loc);
            if(!plugin.getWorldGuardManager().isRestorationEnabled(null, loc)) {
                return null;
            }
            ExplosionData data = new ExplosionData(loc);
            Tasks.nextTick(() -> {
                //System.out.println("Trying to move the explosionData");
                explosions.remove(loc);
                oldExplosions.add(data);
            });
            return data;
        });
    }

    public void register(List<Block> blockList, SimpleLoc loc) {
        ExplosionData data = get(loc);
        if(data != null) {
            data.addBlocks(blockList);
        }
    }

    public void register(Entity entity, SimpleLoc loc) {
        ExplosionData data = get(loc);
        if(data != null) {
            data.addEntity(entity);
        }
    }

    public boolean isAlreadyRegistered(Block block) {
        SimpleLoc sloc = new SimpleLoc(block);
        for(ExplosionData data : explosions.values()) {
            if(data.contains(sloc)) return true;
        }
        for(ExplosionData data : oldExplosions) {
            if(data.contains(sloc)) return true;
        }
        return false;
    }

    public void restoreEverythingNow() {
        for(ExplosionData data : explosions.values()) {
            restoreNow(data);
        }
        explosions.clear();
        for(ExplosionData data : oldExplosions) {
            restoreNow(data);
        }
        oldExplosions.clear();
    }

    public void restoreNow(ExplosionData data) {
        data.restore(data.getBlocks().size() + data.getEntities().size());
    }
}
