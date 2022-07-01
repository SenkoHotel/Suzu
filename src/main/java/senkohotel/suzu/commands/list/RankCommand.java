package senkohotel.suzu.commands.list;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.suzu.commands.Command;
import senkohotel.suzu.utils.MessageUtils;
import senkohotel.suzu.xp.XPCollection;

public class RankCommand extends Command {
    public RankCommand() {
        super();
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) {
        super.exec(msg, args);

        int xpAmount = XPCollection.getXPCount(msg.getAuthor().getId());

        if (xpAmount == 0) {
            MessageUtils.reply(msg, "You have not collected any xp yet! Chat a bit to get some xp!");
            return;
        }

        MessageUtils.reply(msg, "You have " + xpAmount + "XP");
    }
}
