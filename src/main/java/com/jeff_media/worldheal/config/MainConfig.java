package com.jeff_media.worldheal.config;

import com.jeff_media.jefflib.EnumUtils;
import com.jeff_media.jefflib.data.Config;
import com.jeff_media.worldheal.data.RestoreOrder;
import lombok.Getter;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import java.util.Set;

public class MainConfig extends Config {

    @Getter private final long delay = getLong("delay");
    @Getter private final int restoreAtOnce = getInt("restore-at-once");
    @Getter private final int restoreEveryTick = getInt("restore-every-tick");
    @Getter private final boolean preventBuild = getBoolean("prevent-building");
    @Getter private final boolean restoreInventories = getBoolean("restore-inventories");
    @Getter private final RestoreOrder restoreOrder = RestoreOrder.fromString(getString("restore-order"));
    @Getter private final boolean keepChunksLoaded = getBoolean("keep-chunks-loaded");
    @Getter private final boolean debug = getBoolean("debug");
    @Getter private final Set<EntityType> restorableEntityTypes = EnumUtils.getEnumsFromList(EntityType.class, getStringList("restorable-entity-types"));

    public MainConfig() {
        super("config.yml");
    }

}