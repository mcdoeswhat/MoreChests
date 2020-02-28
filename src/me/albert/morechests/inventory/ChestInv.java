package me.albert.morechests.inventory;

import me.albert.morechests.MoreChests;
import me.albert.morechests.chest.Chest;
import me.albert.morechests.utils.ItemUtil;
import me.albert.morechests.utils.SkullUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ChestInv {

    public static void loadChestInv(Chest chest){
        ArrayList<Inventory> invs = new ArrayList<>();
        for (int i = 0;i<chest.getSize();i++){
            Inventory inventory = Bukkit.createInventory(null,54,chest.getItemStack()
            .getItemMeta().getDisplayName()+(i+1)+"/"+chest.getSize());
            invs.add(inventory);
            inventory.setItem(inventory.getSize()-9,getPrev());
            inventory.setItem(inventory.getSize()-1,getNext());
            for (int a = 0;a<7;a++){
                inventory.setItem(52-a,new ItemStack(Material.STAINED_GLASS_PANE,1,(short)3));
            }
        }
        chest.setInvs(invs);
    }


    private static ItemStack getPrev(){
        ItemStack item = SkullUtil.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1MmUyYjkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3MzRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0=");
        String name = MoreChests.getInstance()
                .getConfig().getString("messages.prev_page").replace("&","§");
        return ItemUtil.make(item,name);
    }

    private static ItemStack getNext(){
        final ItemStack head = SkullUtil.getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmEzYjhmNjgxZGFhZDhiZjQzNmNhZThkYTNmZTgxMzFmNjJhMTYyYWI4MWFmNjM5YzNlMDY0NGFhNmFiYWMyZiJ9fX0=");
        String name = MoreChests.getInstance()
                .getConfig().getString("messages.next_page").replace("&","§");
        return ItemUtil.make(head,name);
    }

}
