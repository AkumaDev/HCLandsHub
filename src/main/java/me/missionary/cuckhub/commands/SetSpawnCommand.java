/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.missionary.cuckhub.commands;

import lombok.RequiredArgsConstructor;
import me.missionary.cuckhub.Hub;
import me.missionary.cuckhub.framework.Command;
import me.missionary.cuckhub.framework.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by Missionary (missionarymc@gmail.com) on 5/28/2017.
 */
@RequiredArgsConstructor
public class SetSpawnCommand {

    private final Hub plugin;

    @Command(name = "setspawn", aliases = {"hubsetspawn"}, permission = "hub.setspawn", description = "Command used to set the spawn point of the hub", inGameOnly = true)
    public void handleCommand(CommandArgs args){
        Player player = args.getPlayer();
        World world = player.getLocation().getWorld();
        world.setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        player.sendMessage(ChatColor.GREEN + "You have successfully saved the new spawn point for the world " + world.getName() + ", type " + world.getEnvironment().toString()  +'.');
    }
}