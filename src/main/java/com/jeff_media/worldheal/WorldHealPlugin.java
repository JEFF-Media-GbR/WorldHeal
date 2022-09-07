package com.jeff_media.worldheal;

import co.aikar.commands.PaperCommandManager;
import com.jeff_media.worldheal.command.WorldHealCommand;
import com.jeff_media.worldheal.config.MainConfig;
import com.jeff_media.worldheal.listener.BlockPlacePreventListener;
import com.jeff_media.worldheal.listener.ExplosionListener;
import com.jeff_media.worldheal.manager.ChunkManager;
import com.jeff_media.worldheal.manager.ExplosionManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldHealPlugin extends JavaPlugin {

    @Getter private static WorldHealPlugin instance;
    @Getter private ExplosionManager explosionManager;
    @Getter private MainConfig config;
    @Getter private ChunkManager chunkManager;

    @Override
    public void onEnable() {
        instance = this;
        explosionManager = new ExplosionManager();
        chunkManager = new ChunkManager();
        reloadEverything();
        PaperCommandManager acf = new PaperCommandManager(this);
        acf.registerCommand(new WorldHealCommand());
    }

    public void reloadEverything() {
        getServer().getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        config = new MainConfig();
        getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
        Bukkit.getScheduler().runTaskTimer(this, new RestoreTask(), 0, getConfig().getRestoreEveryTick());
        if(getConfig().isPreventBuild()) {
            getServer().getPluginManager().registerEvents(new BlockPlacePreventListener(), this);
        }
    }

    public boolean isDebug() {
        return getConfig().isDebug();
    }

    public void debug(String s) {
        if(isDebug()) {
            getLogger().warning("[DEBUG] " + s);
        }
    }
}
