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

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RankCommand extends Command {
    public RankCommand() {
        super();
        name = "rank";
        desc = "Shows your own xp count.";
        hidden = false;
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        Member m;
        if (args.length > 0) {
            String userID = args[0].replace("<@", "").replace(">", "");
            try {
                m = msg.getGuild().getMemberById(userID);
                if (m == null)
                    m = msg.getGuild().retrieveMemberById(userID).complete();
            } catch (Exception ex) {
                m = msg.getMember();
            }
        } else {
            m = msg.getMember();
        }

        int xpAmount = XPCollection.getXPCount(m.getUser().getId());

        boolean ignoreXP = hasArgument("--ignore-xp", args);
        boolean allRoles = hasArgument("--all-roles", args);
        boolean noXP = hasArgument("--no-xp", args);
        boolean ignoreCollectedRoles = hasArgument("--ignore-collected", args); // shows all roles even if you already collected them

        if (xpAmount == 0 || ignoreXP) {
            if (m != msg.getMember())
                MessageUtils.reply(msg, "That user has not collected any xp yet!");
            else
                MessageUtils.reply(msg, "You have not collected any xp yet! Chat a bit to get some xp!\nIf you already have xp on tatsu you can use `suzu tatsuimport` to import you tatsu score");
            return;
        }

        if (noXP)
            xpAmount = 0;

        String title = m.getUser().getName() + "#" + m.getUser().getDiscriminator();

        Color topRoleColor = new Color(Main.accentColor); // the embeds color, if no role is found, it uses the bots default color
        for (XPRole role : XPCollection.roles) {
            if (xpAmount > role.reqXP) {
                title += " " + role.roleIcon;
                topRoleColor = msg.getGuild().getRoleById(role.roleID).getColor();
            }
        }

        String rank = "";

        try {
            ResultSet rs = DBUtils.getTop();
            int i = 0;
            while (rs.next()) {
                i++;
                if (rs.getString("userid").equals(m.getUser().getId())) {
                    rank = " | #" + i;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(title)
                .setThumbnail(m.getUser().getAvatarUrl())
                .setDescription(xpAmount + "XP" + rank)
                .setColor(topRoleColor);

        String nextRoles = "";
        for (XPRole role : XPCollection.roles) {
            if (role.reqXP > xpAmount || ignoreCollectedRoles) {

                float percent = ((float) xpAmount / (float) role.reqXP) * 100;
                NumberFormat formatter = new DecimalFormat("#0.00");
                nextRoles += role.roleIcon + " " + msg.getGuild().getRoleById(role.roleID).getName() + " - " + formatter.format(percent) + "% (" + (role.reqXP - xpAmount) + "XP left)\n";
            }
        }

        if (nextRoles.equals("") || allRoles) {
            nextRoles = "All roles collected!";
        }

        embed.addField("Next Roles", nextRoles, false);

        MessageUtils.reply(msg, embed);
    }
}
