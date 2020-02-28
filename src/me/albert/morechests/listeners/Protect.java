package me.albert.morechests.listeners;

import me.albert.morechests.MoreChests;
import me.albert.morechests.database.Storage;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class Protect implements Listener {

    private static Storage storage = MoreChests.getStorage();

    private boolean isChestBlock(Block block){
        return (storage.getChest(block.getLocation()) != null);
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent e){
        if (isChestBlock(e.getBlockClicked().getRelative(e.getBlockFace()))){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        for (int i = 0;i<e.blockList().size();i++) {
            Block block = e.blockList().get(i);
            if (isChestBlock(block)) {
                e.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e){
        for (int i = 0;i<e.blockList().size();i++) {
            Block block = e.blockList().get(i);
            if (isChestBlock(block)) {
                e.blockList().remove(block);
            }
        }
    }
    @EventHandler
    public void onGrow(StructureGrowEvent e){
        for (int i = 0;i<e.getBlocks().size();i++) {
            Block block = e.getBlocks().get(i).getBlock();
            if (isChestBlock(block)) {
                e.setCancelled(true);
                break;
            }
        }

    }
    @EventHandler
    public void onPiston(BlockPistonExtendEvent e){
        for (int i = 0;i<e.getBlocks().size();i++) {
            Block block = e.getBlocks().get(i);
            if (isChestBlock(block)) {
                e.setCancelled(true);
                break;
            }
        }

    }

    @EventHandler
    public void onPiston(BlockPistonRetractEvent e){
        for (int i = 0;i<e.getBlocks().size();i++) {
            Block block = e.getBlocks().get(i);
            if (isChestBlock(block)) {
                e.setCancelled(true);
                break;
            }
        }

    }
    @EventHandler
    public void onFlow(BlockFromToEvent e){
        if (isChestBlock(e.getToBlock())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if (isChestBlock(e.getBlock())){
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onDrop(EntityChangeBlockEvent e){
        if (isChestBlock(e.getBlock())){
            e.setCancelled(true);
        }

    }

}
