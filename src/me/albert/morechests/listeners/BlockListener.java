package me.albert.morechests.listeners;

import me.albert.morechests.MoreChests;
import me.albert.morechests.chest.Chest;
import me.albert.morechests.utils.Chests;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {

    @EventHandler (ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent e){
        if (Chests.isChest(e.getItemInHand())){
            MoreChests.getStorage().placeChest(e.getItemInHand().clone(),e.getBlock().getLocation());
        }

    }

    @EventHandler (ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e){
        if (MoreChests.getStorage().getChest(e.getBlock().getLocation()) != null) {
            Chest chest = MoreChests.getStorage().getChest(e.getBlock().getLocation());
            int items = 0;
            for (Inventory inventory : chest.getInvs()) {
                for (ItemStack itemStack : inventory) {
                    if (itemStack != null) {
                        items++;
                    }
                }
            }
            if (items > 54 + 9 * chest.getSize()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(MoreChests.getInstance().getConfig()
                        .getString("messages.too_many_items", "too many items in chest, clear it first!")
                .replace("&","§"));
                return;
            }
            e.setDropItems(false);
            Bukkit.getScheduler().runTask(MoreChests.getInstance(), () -> {
                if (e.isCancelled()) return;
                chest.delete();
            });
        }


    }


}
