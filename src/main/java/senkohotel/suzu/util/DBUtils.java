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

    public static void updateXP(int xp, String id) {
        SuzuDB.execQuery("UPDATE `ranks` SET `xp` = '" + xp + "' WHERE `ranks`.`userid` = '" + id + "'");
    }

    public static void updateUsername(String name, String id) {
        SuzuDB.execQuery("UPDATE `ranks` SET `username` = '" + name + "' WHERE `ranks`.`userid` = '" + id + "'");
    }

    public static void insertNewUser(int xp, String id, String name) {
        SuzuDB.execQuery("INSERT INTO `ranks` (`userid`, `xp`, `username`) VALUES ('" + id + "', '" + xp + "', '" + name + "')");
    }

    public static ResultSet getRoles() {
        return SuzuDB.execQuery("SELECT * FROM `roles` ORDER BY `xp` ASC");
    }

    public static ResultSet getNotifications(String id) {
        return SuzuDB.execQuery("SELECT * FROM `notifications` WHERE `uid` = '" + id + "'");
    }

    public static void clearNotifications(String id) {
        SuzuDB.execQuery("DELETE FROM `notifications` WHERE `notifications`.`uid` = '" + id + "'");
    }
}
