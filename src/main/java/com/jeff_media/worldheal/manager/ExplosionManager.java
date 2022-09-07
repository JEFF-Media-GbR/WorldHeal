package com.jeff_media.worldheal.manager;

import com.jeff_media.worldheal.WorldHealPlugin;
import com.jeff_media.worldheal.data.ExplosionData;
import com.jeff_media.worldheal.data.SimpleLoc;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;
import java.util.stream.Collectors;

public class ExplosionManager {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();
    @Getter private final List<ExplosionData> explosions = new ArrayList<>();
    @Getter private final Map<Location, ExplosionData> entityExplosions = new HashMap<>();


    public void register(List<Block> blockList, SimpleLoc loc) {
        ExplosionData data = new ExplosionData(blockList
                .stream()
                .filter(block -> !isAlreadyRegistered(block))
                .collect(Collectors.toList()),
                plugin.getConfig().getDelay(), loc);
        explosions.add(data);
    }

    public boolean isAlreadyRegistered(Block block) {
        for(ExplosionData data : explosions) {
            SimpleLoc sloc = new SimpleLoc(block);
            if(data.contains(sloc)) return true;
        }
        return false;
    }
}
