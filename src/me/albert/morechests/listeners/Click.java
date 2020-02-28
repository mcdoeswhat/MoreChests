package me.albert.morechests.listeners;

import me.albert.morechests.chest.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null || e.getCurrentItem() == null){
            return;
        }
        if (!e.getWhoClicked().hasMetadata("morechests")){
            return;
        }
        int slot = e.getSlot();
        Player p = (Player) e.getWhoClicked();
        if (slot > 44){
            e.setCancelled(true);
            if (slot != 53 && slot != 45){
                return;
            }
            Chest chest = (Chest) p.getMetadata("morechests").get(0).value();
            int current = chest.getInvs().indexOf(e.getClickedInventory());
            if (slot == 45){
                if (current == 0) {
                    chest.open(p,chest.getSize()-1);
                    return;
                }
                chest.open(p,current-1);
            }
            if (slot == 53){
                if (current+1 == chest.getSize()){
                    chest.open(p);
                    return;
                }
                chest.open(p,current+1);
            }
        }


    }
}
