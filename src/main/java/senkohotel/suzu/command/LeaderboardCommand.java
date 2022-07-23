package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.Command;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.util.DBUtils;
import senkohotel.suzu.xp.XPCollection;
import senkohotel.suzu.xp.XPRole;

import java.sql.ResultSet;

public class LeaderboardCommand extends Command {
    public LeaderboardCommand() {
        super();
        name = "top";
        desc = "List of the users with the most xp.";
        hidden = false;
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        int page = 1;
        try {
            page = Integer.parseInt(args[0]);
            if (page > 10)
                page = 10;
            if (page < 1)
                page = 0;
        } catch (Exception ex) {
            // do nothing i guess...?
        }

        try {
            ResultSet rs = DBUtils.getTop();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Top Users - Page " + page)
                    .setColor(Main.accentColor)
                    .setFooter("use 'suzu top <1-10>' to cycle between pages");

            int i = 0;
            while (rs.next()) {
                i++;

                if (!(i > page * 10 || i < (10 * (page - 1) + 1))) {
                    try {
                        Member m;
                        m = msg.getGuild().getMemberById(rs.getString("userid"));
                        if (m == null)
                            m = msg.getGuild().retrieveMemberById(rs.getString("userid")).complete();

                        embed.addField(
                                "#" + i + " - " + m.getUser().getName() + "#" + m.getUser().getDiscriminator() + " " + getRoleIcon(rs.getInt("xp")),
                                rs.getInt("xp") + "XP",
                                false
                        );
                    } catch (Exception ex) { // probably didnt find member
                        String name = rs.getString("username");

                        if (name == null || name.isEmpty())
                            name = rs.getString("userid");

                        embed.addField(
                                "#" + i + " - (left) " + name + " " + getRoleIcon(rs.getInt("xp")),
                                rs.getInt("xp") + "XP",
                                false
                        );
                    }
                }
            }

            MessageUtils.reply(msg, embed);
        } catch (Exception ex) {
            MessageUtils.reply(msg, "That didnt work...\nMaybe try again?");
            ex.printStackTrace();
        }
    }

    String getRoleIcon(int xp) {
        String roleIcon = "";
        for (XPRole role : XPCollection.roles) {
            if (xp > role.reqXP)
                roleIcon = role.roleIcon;
        }
        return roleIcon;
    }
}
