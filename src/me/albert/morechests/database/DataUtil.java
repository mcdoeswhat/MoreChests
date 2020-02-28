package me.albert.morechests.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DataUtil {

    public static String locationToString(Location location){
        YamlConfiguration config = new YamlConfiguration();
        config.set("location.x", location.getX());
        config.set("location.y", location.getY());
        config.set("location.z", location.getZ());
        config.set("location.world", location.getWorld().getName());
        return config.saveToString();
    }

    public static String itemToString(ItemStack itemStack) {
        itemStack.setAmount(1);
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", itemStack);
        return config.saveToString();
    }

    public static ItemStack stringToItem(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringBlob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config.getItemStack("item", null);
    }

    public static String invsToString(ArrayList<Inventory> invs){
        YamlConfiguration config = new YamlConfiguration();
        for (int i = 0;i<invs.size();i++){
            config.set("invs."+ i, invToBase64(invs.get(i)));
        }
        return config.saveToString();
    }

    public static ArrayList<Inventory> stringToInvs(String configString){
        if (configString == null) return null;
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(configString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        ArrayList<Inventory> invs = new ArrayList<>();
        for (String key : config.getConfigurationSection("invs").getKeys(false)){
            Inventory inventory = base64ToInv(config.getString("invs."+key));
            invs.add(inventory);
        }
        return invs;
    }


    public static String invToBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(e);
            dataOutput.writeInt(inventory.getSize());
            dataOutput.writeUTF(inventory.getTitle());
            for(int i = 0; i < inventory.getSize(); ++i) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(e.toByteArray());
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot into itemstacks!", ex);
        }
    }

    public static Inventory base64ToInv(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt(),dataInput.readUTF());
            for(int i = 0; i < inventory.getSize(); ++i) {
                inventory.setItem(i, (ItemStack)dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (Exception ex) {
            return null;
        }
    }


}
