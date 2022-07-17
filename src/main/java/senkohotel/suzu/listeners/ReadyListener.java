package senkohotel.suzu.listeners;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import senkohotel.hotelbot.Main;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        long secs = (System.currentTimeMillis() - Main.startTime.getTime()) / 1000;
        long ms = (System.currentTimeMillis() - Main.startTime.getTime()) - (secs * 1000);
        System.out.println("Started in " + secs + "s and " + ms + "ms!");
    }
}
