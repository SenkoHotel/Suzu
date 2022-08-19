package senkohotel.suzu.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import senkohotel.hotelbot.Main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SuzuDB {
    static JsonObject dbConf = null;
    static HikariDataSource dataSource;

    public static HikariDataSource newDS() {
        try {
            dbConf = JsonParser.parseString(Files.readString(Path.of("config/suzu.json"))).getAsJsonObject();

            HikariConfig config = new HikariConfig();
            config.addDataSourceProperty("serverName", dbConf.get("dbHost").getAsString());
            config.addDataSourceProperty("databaseName", dbConf.get("dbName").getAsString());
            config.addDataSourceProperty("port", 3306);
            config.setUsername(dbConf.get("dbUser").getAsString());
            config.setPassword(dbConf.get("dbPassword").getAsString());
            config.setPoolName("xp");
            config.setIdleTimeout(0);
            config.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
            return new HikariDataSource(config);
        } catch (Exception ex) {
            Main.LOG.error("Failed to load database!");
            ex.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public static ResultSet execQuery(String query) {
        try {
            if (dataSource == null)
                dataSource = newDS();

            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            con.close();
            return rs;
        } catch (Exception ex) {
            Main.LOG.error("Couldn't execute query: " + query);
            ex.printStackTrace();
            return null;
        }
    }
}
