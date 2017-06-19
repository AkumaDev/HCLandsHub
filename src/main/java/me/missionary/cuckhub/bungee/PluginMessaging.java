/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.missionary.cuckhub.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.missionary.cuckhub.Hub;
import net.minecraft.util.com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * Created by Missionary (missionarymc@gmail.com) on 5/31/2017.
 */
public class PluginMessaging implements Listener, PluginMessageListener {

    private final Hub plugin;

    //Cannot use lombok here because I have some operations running in the constructor which I could make a method for. :( smh maybe one day like @All/RequiredArgsConstructor(method = callMethodName())
    public PluginMessaging(Hub plugin) {
        this.plugin = plugin;

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            //get a random player online so we can send our message to bungee.
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            //create a new data output w/ bytes.
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            //send message
            out.writeUTF("PlayerCount");
            out.writeUTF("ALL");
            //check to see if the player is not null
            if (player != null) {
                player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }
        }, 20 * 2L, 20 * 2L);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subchannel = in.readUTF();
            if (subchannel.equalsIgnoreCase("PlayerCount")) {
                in.readUTF();
                plugin.setBungeePlayers(in.readInt());
            }
        }
    }
}
