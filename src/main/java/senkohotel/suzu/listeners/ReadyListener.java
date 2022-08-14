package senkohotel.suzu.listeners;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import senkohotel.hotelbot.Main;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        long secs = (System.currentTimeMillis() - Main.startTime) / 1000;
        long ms = (System.currentTimeMillis() - Main.startTime) - (secs * 1000);
        Main.LOG.info("Started in " + secs + "s and " + ms + "ms!");
    }
}
