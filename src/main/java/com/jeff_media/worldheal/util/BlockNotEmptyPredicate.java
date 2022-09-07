package com.jeff_media.worldheal.util;

import org.bukkit.block.Block;

import java.util.function.Predicate;

public class BlockNotEmptyPredicate implements Predicate<Block> {

    public static BlockNotEmptyPredicate INSTANCE = new BlockNotEmptyPredicate();

    @Override
    public boolean test(Block block) {
        return !block.getType().isAir();
    }
}
