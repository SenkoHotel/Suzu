package senkohotel.suzu.command;

import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.Main;
import senkohotel.hotelbot.commands.Command;
import senkohotel.hotelbot.utils.MessageUtils;
import senkohotel.suzu.util.DBUtils;
import senkohotel.suzu.xp.XPCollection;
import senkohotel.suzu.xp.XPUser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class TatsuImportCommand extends Command {
    public TatsuImportCommand() {
        super();
        name = "tatsuimport";
        desc = "Imports your tatsu score to xp. (so you dont loose progress)";
        hidden = false;
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        boolean ignorexp = hasArgument("--ignore-xp", args);
        boolean newuser = hasArgument("--new-user", args);
        boolean forceThrow = hasArgument("--force-throw", args);

        EmbedBuilder embed = new EmbedBuilder().setColor(Main.accentColor);

        try {
            if (forceThrow)
                throw new Exception("Debug Throw");

            HttpRequest req = HttpRequest.newBuilder().uri(new URI("https://api.tatsu.gg/v1/guilds/791993321374613514/rankings/members/" + msg.getMember().getId() + "/all")).GET().header("Authorization", JsonParser.parseString(Files.readString(Path.of("config/suzu.json"))).getAsJsonObject().get("tatsuAPI").getAsString()).build();
            HttpResponse<String> client = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());

            int score = JsonParser.parseString(client.body()).getAsJsonObject().get("score").getAsInt();

            if (XPCollection.users.containsKey(msg.getAuthor().getId()) && !newuser) {
                if (score > XPCollection.users.get(msg.getAuthor().getId()).xp || ignorexp) {
                    XPCollection.users.get(msg.getAuthor().getId()).setXP(score);
                    DBUtils.updateXP(score, msg.getAuthor().getId());
                    embed.setTitle("Done!");
                    embed.setTitle("Imported " + score + "XP from tatsu!");
                } else {
                    embed.setTitle("You already have more XP than the tatsu api!");
                }
            } else {
                XPUser newUser = new XPUser();
                newUser.setXP(score);
                DBUtils.insertNewUser(score, msg.getAuthor().getId(), msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator());
                XPCollection.users.put(msg.getAuthor().getId(), newUser);
                embed.setTitle("Done!");
                embed.setTitle("Imported " + score + "XP from tatsu!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            embed.setTitle("Something went wrong while importing!");
            embed.setColor(0xFF5555);
        }

        MessageUtils.reply(msg, embed);
    }
}
