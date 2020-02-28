package me.albert.morechests.chest;

import me.albert.morechests.MoreChests;
import me.albert.morechests.database.Storage;
import me.albert.morechests.inventory.ChestInv;
import me.albert.morechests.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;


public class Chest {
    private Location location;
    private ItemStack item;
    private ArrayList<Inventory> invs;
    private String name;
    private Integer size;
    private HashSet<UUID> viewers = new HashSet<>();

    public Chest(ItemStack item, ArrayList<Inventory> invs, Location location){
        this.item = item;
        this.invs = invs;
        this.location = location;
        loadID(item.getItemMeta().getDisplayName());
        loadSize();
        if (invs == null){
            ChestInv.loadChestInv(this);
        }
        Storage.chests.add(this);
    }

    private void loadID(String name){
        FileConfiguration config = MoreChests.getInstance().getConfig();
        for (String chest: config.getConfigurationSection("chests").getKeys(false)){
            if (name.equalsIgnoreCase(config.getString("chests."+chest+".name").replace("&","§"))){
                this.name = chest;
            }
        }
    }

    private void loadSize(){
        this.size = MoreChests.getInstance().getConfig().getInt("chests."+name+".page");
    }

    public String getID() {
        return name;
    }

    public void setInvs(ArrayList<Inventory> invs) {
        this.invs = invs;
    }


    public void delete(){
        location.getBlock().setType(Material.AIR);
        dropItem(item);
        for (Inventory inventory : invs){
            for (int i = 0; i<45;i++){
                ItemStack is = inventory.getItem(i);
                if (is == null) continue;
                dropItem(is);
            }
        }
        for (UUID uuid : viewers){
            if (Bukkit.getPlayer(uuid)!=null){
                Bukkit.getPlayer(uuid).closeInventory();
            }

        }
        MoreChests.getStorage().deleteChest(location);
    }

    private void dropItem(ItemStack item){
        location.getWorld().dropItem(location,item);
    }


    public void save(){
        MoreChests.getStorage().saveChest(this);
    }

    public void open(Player p){
        if (viewers.size() == 0 && !p.hasPermission("morechest.silent"))
            location.getWorld().playSound(location,Sounds.CHEST_OPEN.bukkitSound(),0.6F,1);
        viewers.add(p.getUniqueId());
        p.openInventory(invs.get(0));
        p.setMetadata("morechests",new FixedMetadataValue(MoreChests.getInstance(),this));
    }

    public void open(Player p,int page){
        viewers.add(p.getUniqueId());
        p.openInventory(invs.get(page));
        p.setMetadata("morechests",new FixedMetadataValue(MoreChests.getInstance(),this));
    }

    public void close(Player p){
            viewers.remove(p.getUniqueId());
            if (!p.hasPermission("morechest.silent"))
            location.getWorld().playSound(location,Sounds.CHEST_CLOSE.bukkitSound(),0.6F,1);
            save();
    }

    public ItemStack getItemStack() {
        return item;
    }

    public Location getLocation() {
        return location;
    }

    public ArrayList<Inventory> getInvs() {
        return invs;
    }

    public Integer getSize() { return size; }

    public HashSet<UUID> getViewers() {
        return viewers;
    }
}
