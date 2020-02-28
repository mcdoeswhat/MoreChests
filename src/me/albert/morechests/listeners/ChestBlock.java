package me.albert.morechests.listeners;

import me.albert.morechests.MoreChests;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;

public class ChestBlock implements Block {
    private Location location;

    public ChestBlock(Location location){
        this.location = location;
    }
    @Override
    public byte getData() {
        return 0;
    }

    @Override
    public Block getRelative(int i, int i1, int i2) {
        return location.getBlock().getRelative(i,i1,i2);
    }

    @Override
    public Block getRelative(BlockFace blockFace) {
        return location.getBlock().getRelative(blockFace);
    }

    @Override
    public Block getRelative(BlockFace blockFace, int i) {
        return location.getBlock().getRelative( blockFace, i);
    }

    @Override
    public Material getType() {
        return Material.valueOf(MoreChests.getInstance().getConfig().getString("settings.fakeblock").toUpperCase());
    }

    @Override
    public int getTypeId() {
        return 0;
    }

    @Override
    public byte getLightLevel() {
        return 0;
    }

    @Override
    public byte getLightFromSky() {
        return 0;
    }

    @Override
    public byte getLightFromBlocks() {
        return 0;
    }

    @Override
    public World getWorld() {
        return location.getWorld();
    }

    @Override
    public int getX() {
        return location.getBlockX();
    }

    @Override
    public int getY() {
        return location.getBlockY();
    }

    @Override
    public int getZ() {
        return location.getBlockZ();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Location getLocation(Location loc) {
        return loc;
    }

    @Override
    public Chunk getChunk() {
        return location.getChunk();
    }

    @Override
    public void setData(byte b) {

    }

    @Override
    public void setData(byte b, boolean b1) {

    }

    @Override
    public void setType(Material material) {

    }

    @Override
    public void setType(Material material, boolean b) {

    }

    @Override
    public boolean setTypeId(int i) {
        return false;
    }

    @Override
    public boolean setTypeId(int i, boolean b) {
        return false;
    }

    @Override
    public boolean setTypeIdAndData(int i, byte b, boolean b1) {
        return false;
    }

    @Override
    public BlockFace getFace(Block block) {
        return location.getBlock().getFace(block);
    }

    @Override
    public BlockState getState() {
        return location.getBlock().getState();
    }

    @Override
    public BlockState getState(boolean b) {
        return location.getBlock().getState(b);
    }

    @Override
    public Biome getBiome() {
        return location.getBlock().getBiome();
    }

    @Override
    public void setBiome(Biome biome) {

    }

    @Override
    public boolean isBlockPowered() {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return false;
    }

    @Override
    public boolean isBlockFacePowered(BlockFace blockFace) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace blockFace) {
        return false;
    }

    @Override
    public int getBlockPower(BlockFace blockFace) {
        return 0;
    }

    @Override
    public int getBlockPower() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isLiquid() {
        return false;
    }

    @Override
    public double getTemperature() {
        return 0;
    }

    @Override
    public double getHumidity() {
        return 0;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return location.getBlock().getPistonMoveReaction();
    }

    @Override
    public boolean breakNaturally() {
        return false;
    }

    @Override
    public boolean breakNaturally(ItemStack itemStack) {
        return false;
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return location.getBlock().getDrops();
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack itemStack) {
        return location.getBlock().getDrops(itemStack);
    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue) {

    }

    @Override
    public List<MetadataValue> getMetadata(String s) {
        return location.getBlock().getMetadata(s);
    }

    @Override
    public boolean hasMetadata(String s) {
        return false;
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) {

    }
}
