package me.albert.morechests;

import me.albert.morechests.chest.Chest;
import me.albert.morechests.database.Storage;
import me.albert.morechests.listeners.BlockListener;
import me.albert.morechests.listeners.Click;
import me.albert.morechests.listeners.Interact;
import me.albert.morechests.listeners.Protect;
import me.albert.morechests.metrics.MetricsLite;
import me.albert.morechests.recipe.Recipes;
import me.albert.morechests.utils.Chests;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class MoreChests extends JavaPlugin {
    private static MoreChests instance;
    private static Storage storage;
    @Override
    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        storage = new Storage();
        try {
            storage.createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new Chests();
        Recipes.loadRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new Interact(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new Click(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new Protect(),this);
        new MetricsLite(this);
        getLogger().info("[MoreChest] Loaded");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")){
            this.reloadConfig();
            sender.sendMessage("§a[MoreChest] config reloaded");
            return true;
        }
        sender.sendMessage("§b/morechest reload");
        return true;
    }

    @Override
    public void onDisable(){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.hasMetadata("morechests")){
                p.closeInventory();
                p.removeMetadata("morechests", MoreChests.getInstance());
            }
        }
        for (Chest chest : Storage.chests){
            chest.save();
        }
        storage.close();
        getLogger().info("[MoreChest] UnLoaded");
    }

    public static MoreChests getInstance() {
        return instance;
    }
    public static Storage getStorage(){return storage;}
}
