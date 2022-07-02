package senkohotel.suzu.commands.list;

import com.google.gson.JsonParser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.suzu.commands.Command;
import senkohotel.suzu.utils.DBUtils;
import senkohotel.suzu.utils.MessageUtils;
import senkohotel.suzu.xp.XPCollection;
import senkohotel.suzu.xp.XPUser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class TatsuImportCommand extends Command {
    @Override
    public void exec(MessageReceivedEvent msg, String[] args) {
        super.exec(msg, args);

        try {
            HttpRequest req =  HttpRequest.newBuilder().uri(new URI("https://api.tatsu.gg/v1/guilds/791993321374613514/rankings/members/" + msg.getMember().getId() + "/all")).GET().header("Authorization", JsonParser.parseString(Files.readString(Path.of("config/suzu.json"))).getAsJsonObject().get("tatsuAPI").getAsString()).build();
            HttpResponse client = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());

            int score = JsonParser.parseString(client.body().toString()).getAsJsonObject().get("score").getAsInt();

            if (XPCollection.users.containsKey(msg.getAuthor().getId())) {
                if (score > XPCollection.users.get(msg.getAuthor().getId()).xp) {
                    XPCollection.users.get(msg.getAuthor().getId()).setXP(score);
                    DBUtils.updateXP(score, msg.getAuthor().getId());
                    MessageUtils.reply(msg, "Imported " + score + "XP from tatsu!");
                } else {
                    MessageUtils.reply(msg, "You already have more XP then the tatsu api!");
                }
            } else {
                XPUser newUser = new XPUser();
                newUser.setXP(score);
                DBUtils.insertNewUser(score, msg.getAuthor().getId());
                XPCollection.users.put(msg.getAuthor().getId(), newUser);
                MessageUtils.reply(msg, "Imported " + score + "XP from tatsu!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtils.reply(msg, "Something went wrong while importing!");
        }
    }
}
