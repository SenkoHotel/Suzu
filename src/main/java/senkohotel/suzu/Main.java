package senkohotel.suzu;

import com.google.gson.JsonParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import senkohotel.suzu.commands.CommandList;
import senkohotel.suzu.listeners.MessageListener;
import senkohotel.suzu.listeners.ReadyListener;
import senkohotel.suzu.xp.XPCollection;

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

    public static void main(String[] args) throws LoginException {
        startTime = Date.from(new Date().toInstant());

        CommandList.initList();
        XPCollection.initRoleList();
        XPCollection.loadUsers();

        JDABuilder jda = JDABuilder.createDefault(loadToken());
        jda.enableIntents(EnumSet.allOf(GatewayIntent.class));
        jda.setRawEventsEnabled(true);
        bot = jda.build();
        bot.addEventListener(new MessageListener());
        bot.addEventListener(new ReadyListener());
    }

    static String loadToken() {
        try {
            return JsonParser.parseString(Files.readString(Path.of("config/suzu.json"))).getAsJsonObject().get("token").getAsString();
        } catch (Exception ex) {
            System.out.println("Failed to load token");
            return "";
        }
    }
}
