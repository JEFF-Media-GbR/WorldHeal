package com.jeff_media.worldheal.worldguard;

import com.jeff_media.jefflib.pluginhooks.WorldGuardUtils;
import com.jeff_media.worldheal.WorldHealPlugin;
import com.jeff_media.worldheal.data.SimpleLoc;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class WorldGuardHandler extends WorldGuardManager {

    private static final WorldHealPlugin plugin = WorldHealPlugin.getInstance();
    private final WorldGuardPlugin worldGuardPlugin;
    private final WorldGuard worldGuard;
    private final FlagRegistry flagRegistry;
    private final StateFlag restoreExplosionsFlag;

    public WorldGuardHandler() {
        worldGuardPlugin = WorldGuardPlugin.inst();
        worldGuard = WorldGuard.getInstance();
        flagRegistry = worldGuard.getFlagRegistry();
        restoreExplosionsFlag = registerOrGetStateFlag("worldheal-restore-explosions", plugin.getConfig().getFlagDefaults().isRestoreExplosions());
    }

    @Override
    public boolean isRestorationEnabled(Player player, SimpleLoc loc) {
        return testStateFlag(player, loc.asLocation(), restoreExplosionsFlag);
    }

    private StateFlag registerOrGetStateFlag(String name, boolean defaultValue) {
        StateFlag flag = new StateFlag(name, defaultValue);
        try {
            flagRegistry.register(flag);
        } catch (FlagConflictException e) {
            Flag<?> existing = flagRegistry.get(name);
            if (existing instanceof StateFlag) {
                flag = (StateFlag) existing;
            } else {
                throw new RuntimeException("Flag " + name + " already exists and is not a StateFlag");
            }
        }
        return flag;
    }

    public boolean testStateFlag(@Nonnull final Player player, @Nonnull final Location location, @Nonnull final StateFlag flag) {
        final LocalPlayer localPlayer = worldGuardPlugin.wrapPlayer(player);
        final ApplicableRegionSet set = getRegionSet(location);
        return set.testState(localPlayer, flag);
    }

    private ApplicableRegionSet getRegionSet(@Nonnull final Location location) {
        final RegionContainer container = worldGuard.getPlatform().getRegionContainer();
        final RegionQuery query = container.createQuery();
        return query.getApplicableRegions(BukkitAdapter.adapt(location));
    }
}
