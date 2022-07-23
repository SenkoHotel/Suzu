package senkohotel.suzu.xp;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.util.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class XPCollection {
    public static HashMap<String, XPUser> users = new HashMap<>();

    public static ArrayList<XPRole> roles = new ArrayList<>();

    public static void initRoleList () {
        ResultSet rs = DBUtils.getRoles();
        try {
            while (rs.next()) {
                roles.add(new XPRole(rs.getInt("xp"), rs.getString("id"), rs.getString("icon")));
                System.out.println(
                        "Added role: " + rs.getString("id")
                                + " with xp: " + rs.getInt("xp")
                                + " and icon: " + rs.getString("icon"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void gainXP(@NotNull MessageReceivedEvent msg) {
        if (msg.getAuthor().isBot())
            return;
        if (!msg.getChannelType().isGuild()) {
            MessageUtils.reply(msg, "I dunno what you're trying to do, but you cant collect xp in DMs.");
            return;
        } else {
            if (!msg.getGuild().getId().equals("791993321374613514"))
                return; // not senkohotel lol
        }

        if (msg.getChannel().getId().equals("843148981939994624") ||
                msg.getChannel().getId().equals("838037612371378186") ||
                msg.getChannel().getId().equals("843149348756783144")) {
            return; // dont collect xp in these channels
        }

        Random rn = new Random();
        int gainedXP = rn.nextInt((20 - 10) + 1) + 10;

        if (users.containsKey(msg.getAuthor().getId())) {
            users.get(msg.getAuthor().getId()).addXP(gainedXP, msg);
        } else {
            XPUser newuser = new XPUser();
            newuser.setXP(gainedXP);
            DBUtils.insertNewUser(gainedXP, msg.getAuthor().getId(), msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator());
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
            ResultSet rs = DBUtils.getUsers();
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
