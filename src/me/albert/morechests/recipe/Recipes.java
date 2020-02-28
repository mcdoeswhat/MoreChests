package me.albert.morechests.recipe;

import me.albert.morechests.MoreChests;
import me.albert.morechests.utils.ItemUtil;
import me.albert.morechests.utils.SkullUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class Recipes {
    private static MoreChests instance = MoreChests.getInstance();

    public static void loadRecipe(){
        List<String> keys = new ArrayList<>(instance.getConfig().getConfigurationSection("chests").getKeys(false));
        for (String key : keys) {
            ItemStack material = new ItemStack(Material.getMaterial(instance.getConfig().getString("chests."+key + ".material").toUpperCase()));
            try {
                Bukkit.getServer().addRecipe(getRecipe(material.getType(),key));
            } catch (Exception ignored){}
        }
    }

    private static Recipe getRecipe(Material material, String name){
        String displayName = instance.getConfig().getString("chests."+name+".name").replace("&","§");
        String value = instance.getConfig().getString("chests."+name+".value");
        ItemStack result = ItemUtil.make(SkullUtil.getHead(value),displayName);
        NamespacedKey namespacedKey = new NamespacedKey(instance, instance.getName()+System.nanoTime());
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey,result);
        recipe.shape(
                "MMM",
                "MCM",
                "MMM"
        );
        recipe.setIngredient( 'M', material);
        recipe.setIngredient( 'C', Material.CHEST);
        return recipe;
    }

}
