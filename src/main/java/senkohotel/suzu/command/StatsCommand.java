package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.Command;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.util.TimeUtils;
import senkohotel.suzu.xp.XPCollection;
import senkohotel.suzu.xp.XPUser;

import java.util.Map;

public class StatsCommand extends Command {
    public StatsCommand() {
        super();
        name = "stats";
        desc = "shows the bot's stats";
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Stats")
                .setColor(Main.accentColor);

        int totalXP = 0;
        int users = 0;
        embed.setFooter(TimeUtils.formatHMS((System.currentTimeMillis() - Main.startTime)) + " since startup");

        for (Map.Entry<String, XPUser> user : XPCollection.users.entrySet()) {
            users++;
            totalXP += user.getValue().xp;
        }

        embed.addField("Total Users", users + "", false);
        embed.addField("Total XP", totalXP + "XP", false);

        MessageUtils.reply(msg, embed);
    }
}
