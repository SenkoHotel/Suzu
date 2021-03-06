package senkohotel.suzu.xp;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.util.DBUtils;

import java.time.OffsetDateTime;

public class XPUser {
    public int xp = 0;
    OffsetDateTime lastXPTimestamp = null;

    public XPUser () {}

    public void addXP (int amount, MessageReceivedEvent msg) {
        if (lastXPTimestamp == null) {
            lastXPTimestamp = msg.getMessage().getTimeCreated();
        } else {
            int timeOffset = msg.getMessage().getTimeCreated().getMinute() - lastXPTimestamp.getMinute();

            if (timeOffset == 0)
                return;

            lastXPTimestamp = msg.getMessage().getTimeCreated();
        }

        xp += amount;
        DBUtils.updateXP(xp, msg.getAuthor().getId());
        DBUtils.updateUsername(msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator(), msg.getAuthor().getId());
        checkForNewRoles(msg);
    }

    void checkForNewRoles(MessageReceivedEvent msg) {
        for (XPRole role : XPCollection.roles) {
            if (xp >= role.reqXP)
                addRole(msg, role.roleID);
        }
    }

    void addRole (MessageReceivedEvent msg, String roleID) {
        if (checkForRole(msg, roleID))
            return;

        msg.getGuild().addRoleToMember(msg.getMember(), msg.getGuild().getRoleById(roleID)).complete();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Congratulations " + msg.getAuthor().getName() + " on getting `" + msg.getGuild().getRoleById(roleID).getName() + "`.")
                .setDescription("Keep it up! <:SK_stronk:792073244156231710>")
                .setColor(msg.getGuild().getRoleById(roleID).getColor());

        MessageUtils.send(msg.getChannel().getId(), embed);
    }

    boolean checkForRole (MessageReceivedEvent msg, String roleID) {
        return msg.getMember().getRoles().contains(msg.getGuild().getRoleById(roleID));
    }

    public void setXP (int amount) {
        xp = amount;
    }
}
