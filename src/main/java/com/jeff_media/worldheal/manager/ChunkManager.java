package com.jeff_media.worldheal.manager;

import com.jeff_media.worldheal.WorldHealPlugin;
import org.bukkit.Chunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChunkManager {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();
    private final HashMap<Chunk, Integer> loadedChunks = new HashMap<>();

    public void keepChunk(Chunk chunk) {
        loadedChunks.put(chunk, loadedChunks.getOrDefault(chunk, 0) + 1);
        plugin.debug("Keeping chunk loaded: " + chunk);
        chunk.addPluginChunkTicket(plugin);
    }

    public void freeChunk(Chunk chunk) {
        plugin.debug("Trying to free chunk " + chunk + "...");
        loadedChunks.computeIfPresent(chunk, (__, amount) -> (amount - 1 <= 0) ? null : amount - 1);
        if(!loadedChunks.containsKey(chunk)) {
            plugin.debug("...freeing chunk " + chunk);
            chunk.removePluginChunkTicket(plugin);
        } else {
            plugin.debug("...cannot free chunk " + chunk + " yet");
        }

    }
}
