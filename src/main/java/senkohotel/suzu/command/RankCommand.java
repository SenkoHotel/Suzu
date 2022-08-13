package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
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

        // badges and role color
        String badges = "";
        Color topRoleColor = new Color(Main.accentColor); // the embeds color, if no role is found, it uses the bots default color
        for (XPRole role : XPCollection.roles) {
            if (xpAmount > role.reqXP) {
                badges += role.roleIcon;
                topRoleColor = msg.getGuild().getRoleById(role.roleID).getColor();
            }
        }

        // rank calculation
        int rank = 0;
        try {
            ResultSet rs = DBUtils.getTop();
            while (rs.next()) {
                rank++;
                if (rs.getString("userid").equals(m.getUser().getId())) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // next role
        String nextRole = "";
        for (XPRole role : XPCollection.roles) {
            if (role.reqXP > xpAmount || ignoreCollectedRoles) {
                float percent = ((float) xpAmount / (float) role.reqXP) * 100;
                NumberFormat formatter = new DecimalFormat("#0.00");
                nextRole += role.roleIcon + " " + formatter.format(percent) + "% (" + (role.reqXP - xpAmount) + "XP left)\n";
                break;
            }
        }

        if (nextRole.equals("") || allRoles) {
            nextRole = "All roles collected!";
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(m.getUser().getName() + "#" + m.getUser().getDiscriminator(), null, m.getUser().getAvatarUrl())
                .setColor(topRoleColor);

        embed.addField("<:SK_mangaLaugh:792020838990872586> XP", xpAmount + "", true);
        embed.addField("\u2b06\ufe0f Rank", "#" + rank, true);
        embed.addField("\u23e9 Next Role", nextRole, true);

        MessageBuilder message = new MessageBuilder()
                .setEmbeds(embed.build());

        ResultSet notifications = DBUtils.getNotifications(msg.getAuthor().getId());
        int notifCount = 0;

        while (notifications.next()) {
            notifCount++;
        }

        if (notifCount != 0) {
            message.setContent(msg.getAuthor().getAsMention() + " You have " + notifCount + " notifications! (use `suzu notifications` to see them)");
        }

        MessageUtils.reply(msg, message);
    }
}
