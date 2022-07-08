package senkohotel.suzu.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import senkohotel.suzu.Main;
import senkohotel.suzu.commands.CommandList;
import senkohotel.suzu.utils.MessageUtils;
import senkohotel.suzu.xp.XPCollection;

public class MessageListener extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent msg) {
        String content = msg.getMessage().getContentRaw().toLowerCase();

        boolean usedCommand = false;
        for (String prefix : Main.prefix) {
            if (content.startsWith(prefix)) {
                usedCommand = true;
                if (msg.getChannelType().isGuild()) {
                    if (msg.getGuild().getId().equals("791993321374613514"))
                        CommandList.check(msg, prefix);
                    else
                        MessageUtils.reply(msg, "Did you know this bot was made for another server?\nSo you can't use this here.");
                } else {
                    MessageUtils.reply(msg, "No you cant use commands outside of a server.");
                }
            }
        }

        if (!usedCommand) {
            XPCollection.gainXP(msg);
        }
    }
}