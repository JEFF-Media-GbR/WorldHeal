package com.jeff_media.worldheal.worldguard;

import com.jeff_media.worldheal.WorldHealPlugin;
import com.jeff_media.worldheal.data.SimpleLoc;
import org.bukkit.entity.Player;

public class WorldGuardManager {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();

    public boolean isRestorationEnabled(Player player, SimpleLoc loc) {
        return plugin.getConfig().getFlagDefaults().isRestoreExplosions();
    }

}
