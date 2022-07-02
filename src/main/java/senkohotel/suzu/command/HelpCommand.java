package senkohotel.suzu.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.suzu.Main;
import senkohotel.suzu.commands.Command;
import senkohotel.suzu.commands.CommandList;
import senkohotel.suzu.utils.MessageUtils;

import java.util.Map;

public class HelpCommand extends Command {
    public HelpCommand() {
        super();
        name = "help";
        hidden = true;
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) {
        super.exec(msg, args);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(Main.bot.getSelfUser().getName() + " help page")
                .setColor(Main.accentColor)
                .setDescription("A list off all commands i can use!");

        for (Map.Entry<String, Command> command : CommandList.getCommands().entrySet()) {
            if (!command.getValue().hidden)
                embed.addField(command.getKey(), command.getValue().desc, true);
        }

        MessageUtils.reply(msg, embed);
    }
}
