package com.jeff_media.worldheal.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.gson.FieldAttributes;
import com.jeff_media.jefflib.CommandUtils;
import com.jeff_media.jefflib.TimeUtils;
import com.jeff_media.worldheal.WorldHealPlugin;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.concurrent.TimeUnit;

@CommandAlias("worldheal")
@CommandPermission("worldheal.admin")
public class WorldHealCommand extends BaseCommand {

    @Subcommand("reload")
    public void reload(CommandSender sender) {
        WorldHealPlugin.getInstance().reloadEverything();
        sender.sendMessage("Reloaded WorldHeal configuration.");
    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender) {
        CommandUtils.sendHelpMessage(sender, CommandUtils.HelpStyle.SAME_LINE_COMPACT, "worldheal reload","Reloads the configuration file");
    }

    @Subcommand("reset-config")
    public void resetConfig(CommandSender sender) {
        if(!new File(WorldHealPlugin.getInstance().getDataFolder(), "config.yml").delete()) {
            sender.sendMessage("Could not delete config.yml");
            return;
        }
        WorldHealPlugin.getInstance().reloadEverything();
        sender.sendMessage("Reset WorldHeal configuration.");
    }

    @Subcommand("restore-all")
    public void restoreAll(CommandSender sender) {
        TimeUtils.startTimings("restoreall");
        WorldHealPlugin.getInstance().getExplosionManager().restoreEverythingNow();
        long nanoSeconds = TimeUtils.endTimings("restoreall", false);
        long milliSeconds = TimeUnit.MILLISECONDS.convert(nanoSeconds, TimeUnit.NANOSECONDS);
        sender.sendMessage("Restored all leftover explosions in " + milliSeconds + " ms.");
    }
}
