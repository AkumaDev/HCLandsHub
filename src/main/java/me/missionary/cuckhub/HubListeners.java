/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.missionary.cuckhub;

import lombok.RequiredArgsConstructor;

import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by Missionary (missionarymc@gmail.com) on 5/28/2017.
 */
@RequiredArgsConstructor
public class HubListeners implements Listener {

    private final Hub plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.teleport(player.getWorld().getSpawnLocation());
        player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        player.getInventory().clear();
        player.getInventory().setItem(4, plugin.getSelectorItem());
        plugin.getConfig().getStringList("motd").forEach(s -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', s)));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            switch (player.getInventory().getHeldItemSlot()) {
                case 4:
                    plugin.getServerSelector().showMenu(player);
                    break;
                default:
                    break;
            }
        }
    }

    private final static String ADMIN_PERMISSION = "hub.admin";


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!(event.getPlayer().getGameMode() == GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getPlayer().getGameMode() == GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("hub.admin") || !player.isOp()){
            event.setCancelled(true);
        }
        //Default formatting is %1$s <- Player name, %2$s <- Player's message. Use section symbols here.
        event.setFormat("%1$s§7: §f%2$s");
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPLMV(PlayerMoveEvent event) {
        if (event.getFrom().getY() > event.getTo().getY()) {
            if (event.getTo().getY() <= plugin.getConfig().getInt("voidteleport")) {
                event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onBreak(InventoryClickEvent e) {
        if (e.getWhoClicked().getGameMode() == GameMode.CREATIVE){
            return;
        }
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
            e.setCancelled(true);
        }
        if (e.getSlotType() == InventoryType.SlotType.QUICKBAR) {
            e.setCancelled(true);
        }
        if (e.getSlotType() == InventoryType.SlotType.CONTAINER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCropTrample(final PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMob(EntityTargetEvent e){
        e.setCancelled(true);
    }
}
