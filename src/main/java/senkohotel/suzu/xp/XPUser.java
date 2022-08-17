package senkohotel.suzu.xp;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.util.DBUtils;

public class XPUser {
    public int xp = 0;
    public long lastXPTimestamp = 0;

    public XPUser () {}

    public void addXP (int amount, MessageReceivedEvent msg) {
        if (System.currentTimeMillis() - lastXPTimestamp < 60000) {
            return;
        }

        lastXPTimestamp = System.currentTimeMillis();

        xp += amount;
        DBUtils.updateXP(xp, msg.getAuthor().getId());
        DBUtils.updateUsername(msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator(), msg.getAuthor().getId());
        checkForNewRoles(msg);
    }

    void checkForNewRoles(MessageReceivedEvent msg) {
        for (XPRole role : XPCollection.roles) {
            if (xp >= role.reqXP)
                addRole(msg, role);
        }
    }

    void addRole(MessageReceivedEvent msg, XPRole role) {
        if (checkForRole(msg, role.roleID))
            return;

        msg.getGuild().addRoleToMember(msg.getMember(), msg.getGuild().getRoleById(role.roleID)).complete();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Congratulations " + msg.getAuthor().getName() + " on getting a new role!")
                .setDescription("Keep it up! <:SK_stronk:792073244156231710>")
                .addField("Role", role.roleIcon + " " + msg.getGuild().getRoleById(role.roleID).getName(), false)
                .setColor(msg.getGuild().getRoleById(role.roleID).getColor());

        MessageUtils.send(msg.getChannel().getId(), embed);
    }

    boolean checkForRole (MessageReceivedEvent msg, String roleID) {
        return msg.getMember().getRoles().contains(msg.getGuild().getRoleById(roleID));
    }

    public void setXP (int amount) {
        xp = amount;
    }
}
