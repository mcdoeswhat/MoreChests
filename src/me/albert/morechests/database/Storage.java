package me.albert.morechests.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.albert.morechests.MoreChests;
import me.albert.morechests.chest.Chest;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Storage {
    private static final String CHEST_TABLE = "chests";

    private final HikariDataSource dataSource;

    public static HashSet<Chest> chests = new HashSet<>();

    private FileConfiguration cfg = MoreChests.getInstance().getConfig();

    private int i = cfg.getInt("storage.type");

    public Storage() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("MoreChests");
        config.setDriverClassName(getDriver(i));
        config.setUsername(cfg.getString("storage.username"));
        config.setPassword(cfg.getString("storage.password"));
        Properties properties = new Properties();
        String jdbcUrl = "jdbc:";
        if (i == 0) {
            String folderPath = MoreChests.getInstance().getDataFolder().getAbsolutePath() + "/MoreChests.db";
            jdbcUrl += "sqlite://" + folderPath;
        } else{
            jdbcUrl += "mysql://" + cfg.getString("storage.host") + ':' +
                    cfg.getString("storage.port") + '/' + cfg.getString("storage.database");
            properties.setProperty("useSSL", cfg.getString("storage.useSSL"));
        }
        config.setConnectionTestQuery("SELECT 1");
        config.setMaximumPoolSize(1);
        properties.setProperty("date_string_format", "yyyy-MM-dd HH:mm:ss");
        config.setJdbcUrl(jdbcUrl);
        config.setDataSourceProperties(properties);
        this.dataSource = new HikariDataSource(config);
    }

    private String getDriver(int i){
        if (i == 0){
            return "org.sqlite.JDBC";
        }
        return "com.mysql.jdbc.Driver";
    }

    public void createTables() throws SQLException {
        String file;
        if (i == 0) {file = "/sqlite.sql";} else {file = "/create.sql";}
        try (InputStream in = getClass().getResourceAsStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement()) {
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) continue;

                builder.append(line);
                if (line.endsWith(";")) {
                    String sql = builder.toString();
                    if (dataSource.getJdbcUrl().contains("sqlite")) {
                        sql = sql.replace("AUTO_INCREMENT", "AUTOINCREMENT");
                    }

                    stmt.addBatch(sql);
                    builder = new StringBuilder();
                }
            }

            stmt.executeBatch();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void placeChest(ItemStack chest, Location location) {
            String locString = DataUtil.locationToString(location);
            String itemString = DataUtil.itemToString(chest);
            try (Connection con = dataSource.getConnection();
                 PreparedStatement stmt = con.prepareStatement("INSERT INTO " + CHEST_TABLE
                         + " (location, chest_item, items) VALUES"
                         + " (?, ?, ?)", RETURN_GENERATED_KEYS)) {
                stmt.setString(1,locString);
                stmt.setString(2,itemString);
                stmt.setString(3,null);
                stmt.executeUpdate();
        } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
    }

    public void saveChest(Chest chest){
        String locString = DataUtil.locationToString(chest.getLocation());
        String itemString = DataUtil.itemToString(chest.getItemStack());
        String items = DataUtil.invsToString(chest.getInvs());
        String replace = "";
        if (i != 0){replace = "ON DUPLICATE KEY UPDATE location = ?,chest_item = ?,items = ?";}
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("INSERT INTO " + CHEST_TABLE
                     + " (location, chest_item, items) VALUES"
                     + " (?, ?, ?)"+replace, RETURN_GENERATED_KEYS)) {
            stmt.setString(1,locString);
            stmt.setString(2,itemString);
            stmt.setString(3,items);
            if (i != 0) {
                stmt.setString(4, locString);
                stmt.setString(5, itemString);
                stmt.setString(6, items);
            }
            stmt.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }

    public void deleteChest(Location location){
        for (Chest chest : chests){
            if (chest.getLocation().equals(location)){
                chests.remove(chest);
                break;
            }
        }
        String locString = DataUtil.locationToString(location);
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("DELETE" + " FROM " + CHEST_TABLE + " WHERE location= ? ")) {
            stmt.setString(1, locString);
            stmt.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public Chest getChest(Location location) {
        for (Chest chest : chests){
            if (chest.getLocation().equals(location)){
                return chest;
            }
        }
        String locString = DataUtil.locationToString(location);
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT location, chest_item, items" +
                     " FROM " + CHEST_TABLE + " WHERE location=? ORDER BY ChestID DESC LIMIT 1")) {
            stmt.setString(1, locString);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    ItemStack is = DataUtil.stringToItem(resultSet.getString("chest_item"));
                    return new Chest(is,DataUtil.stringToInvs(resultSet.getString("items")),location);
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }


    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }


}
