package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.suzu.Main;
import senkohotel.suzu.commands.Command;
import senkohotel.suzu.utils.MessageUtils;
import senkohotel.suzu.utils.TimeUtils;
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
    public void exec(MessageReceivedEvent msg, String[] args) {
        super.exec(msg, args);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Stats")
                .setColor(Main.accentColor);

        int totalXP = 0;
        int users = 0;
        embed.setFooter(TimeUtils.formatHMS((System.currentTimeMillis() - Main.startTime.getTime())) + " since startup");

        for (Map.Entry<String, XPUser> user : XPCollection.users.entrySet()) {
            users++;
            totalXP += user.getValue().xp;
        }

        embed.addField("Total Users", users + "", false);
        embed.addField("Total XP", totalXP + "XP", false);

        MessageUtils.reply(msg, embed);
    }
}
