package me.albert.morechests.utils;

import me.albert.morechests.MoreChests;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chests {
    public static Set<ItemStack> chests = new HashSet<>();

    private static MoreChests instance = MoreChests.getInstance();

    static {
        loadChests();
    }
    public static Boolean isChest(ItemStack itemStack){
        for (ItemStack is : chests){
            if (is.isSimilar(itemStack)){
                return true;
            }
        }
        return false;
    }



    private static void loadChests(){
        List<String> keys = new ArrayList<>(instance.getConfig().getConfigurationSection("chests").getKeys(false));
        for (String key : keys) {
            String displayName = instance.getConfig().getString("chests."+key+".name").replace("&","§");
            String value = instance.getConfig().getString("chests."+key+".value");
            ItemStack result = ItemUtil.make(SkullUtil.getHead(value),displayName);
            chests.add(result);
        }

    }
}
