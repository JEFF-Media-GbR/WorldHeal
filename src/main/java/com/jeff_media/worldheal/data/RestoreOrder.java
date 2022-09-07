package com.jeff_media.worldheal.data;

import com.jeff_media.worldheal.WorldHealPlugin;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;

public enum RestoreOrder {
    BOTTOM_FIRST {
        @Override
        public void apply(List<Block> blocks, Location explosionCenter) {
            blocks.sort(Comparator.comparingInt(b -> b.getLocation().getBlockY()));
        }
    },
    CENTER_FIRST {
        @Override
        public void apply(List<Block> blocks, Location explosionCenter) {
            blocks.sort(Comparator.comparingDouble(b -> b.getLocation().distanceSquared(explosionCenter)));
        }
    },
    RANDOM() {
        @Override
        public void apply(List<Block> blocks, Location explosionCenter) {
            Collections.shuffle(blocks);
        }
    }, OUTSIDE_FIRST {
        @Override
        public void apply(List<Block> blocks, Location explosionCenter) {
            CENTER_FIRST.apply(blocks, explosionCenter);
            Collections.reverse(blocks);
        }
    };

    public abstract void apply(List<Block> blocks, Location explosionCenter);

    public static RestoreOrder fromString(String string) {
        switch (string.toLowerCase(Locale.ROOT)) {
            case "bottom-first": return BOTTOM_FIRST;
            case "center-first": return CENTER_FIRST;
            case "outside-first": return OUTSIDE_FIRST;
            case "random": return RANDOM;
            default:
                WorldHealPlugin.getInstance().getLogger().warning("Invalid restore-order: \"" + string + "\". Using default (random)");
                return RANDOM;
        }
    }

}
