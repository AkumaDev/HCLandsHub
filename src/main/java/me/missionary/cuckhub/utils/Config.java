/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.missionary.cuckhub.utils;

/**
 * Created by Missionary (missionarymc@gmail.com) on 5/28/2017.
 */

import me.missionary.cuckhub.Hub;
import me.missionary.cuckhub.objects.Server;
import net.minecraft.util.com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Config {

    public static ConcurrentHashMap<String, Server> servers = new ConcurrentHashMap<>();
    private JavaPlugin plugin;
    private String configName;
    private File configurationFile;
    private FileConfiguration configuration;

    /**
     * Constructs a new Config file object.
     * @param plugin The corresponding plugin that is using this Utility.
     * @param configName The name of the Config.
     * @param folder The directory that the config file is located.
     */
    public Config(JavaPlugin plugin, String configName, String folder) {
        if (plugin == null) {
            throw new IllegalStateException("Plugin must not be null!");
        }
        this.plugin = plugin;
        this.configName = configName;
        if (folder == null) {
            this.configurationFile = new File(plugin.getDataFolder(), configName);
        } else {
            this.configurationFile = new File(plugin.getDataFolder() + "/" + folder, configName);
        }
    }

    public FileConfiguration getConfiguration() {
        if (configuration == null) {
            this.reloadConfig();
        }
        return configuration;
    }

    public File getFile() {
        return configurationFile;
    }

    public void reloadConfig() {
        configuration = YamlConfiguration.loadConfiguration(configurationFile);
        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(configName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            configuration.setDefaults(defConfig);
        }
    }

    public void saveConfig() {
        if (configuration != null && configurationFile != null) {
            try {
                getConfiguration().save(configurationFile);
            } catch (IOException ex) {
                plugin.getLogger().info("Configuration save failed! Reason: " + ex.getCause());
            }
        }
    }

    public void saveDefaultConfig() {
        if (!configurationFile.exists()) {
            this.plugin.saveResource(configName, false);
        }
    }

    public void deleteConfig() {
        configurationFile.delete();
    }


    public static ItemStack getServerIcon(String srv) {
        Server server = servers.get(srv);
        ItemStack serverItem = server.getItem();
        ItemMeta serverItemMeta = serverItem.getItemMeta();
        List<String> newLore = Lists.newArrayList();
        newLore.addAll(serverItemMeta.getLore());
        serverItemMeta.setLore(newLore);
        serverItem.setItemMeta(serverItemMeta);
        return serverItem;
    }

    public static void registerServers() {
        FileConfiguration kf = Hub.getInstance().getServers().getConfiguration();
        for (String s : kf.getKeys(false)) {
            Server server = new Server(s,
                    Material.getMaterial(kf.getString(s + ".Material")),
                    kf.getInt(s + ".Data"),
                    ChatColor.translateAlternateColorCodes('&', kf.getString(s + ".Display-Name")),
                    kf.getStringList(s + ".Lore"),
                    kf.getInt(s + ".Slot"),
                    kf.getString(s + ".Command"));
            servers.put(s, server);
        }
    }
}