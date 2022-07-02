package senkohotel.hotelbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Command {
    public Command() {}

    public void exec(MessageReceivedEvent msg, String[] args) {
        msg.getChannel().sendTyping().complete();
    }

    public boolean hasArgument (String arg, String[] args) {
        for (String a : args) {
            if (a.equals(arg))
                return true;
        }

        return false;
    }
}