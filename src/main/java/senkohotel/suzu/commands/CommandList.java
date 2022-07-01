package senkohotel.suzu.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.suzu.Main;
import senkohotel.suzu.commands.list.RankCommand;
import senkohotel.suzu.commands.list.TatsuImportCommand;

import java.util.Arrays;
import java.util.TreeMap;

public class CommandList {

    static TreeMap<String, Command> commands = new TreeMap<>();

    public static void initList() {
        // commands.put("<name>", new <CommandClass>());
        commands.put("rank", new RankCommand());
        commands.put("tatsuimport", new TatsuImportCommand());
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
