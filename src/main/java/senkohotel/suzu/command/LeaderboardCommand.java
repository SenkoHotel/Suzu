package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.suzu.Main;
import senkohotel.suzu.commands.Command;
import senkohotel.suzu.utils.DBUtils;
import senkohotel.suzu.utils.MessageUtils;

import java.sql.ResultSet;

public class LeaderboardCommand extends Command {
    public LeaderboardCommand() {
        super();
        name = "top";
        desc = "List of the users with the most xp.";
        hidden = false;
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) {
        super.exec(msg, args);

        int count = 10;
        try {
            count = Integer.parseInt(args[0]);
            if (count > 20)
                count = 20;
        } catch (Exception ex) {
            // do nothing i guess...?
        }

        try {
            ResultSet rs = DBUtils.getTop();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Top " + count + " Users")
                    .setColor(Main.accentColor);

            int i = 0;
            while (rs.next()) {
                if (i >= count)
                    break;

                i++;
                try {
                    Member m;
                    m = msg.getGuild().getMemberById(rs.getString("userid"));
                    if (m == null)
                        m = msg.getGuild().retrieveMemberById(rs.getString("userid")).complete();

                    embed.addField(
                            "#" + i + " - " + m.getUser().getName() + "#" + m.getUser().getDiscriminator() + " " + getRoleIcon(rs.getInt("xp")),
                            rs.getInt("xp") + "XP",
                            count > 10
                    );
                } catch (Exception ex) {
                    embed.addField(
                            "bruh",
                            "nice one flux, something went wrong getting the user data",
                            false
                    );
                }
            }

            MessageUtils.reply(msg, embed);
        } catch (Exception ex) {
            MessageUtils.reply(msg, "That didnt work...\nMaybe try again?");
            ex.printStackTrace();
        }
    }

    String getRoleIcon(int xp) {
        if (xp >= 100000)
            return "<:SK_stronk:792073244156231710>";
        if (xp >= 50000)
            return "<:rubyTail:992862737476632616>";
        if (xp >= 25000)
            return "<:platinumTail:992862743793246261>";
        if (xp >= 10000)
            return "<:goldTail:992862738634244146>";
        if (xp >= 5000)
            return "<:silverTail:992862742790811758>";
        if (xp >= 2500)
            return "<:bronzeTail:992862740249055252>";
        if (xp >= 1000)
            return "<:classicTail:992862741561868388>";
        else
            return "";
    }
}
