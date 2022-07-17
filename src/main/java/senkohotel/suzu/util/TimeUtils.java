package senkohotel.suzu.util;

public class TimeUtils {
    // hours, minutes, seconds
    public static String formatHMS(long time) {
        return (int) (time / 3600000) + " hours, " + (int) (time % 3600000 / 60000) + " minutes, " + (int) (time % 60000 / 1000) + " seconds";
    }

    // minutes, seconds
    public static String formatMS(long time) {
        return (int) (time % 3600000 / 60000) + " minutes, " + (int) (time % 60000 / 1000) + " seconds";
    }
}
