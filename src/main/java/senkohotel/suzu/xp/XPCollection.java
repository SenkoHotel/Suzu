package senkohotel.suzu.xp;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import senkohotel.suzu.Main;
import senkohotel.suzu.api.SuzuDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class XPCollection {
    public static HashMap<String, XPUser> users = new HashMap<>();

    public static ArrayList<XPRole> roles = new ArrayList<>();

    public static void initRoleList () {
        roles.add(new XPRole(1000, "792185558863970325")); // classic
        roles.add(new XPRole(2500, "792220150874243072")); // bronze
        roles.add(new XPRole(5000, "792220517285101598")); // silver
        roles.add(new XPRole(10000, "792221386751868929")); // gold
        roles.add(new XPRole(25000, "815158447031975967")); // platinum
        roles.add(new XPRole(50000, "815162865359126528")); // ruby
        roles.add(new XPRole(100000, "815158741703720961")); // overlord
    }

    public static void gainXP(@NotNull MessageReceivedEvent msg) {
        if (msg.getAuthor().isBot())
            return;
        if (!msg.getChannelType().isGuild())
            return;

        Random rn = new Random();
        int gainedXP =  rn.nextInt((20 - 10) + 1) + 10;

        if (users.containsKey(msg.getAuthor().getId())) {
            users.get(msg.getAuthor().getId()).addXP(gainedXP, msg);
        } else {
            XPUser newuser = new XPUser();
            newuser.setXP(gainedXP);
            users.put(msg.getAuthor().getId(), newuser);
        }
    }

    public static int getXPCount (String userid) {
        if (users.containsKey(userid))
            return users.get(userid).xp;
        else
            return 0;
    }

    public static void loadUsers() {
        try {
            ResultSet rs = SuzuDB.getUsers();
            while (rs.next()) {
                try {
                    XPUser newuser = new XPUser();
                    newuser.setXP(rs.getInt("xp"));
                    users.put(rs.getString("userid"), newuser);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
