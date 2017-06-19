package me.missionary.cuckhub.commands;

import lombok.RequiredArgsConstructor;
import me.missionary.cuckhub.Hub;
import me.missionary.cuckhub.framework.Command;
import me.missionary.cuckhub.framework.CommandArgs;
import me.signatured.ezqueuespigot.EzQueueAPI;
import org.bukkit.entity.Player;

/**
 * Created by Interlagos on 6/3/2017.
 */
@RequiredArgsConstructor
public class TestQueueAPICommand {

    private final Hub plugin;

    @Command(name = "testqueue", inGameOnly = true)
    public void onCommand(CommandArgs args) {
        Player sender = args.getPlayer();
        if (EzQueueAPI.getQueue(sender) != null) {
            String queueName = EzQueueAPI.getQueue(sender);
            sender.sendMessage("*QUEUE INFORMATION* QUEUE Name: " + queueName + ", Players in QUEUE " + queueName + ": " + EzQueueAPI.getPlayersInQueue(queueName) + '.');
        } else {
            sender.sendMessage(sender.getName() + " is currently not in a queue.");
            sender.sendMessage("If you have joined a queue this queue plugin is fucking garbage.");
        }
    }
}
