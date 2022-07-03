package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.suzu.Main;
import senkohotel.suzu.commands.Command;
import senkohotel.suzu.utils.MessageUtils;
import senkohotel.suzu.xp.XPCollection;
import senkohotel.suzu.xp.XPRole;

public class RankCommand extends Command {
    public RankCommand() {
        super();
        name = "rank";
        desc = "Shows your own xp count.";
        hidden = false;
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) {
        super.exec(msg, args);

        int xpAmount = XPCollection.getXPCount(msg.getAuthor().getId());

        boolean ignoreXP = hasArgument("--ignore-xp", args);
        boolean allRoles = hasArgument("--all-roles", args);
        boolean noXP = hasArgument("--no-xp", args);

        if (xpAmount == 0 || ignoreXP) {
            MessageUtils.reply(msg, "You have not collected any xp yet! Chat a bit to get some xp!\nIf you already have xp on tatsu you can use `suzu tatsuimport` to import you tatsu score");
            return;
        }

        if (noXP)
            xpAmount = 0;

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator())
                .setDescription(xpAmount + "XP")
                .setColor(Main.accentColor);

        String nextRoles = "";
        for (XPRole role : XPCollection.roles) {
            if (role.reqXP > xpAmount) {
                double percent = ((double) xpAmount / (double) role.reqXP) * 100;
                nextRoles += role.roleIcon + " " + msg.getGuild().getRoleById(role.roleID).getName() + " - " + percent + "% (" + (role.reqXP - xpAmount) + "XP left)\n";
            }
        }

        if (nextRoles.equals("") || allRoles) {
            nextRoles = "All roles collected!";
        }

        embed.addField("Next Roles", nextRoles, false);
        MessageUtils.reply(msg, embed);
    }
}
