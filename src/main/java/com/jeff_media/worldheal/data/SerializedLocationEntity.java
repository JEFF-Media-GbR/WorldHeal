package com.jeff_media.worldheal.data;

import com.jeff_media.jefflib.JeffLib;
import com.jeff_media.jefflib.data.SerializedEntity;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class SerializedLocationEntity {

    @Getter private final Location location;
    @Getter private final SerializedEntity entity;

    public SerializedLocationEntity(Location location, SerializedEntity entity) {
        this.location = location;
        this.entity = entity;
    }

    public void spawn() {
        entity.spawn(location);
    }

    public SerializedLocationEntity(Entity entity) {
        this(entity.getLocation(), JeffLib.getNMSHandler().serialize(entity));
    }
}
