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
        if (content.startsWith(Main.prefix)) {
            CommandList.check(msg);
        } else {
            XPCollection.gainXP(msg);
        }
    }
}