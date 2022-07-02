package senkohotel.suzu.utils;

import senkohotel.suzu.api.SuzuDB;

import java.sql.ResultSet;

public class DBUtils {
    public static ResultSet getUsers() {
        return SuzuDB.execQuery("SELECT * FROM `suzu`");
    }

    public static ResultSet getTop() {
        return SuzuDB.execQuery("SELECT * FROM `suzu` ORDER BY `xp` DESC");
    }

    public static ResultSet updateXP(int xp, String id) {
        return SuzuDB.execQuery("UPDATE `suzu` SET `xp` = '" + xp + "' WHERE `suzu`.`userid` = '" + id + "'");
    }

    public static ResultSet insertNewUser(int xp, String id) {
        return SuzuDB.execQuery("INSERT INTO `suzu` (`userid`, `xp`) VALUES ('" + id + "', '" + xp + "')");
    }
}
