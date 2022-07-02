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

        boolean ignoreXP = false; //  to simulate not having xp

        for (String arg : args) {
            if (arg.equals("--ignore-xp"))
                ignoreXP = true;
        }

        if (xpAmount == 0 || ignoreXP) {
            MessageUtils.reply(msg, "You have not collected any xp yet! Chat a bit to get some xp!\nIf you already have xp on tatsu you can use `suzu tatsuimport` to import you tatsu score");
            return;
        }

        MessageUtils.reply(msg, "You have " + xpAmount + "XP");
    }
}
