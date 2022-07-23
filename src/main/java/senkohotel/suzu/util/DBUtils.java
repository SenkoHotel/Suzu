package senkohotel.suzu.util;

import senkohotel.suzu.api.SuzuDB;

import java.sql.ResultSet;

public class DBUtils {
    public static ResultSet getUsers() {
        return SuzuDB.execQuery("SELECT * FROM `ranks`");
    }

    public static ResultSet getTop() {
        return SuzuDB.execQuery("SELECT * FROM `ranks` ORDER BY `xp` DESC");
    }

    public static ResultSet updateXP(int xp, String id) {
        return SuzuDB.execQuery("UPDATE `ranks` SET `xp` = '" + xp + "' WHERE `ranks`.`userid` = '" + id + "'");
    }

    public static ResultSet updateUsername(String name, String id) {
        return SuzuDB.execQuery("UPDATE `ranks` SET `username` = '" + name + "' WHERE `ranks`.`userid` = '" + id + "'");
    }

    public static ResultSet insertNewUser(int xp, String id, String name) {
        return SuzuDB.execQuery("INSERT INTO `ranks` (`userid`, `xp`, `username`) VALUES ('" + id + "', '" + xp + "', '" + name + "')");
    }

    public static ResultSet getRoles() {
        return SuzuDB.execQuery("SELECT * FROM `roles` ORDER BY `xp` ASC");
    }

    public static ResultSet getNotifications(String id) {
        return SuzuDB.execQuery("SELECT * FROM `notifications` WHERE `uid` = '" + id + "'");
    }

    public static ResultSet clearNotifications(String id) {
        return SuzuDB.execQuery("DELETE FROM `notifications` WHERE `notifications`.`uid` = '" + id + "'");
    }
}
