package senkohotel.hotelbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.Main;

import java.util.Arrays;
import java.util.TreeMap;

public class CommandList {

    static TreeMap<String, Command> commands = new TreeMap<>();

    public static void initList() {
        // commands.put("<name>", new <CommandClass>());
    }

    public static void check(MessageReceivedEvent msg) {
        String[] args = msg.getMessage().getContentRaw().substring(Main.prefix.length()).split(" ");

        if (args.length > 0)
            exec(msg, args);
    }

    static void exec(MessageReceivedEvent msg, String[] split) {
        if (commands.containsKey(split[0])) {
            String[] args = Arrays.copyOfRange(split, 1, split.length);
            commands.get(split[0]).exec(msg, args);
        }
    }
}
