/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.missionary.cuckhub;

import lombok.Getter;
import lombok.Setter;
import me.missionary.cuckhub.bungee.PluginMessaging;
import me.missionary.cuckhub.commands.SetSpawnCommand;
import me.missionary.cuckhub.commands.TestQueueAPICommand;
import me.missionary.cuckhub.framework.CommandFramework;
import me.missionary.cuckhub.objects.Server;
import me.missionary.cuckhub.scoreboard.HubProvider;
import me.missionary.cuckhub.utils.Config;
import me.missionary.cuckhub.utils.ItemBuilder;
import me.missionary.cuckhub.utils.Menu;
import me.missionary.lilac.Lilac;
import me.missionary.lilac.Options;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.stream.Collectors;

import static me.missionary.cuckhub.utils.Config.registerServers;

/**
 * Created by Missionary (missionarymc@gmail.com) on 5/28/2017.
 */
public class Hub extends JavaPlugin {

    @Getter
    private static Hub instance;

    @Getter
    private CommandFramework framework;

    @Getter
    private Config servers;

    @Getter
    private Config scoreboard;

    @Getter
    private Config groups;

    @Getter
    private ItemStack selectorItem;

    @Getter
    private Menu serverSelector;

    @Getter
    private static Permission permission = null;

    @Getter
    @Setter
    private int bungeePlayers;

    @Getter
    @Setter
    private boolean isUsingEZQueue;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        setupPermissions();
        framework = new CommandFramework(this);
        getServer().getWorlds().forEach(world -> {
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("doMobSpawning", "false");
        });
        initializeConfigurations();
        registerCommands();
        loadItems();
        registerServers();
        loadSelector();
        getServer().getPluginManager().registerEvents(new HubListeners(this), this);
        getServer().getPluginManager().registerEvents(new PluginMessaging(this), this);
        if (getConfig().getBoolean("ezQueue")){
            setUsingEZQueue(true);
        } else {
            setUsingEZQueue(false);
        }
        new Lilac(this, new Options(), new HubProvider());
    }

    //Initialize the configurations.
    private void initializeConfigurations() {
        servers = new Config(this, "servers.yml", null);
        if (!servers.getFile().exists()) {
            servers.saveDefaultConfig();
        }
        scoreboard = new Config(this, "scoreboard.yml", null);
        if (!scoreboard.getFile().exists()) {
            scoreboard.saveDefaultConfig();
        }
        groups = new Config(this, "groups.yml", null);
        if (!groups.getFile().exists()){
            groups.saveDefaultConfig();
        }
        getLogger().info("Initialized configurations.");
    }

    //Register the commands.
    private void registerCommands() {
        framework.registerCommands(new SetSpawnCommand(this));
        framework.registerCommands(new TestQueueAPICommand(this));
    }

    //Load the hub items.
    private void loadItems() {
        selectorItem = new ItemBuilder(Material.valueOf(getConfig().getString("Selector.Item"))).setDurability((short) getConfig().getInt("Selector.Data"))
                .setName(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Selector.Name")))
                .setLore(getConfig().getStringList("Selector.Lore").stream().map(i -> ChatColor.translateAlternateColorCodes('&', i)).collect(Collectors.toList())).toItemStack();
    }

    //Load the selector information.
    private void loadSelector() {
        serverSelector = new Menu(ChatColor.translateAlternateColorCodes('&', getConfig().getString("selectorTitle")), getConfig().getInt("selectorSize"));
        serverSelector.runWhenEmpty(false);
        for (Map.Entry<String, Server> server : Config.servers.entrySet()) {
            serverSelector.setItem(server.getValue().getSlot(), Config.getServerIcon(server.getKey()));
        }
        serverSelector.setGlobalAction((player1, inv, item, slot, action) -> {
            for (Map.Entry<String, Server> server : Config.servers.entrySet()) {
                if (Config.getServerIcon(server.getKey()).equals(item)) {
                    player1.closeInventory();
                    player1.getPlayer().performCommand(server.getValue().getCommand());
                }
            }
        });
    }

    //Hook into Vault plugin.
    private void setupPermissions() {
        try {
            RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
            permission = rsp.getProvider();
        } catch (Exception e) {
            getLogger().warning("An exception was thrown whilst trying to hook into Vault. Cause: " + e.getCause() + ", Message: " + e.getMessage() + '.');
        }
    }

    @Override
    public void onDisable() {
        //Set all major static instances to null.
        //Set the permission instance to null. (static)
        permission = null;
        //Set the main instance of the plugin. (static)
        instance = null;
    }
}
