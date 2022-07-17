package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.Command;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.xp.XPCollection;

public class ImageCommand extends Command {
    public ImageCommand() {
        super();
        name = "image";
        desc = "embed perms thing";
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) {
        super.exec(msg, args);

        EmbedBuilder embed = new EmbedBuilder()
                .setDescription(
                        "Hi\nYou might ask 'Why cant i send images?'\nor 'Why is that link not making an image appear?'\nJust chat a bit more until you get the **" +
                                msg.getGuild().getRoleById(XPCollection.roles.get(0).roleID).getName() + "** " + XPCollection.roles.get(0).roleIcon + " role!\n" +
                                "You can check your progress with `suzu rank` in <#843148981939994624>"
                ).setColor(Main.accentColor);
        MessageUtils.send(msg.getChannel().getId(), embed);
        msg.getMessage().delete().complete();
    }
}
