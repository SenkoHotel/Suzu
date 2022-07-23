package senkohotel.suzu.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import senkohotel.hotelbot.commands.Command;
import senkohotel.suzu.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class NewRankCommand extends Command {
    public NewRankCommand() {
        name = "rank2";
        hidden = true;
    }

    @Override
    public void exec(MessageReceivedEvent msg, String[] args) throws Exception {
        super.exec(msg, args);

        try {
            BufferedImage img = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();

            BufferedImage bg = ImageIO.read(new URL("https://cdn.discordapp.com/attachments/991066755197722665/998225768024916169/rankBanner.png"));
            g.drawImage(bg, 0, 0, null);

            BufferedImage avatar = ImageIO.read(new URL(msg.getAuthor().getEffectiveAvatarUrl() + "?size=256"));
            g.drawImage(ImageUtils.makeRoundedCorner(avatar, 10), 20, 20, null);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Quicksand", Font.BOLD, 64));
            g.drawString(msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator(), 20, 30 + 32);

            g.dispose();

            File file = new File("rank.png");
            ImageIO.write(img, "png", file);

            msg.getChannel().sendFile(file).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
