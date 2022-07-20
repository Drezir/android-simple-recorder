package ostrozlik.adam.simplerecorder;

import android.text.format.Formatter;

import java.time.Duration;
import java.util.Locale;

public final class SimpleRecorderUtils {
    private SimpleRecorderUtils() {

    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        return String.format(Locale.getDefault(),
                "%d:%02d:%02d",
                seconds / 3600,
                (seconds % 3600) / 60,
                seconds % 60);
    }
}
