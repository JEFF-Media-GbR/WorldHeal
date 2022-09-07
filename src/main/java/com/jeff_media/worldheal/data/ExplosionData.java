package com.jeff_media.worldheal.data;

import com.jeff_media.jefflib.JeffLib;
import com.jeff_media.jefflib.data.SerializedEntity;
import com.jeff_media.worldheal.WorldHealPlugin;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.*;
import java.util.stream.Collectors;

public class ExplosionData {

    @Override
    public String toString() {
        return "ExplosionData{" + "blocks=" + blocks.size() + ", entities=" + entities.size() + ", restoreTime=" + restoreTime + ", forceLoadedChunks=" + forceLoadedChunks + '}';
    }

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();
    @Getter private final List<FullBlockData> blocks;
    private final long restoreTime;
    @Getter private final List<SerializedLocationEntity> entities;
    @Getter private final Set<Chunk> forceLoadedChunks;
    @Getter private final SimpleLoc location;

    public ExplosionData(SimpleLoc location) {
        this.blocks = new ArrayList<>();
        //this.blocksIterator = blocks.iterator();
        this.restoreTime = System.currentTimeMillis() + plugin.getConfig().getDelay();
        this.entities = new ArrayList<>();
        this.forceLoadedChunks = new HashSet<>();
        this.location = location;
    }

    public void addBlocks(List<Block> blockList) {
        blocks.addAll(blockList
                .stream()
                .filter(block -> !plugin.getConfig().getIgnoredBlockTypes().contains(block.getType()))
                .map(FullBlockData::new)
                .collect(Collectors.toList()));
    }

    private ExplosionData(List<Block> blocks, long delayInMs, SimpleLoc location) {
        this.blocks = blocks.stream().map(FullBlockData::new).collect(Collectors.toList());
        //this.blocksIterator = this.blocks.iterator();
        this.restoreTime = System.currentTimeMillis() + delayInMs;
        this.forceLoadedChunks = plugin.getConfig().isKeepChunksLoaded() ? blocks.stream().map(Block::getChunk).collect(Collectors.toSet()) : Collections.emptySet();
        this.forceLoadedChunks.forEach(plugin.getChunkManager()::keepChunk);
        this.entities = new ArrayList<>();
        this.location = location;
    }

    public boolean contains(SimpleLoc sloc) {
        return blocks.stream().map(FullBlockData::getLoc).anyMatch(loc -> loc.equals(sloc));
    }

    public boolean isReadyToRestore() {
        return System.currentTimeMillis() >= restoreTime;
    }

    public int restore(int number) {
        int restored = 0;
        Iterator<FullBlockData> blocksIterator = blocks.iterator();
        while(blocksIterator.hasNext() && restored < number) {
            FullBlockData data = blocksIterator.next();
            Block inWorld =data.getLoc().asBlock();
            if(inWorld != null) {
                inWorld.breakNaturally();
            }
            data.restore();
            blocksIterator.remove();
            restored++;
        }
        if(!blocksIterator.hasNext() && restored < number) {
            Iterator<SerializedLocationEntity> entityIterator = entities.iterator();
            while(entityIterator.hasNext()) {
                SerializedLocationEntity entity = entityIterator.next();
                entity.spawn();
                entityIterator.remove();
                restored++;
            }
        }
        return restored;
    }


    public void addEntity(Entity entity) {
        SerializedLocationEntity serialized = new SerializedLocationEntity(entity);
        System.out.println("Added SerializedEntity: " + serialized.getEntity().getNbtData() + " @ " + serialized.getLocation());
        entities.add(serialized);
    }
}
