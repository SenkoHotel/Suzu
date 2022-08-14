package senkohotel.hotelbot;

import com.google.gson.JsonParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import senkohotel.hotelbot.commands.CommandList;
import senkohotel.hotelbot.commands.SlashCommandList;
import senkohotel.hotelbot.listeners.MessageListener;
import senkohotel.hotelbot.listeners.SlashCommandListener;
import senkohotel.suzu.listeners.ReadyListener;

import javax.security.auth.login.LoginException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.EnumSet;

public class Main {
    public static String[] prefix = {"suzu ", "s."};
    public static JDA bot;
    public static int accentColor = 0xdda389;
    public static Date startTime;
    public static Logger LOG = LoggerFactory.getLogger("suzu");
    public static boolean debug = false; // just adds the slash commands instantly

    public static void main(String[] args) throws LoginException {
        CommandList.initList();

        JDABuilder jda = JDABuilder.createDefault(loadToken());
        jda.enableIntents(EnumSet.allOf(GatewayIntent.class));
        jda.setRawEventsEnabled(true);
        bot = jda.build();
        bot.addEventListener(new MessageListener());
        bot.addEventListener(new ReadyListener());
        bot.addEventListener(new SlashCommandListener());

        SlashCommandList.initGlobal();
    }

    static String loadToken() {
        try {
            return JsonParser.parseString(Files.readString(Path.of("config/suzu.json"))).getAsJsonObject().get("token").getAsString();
        } catch (Exception ex) {
            LOG.error("Failed to load token!");
            return "";
        }
    }
}
