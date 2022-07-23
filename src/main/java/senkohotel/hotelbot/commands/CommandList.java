package senkohotel.hotelbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.reflections.Reflections;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.command.HelpCommand;

import java.util.*;

public class CommandList {

    static TreeMap<String, Command> commands = new TreeMap<>();

    public static void initList() {
        Reflections reflections = new Reflections("senkohotel");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> cmd : classes) {
            try {
                addCommand(cmd.getConstructor().newInstance());
            } catch (Exception e) {
                System.out.println("Couldn't add command " + cmd.getName());
                e.printStackTrace();
            }
        }
    }

    static void addCommand (Command cmd) {
        if (cmd.name.equals("")) // dont add it since it doesnt have a way to use it
            return;

        commands.put(cmd.name, cmd);
        System.out.println("Added command " + cmd.name);
    }

    public static void check(MessageReceivedEvent msg, String prefix) {
        String[] args = msg.getMessage().getContentRaw().substring(prefix.length()).split(" ");

        if (args.length > 0)
            exec(msg, args);
    }

    static void exec(MessageReceivedEvent msg, String[] split) {
        if (commands.containsKey(split[0])) {
            String[] args = Arrays.copyOfRange(split, 1, split.length);
            commands.get(split[0]).exec(msg, args);
        }
    }

    public static TreeMap<String, Command> getCommands() {
        return commands;
    }
}
