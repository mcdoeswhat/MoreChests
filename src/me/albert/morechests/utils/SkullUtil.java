package me.albert.morechests.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
//author Albert
public class SkullUtil {
    @SuppressWarnings("deprecation")
    public static ItemStack getHead(String value) {
        ItemStack skull;
        if (getVersion() > 12) {
            skull = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            skull = new ItemStack(Objects.requireNonNull(Material.getMaterial("SKULL_ITEM")), 1, (short) SkullType.PLAYER.ordinal());
        }
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
    }

    public static String getHeadValue(String name){
        try {
            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
            Gson g = new Gson();
            JsonObject obj = g.fromJson(result, JsonObject.class);
            String uid = obj.get("id").toString().replace("\"","");
            String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
            obj = g.fromJson(signature, JsonObject.class);
            String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            obj = g.fromJson(decoded,JsonObject.class);
            String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(skinByte));
        } catch (Exception ignored){ }
        return "";
    }

    private static String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try{
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8) );
            String str;
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ignored) { }
        finally{
            try{
                if(in!=null) {
                    in.close();
                }
            }catch(IOException ignored) { }
        }
        return sb.toString();
    }

    private static int getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        name = (name.substring(name.lastIndexOf('.') + 1) + ".").substring(3);
        return Integer.parseInt(name.substring(0, name.length() - 4));
    }
}
