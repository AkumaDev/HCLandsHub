/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.missionary.cuckhub.scoreboard;

import me.missionary.cuckhub.Hub;
import me.missionary.lilac.BoardProvider;
import me.signatured.ezqueueshared.QueueInfo;
import me.signatured.ezqueuespigot.EzQueueAPI;
import net.minecraft.util.com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;


/**
 * Created by Missionary (missionarymc@gmail.com) on 5/29/2017.
 */
public class HubProvider implements BoardProvider {

    private final Hub plugin = Hub.getInstance();

    @Override
    public List<String> provide(Player player) {
        List<String> entries = Lists.newArrayList();
        entries.add("&7&m-------------------&r");
        entries.add(ChatColor.GOLD.toString() + ChatColor.BOLD + "Online" + ChatColor.GRAY + ":");
        entries.add(ChatColor.WHITE.toString() + plugin.getBungeePlayers());
        entries.add(ChatColor.AQUA.toString() + ChatColor.RED);
        entries.add(ChatColor.GOLD + ChatColor.BOLD.toString() + "Rank" + ChatColor.GRAY + ":");
        entries.add(getFancyGroupName(Hub.getPermission().getPrimaryGroup(player)));
        if (plugin.isUsingEZQueue()) {
            if (EzQueueAPI.getQueue(player) != null) {
                entries.add(ChatColor.GOLD.toString() + ChatColor.BOLD + "Queue" + ChatColor.GRAY + ":");
                entries.add(QueueInfo.getQueueInfo(QueueInfo.getQueue(player.getName())).getQueue());
                entries.add("#" + QueueInfo.getPosition(player.getName()) + " of " + QueueInfo.getQueueInfo(player.getName()).getPlayersInQueue().toString());
                entries.add(ChatColor.BLACK.toString() + ChatColor.ITALIC);
            }
        }
        entries.add("&7&m-------------------");
        //Because if there is no entries someone darn fucked up...
        if (entries.size() == 2) {
            return null;
        }
        return entries;
    }

    @Override
    public String getTitle(Player player) {
        return plugin.getScoreboard().getConfiguration().getString("title");
    }

    private String getFancyGroupName(String group) {
        FileConfiguration config = plugin.getGroups().getConfiguration();
        if (config.getString(group) != null) {
            return ChatColor.translateAlternateColorCodes('&', config.getString(group));
        } else {
            return group;
        }
    }
}
