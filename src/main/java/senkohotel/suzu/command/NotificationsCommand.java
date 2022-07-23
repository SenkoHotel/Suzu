package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.Command;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.util.DBUtils;

import java.sql.ResultSet;

public class NotificationsCommand extends Command {
    public NotificationsCommand() {
        name = "notifications";
        desc = "Shows your notifications.";
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Notifications")
                .setColor(Main.accentColor);

        ResultSet notifs = DBUtils.getNotifications(msg.getAuthor().getId());
        String notifsStr = "";

        while (notifs.next()) {
            notifsStr += "- " + notifs.getString("msg") + "\n";
        }

        if (notifsStr.equals("")) {
            notifsStr = "You have no notifications!";
        }

        embed.setDescription(notifsStr);

        MessageUtils.reply(msg, embed);
        DBUtils.clearNotifications(msg.getAuthor().getId());
    }
}
