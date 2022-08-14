package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.SlashCommand;
import senkohotel.suzu.util.DBUtils;
import senkohotel.suzu.xp.XPCollection;
import senkohotel.suzu.xp.XPRole;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RankSlashCommand extends SlashCommand {
    public RankSlashCommand() {
        name = "rank";
        description = "Shows your own xp count.";
    }

    public void exec(SlashCommandInteraction interact) {
        int xpAmount = XPCollection.getXPCount(interact.getUser().getId());

        if (xpAmount == 0) {
            interact.reply("You have not collected any xp yet! Chat a bit to get some xp!\nIf you already have xp on tatsu you can use `suzu tatsuimport` to import you tatsu score");
            return;
        }

        // badges and role color
        String badges = "";
        Color topRoleColor = new Color(Main.accentColor); // the embeds color, if no role is found, it uses the bots default color
        for (XPRole role : XPCollection.roles) {
            if (xpAmount > role.reqXP) {
                badges += role.roleIcon;
                topRoleColor = interact.getGuild().getRoleById(role.roleID).getColor();
            }
        }

        // rank calculation
        int rank = 0;
        try {
            ResultSet rs = DBUtils.getTop();
            while (rs.next()) {
                rank++;
                if (rs.getString("userid").equals(interact.getUser().getId())) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // next role
        String nextRole = "";
        for (XPRole role : XPCollection.roles) {
            if (role.reqXP > xpAmount) {
                float percent = ((float) xpAmount / (float) role.reqXP) * 100;
                NumberFormat formatter = new DecimalFormat("#0.00");
                nextRole += role.roleIcon + " " + formatter.format(percent) + "% (" + (role.reqXP - xpAmount) + "XP left)\n";
                break;
            }
        }

        if (nextRole.equals("")) {
            nextRole = "All roles collected!";
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(interact.getUser().getName() + "#" + interact.getUser().getDiscriminator(), null, interact.getUser().getAvatarUrl())
                .setColor(topRoleColor);

        embed.addField("<:SK_mangaLaugh:792020838990872586> XP", xpAmount + "", true);
        embed.addField("\u2b06\ufe0f Rank", "#" + rank, true);
        embed.addField("\u23e9 Next Role", nextRole, true);

        reply(interact, embed);
    }
}
