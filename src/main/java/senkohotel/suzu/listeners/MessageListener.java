package senkohotel.suzu.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import senkohotel.suzu.Main;
import senkohotel.suzu.commands.CommandList;
import senkohotel.suzu.xp.XPCollection;

public class MessageListener extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent msg) {
        String content = msg.getMessage().getContentRaw().toLowerCase();

        boolean usedCommand = false;
        for (String prefix : Main.prefix) {
            if (content.startsWith(prefix)) {
                usedCommand = true;
                CommandList.check(msg, prefix);
            }
        }

        if (!usedCommand) {
            XPCollection.gainXP(msg);
        }
    }
}