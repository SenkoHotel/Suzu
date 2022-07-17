package senkohotel.suzu.xp;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.util.DBUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class XPCollection {
    public static HashMap<String, XPUser> users = new HashMap<>();

    public static ArrayList<XPRole> roles = new ArrayList<>();

    public static void initRoleList () {
        roles.add(new XPRole(1000, "792185558863970325", "<:classicTail:992862741561868388>")); // classic
        roles.add(new XPRole(2500, "792220150874243072", "<:bronzeTail:992862740249055252>")); // bronze
        roles.add(new XPRole(5000, "792220517285101598", "<:silverTail:992862742790811758>")); // silver
        roles.add(new XPRole(10000, "792221386751868929", "<:goldTail:992862738634244146>")); // gold
        roles.add(new XPRole(25000, "815158447031975967", "<:platinumTail:992862743793246261>")); // platinum
        roles.add(new XPRole(50000, "815162865359126528", "<:rubyTail:992862737476632616>")); // ruby
        roles.add(new XPRole(100000, "815158741703720961", "<:SK_stronk:792073244156231710>")); // overlord
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
