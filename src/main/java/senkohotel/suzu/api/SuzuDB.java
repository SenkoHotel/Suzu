package senkohotel.suzu.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SuzuDB {
    static JsonObject dbConf = null;
    static HikariDataSource ds;

    public static void initDB () {
        try {
            dbConf = JsonParser.parseString(Files.readString(Path.of("config/suzu.json"))).getAsJsonObject();

            HikariConfig config = new HikariConfig();
            config.addDataSourceProperty("serverName", dbConf.get("dbHost").getAsString());
            config.addDataSourceProperty("databaseName", dbConf.get("dbName").getAsString());
            config.addDataSourceProperty("port", 3306);
            config.setUsername(dbConf.get("dbUser").getAsString());
            config.setPassword(dbConf.get("dbPassword").getAsString());
            config.setPoolName("suzu");
            config.setIdleTimeout(0);
            config.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            ds = new HikariDataSource(config);
        } catch (Exception ex) {
            System.out.println("Couldn't initialize the DataBase!");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static ResultSet execQuery(String query) {
        try {
            PreparedStatement ps = ds.getConnection().prepareStatement(query);
            return ps.executeQuery();
        } catch (Exception ex) {
            System.out.println("[DataBase] Couldn't execute query '" + query + "'");
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getUsers() {
        return execQuery("SELECT * FROM `suzu`");
    }

    public static ResultSet updateXP(int xp, String id) {
        return execQuery("UPDATE `suzu` SET `xp` = '" + xp + "' WHERE `suzu`.`userid` = '" + id + "'");
    }

    public static ResultSet insertNewUser(int xp, String id) {
        return execQuery("INSERT INTO `suzu` (`userid`, `xp`) VALUES ('" + id + "', '" + xp + "')");
    }
}
