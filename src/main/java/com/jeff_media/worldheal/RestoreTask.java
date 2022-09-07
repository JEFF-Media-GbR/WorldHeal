package com.jeff_media.worldheal;

import com.jeff_media.worldheal.data.ExplosionData;
import com.jeff_media.worldheal.manager.ExplosionManager;

import java.util.Iterator;

public class RestoreTask implements Runnable {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();

    @Override
    public void run() {
        ExplosionManager manager = plugin.getExplosionManager();
        Iterator<ExplosionData> iterator = manager.getOldExplosions().iterator();
        while(iterator.hasNext()) {
            ExplosionData data = iterator.next();
            if(data.isReadyToRestore()) {
                int restored = data.restore(plugin.getConfig().getRestoreAtOnce());
                if(restored == 0) {
                    if(plugin.isDebug()) {
                        plugin.debug("Explosion " + data + " has no blocks left to restore");
                    }
                    data.getForceLoadedChunks().forEach(plugin.getChunkManager()::freeChunk);
                    iterator.remove();
                } else {
                    if(plugin.isDebug()) {
                        plugin.debug("Restored " + restored + " blocks for explosion " + data);
                    }
                }
            } else {
                if(plugin.isDebug()) {
                    plugin.debug("Explosion " + data + " is not ready to restore yet");
                }
            }
        }
    }
}
