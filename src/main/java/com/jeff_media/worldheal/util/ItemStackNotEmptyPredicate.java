package com.jeff_media.worldheal.util;

import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class ItemStackNotEmptyPredicate implements Predicate<ItemStack> {

    public static ItemStackNotEmptyPredicate INSTANCE = new ItemStackNotEmptyPredicate();

    @Override
    public boolean test(ItemStack itemStack) {
        return itemStack != null && itemStack.getAmount() > 1;
    }
}
