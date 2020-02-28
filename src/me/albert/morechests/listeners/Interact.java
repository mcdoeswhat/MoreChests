package me.albert.morechests.listeners;

import me.albert.morechests.MoreChests;
import me.albert.morechests.chest.Chest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK
                    || e.getClickedBlock().getType() != Material.SKULL
                    || e.getPlayer().getOpenInventory().getTopInventory().getSize() > 10
            || e.getPlayer().isSneaking()){
                return;
            }
            Location loc = e.getClickedBlock().getLocation();
            if (MoreChests.getStorage().getChest(loc) != null){
                Player p = e.getPlayer();
                if (!canOpen(p,loc)) return;
                Chest chest = MoreChests.getStorage().getChest(e.getClickedBlock().getLocation());
                chest.open(p);
                e.setCancelled(true);
            }

    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        Bukkit.getScheduler().runTask(MoreChests.getInstance(), () -> {
            if (p.getOpenInventory().getTopInventory().getSize() < 54 && p.hasMetadata("morechests")){
                Chest chest = (Chest) p.getMetadata("morechests").get(0).value();
                chest.getViewers().remove(p.getUniqueId());
                if (chest.getViewers().size() == 0) {
                    p.removeMetadata("morechests", MoreChests.getInstance());
                    chest.close(p);
                }
            }
        });

    }

    private static boolean canOpen(Player player, Location location){
        Block block = new ChestBlock(location);
        PlayerInteractEvent e = new PlayerInteractEvent(player,Action.RIGHT_CLICK_BLOCK
                ,player.getItemInHand(), block,BlockFace.EAST);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return !e.isCancelled();
    }

}
