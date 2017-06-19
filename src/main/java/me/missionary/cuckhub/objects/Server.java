/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.missionary.cuckhub.objects;

/**
 * Created by Missionary (missionarymc@gmail.com) on 5/28/2017.
 */
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class Server {
    @Getter
    private String name;

    @Getter
    private Material item;

    @Getter
    private int data;

    @Getter
    private String displayName;

    @Getter
    private List<String> lore;

    private int slot;

    @Getter
    private String command;

    public Server(String name, Material item, int data, String displayName, List<String> lore, int slot, String command) {
        this.name = name;
        this.item = item;
        this.data = data;
        this.displayName = displayName;
        this.lore = lore;
        this.slot = slot;
        this.command = command;
    }


    public int getSlot() {
        return slot - 1;
    }

    public ItemStack getItem() {
        ItemStack serverItem = new ItemStack(item, 1, (short) data);
        ItemMeta serverItemMeta = serverItem.getItemMeta();
        serverItemMeta.setDisplayName(displayName);
        List<String> lore = this.lore.stream().map(aLore -> aLore.replace('&', '§')).collect(Collectors.toList());
        serverItemMeta.setLore(lore);
        serverItem.setItemMeta(serverItemMeta);
        return serverItem;
    }
}