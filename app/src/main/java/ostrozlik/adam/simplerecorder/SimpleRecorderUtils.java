package ostrozlik.adam.simplerecorder;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Locale;

import ostrozlik.adam.simplerecorder.record.constant.RecordExtension;

public final class SimpleRecorderUtils {
    private SimpleRecorderUtils() {

    }

    public static String formatDurationWithoutHours(Duration duration) {
        String withHours = formatDurationWithHours(duration);
        return withHours.substring(withHours.indexOf(":") + 1);
    }

    public static String formatDurationWithHours(Duration duration) {
        long seconds = duration.getSeconds();
        return String.format(Locale.getDefault(),
                "%d:%02d:%02d",
                seconds / 3600,
                (seconds % 3600) / 60,
                seconds % 60);
    }

    public static RecordExtension resolveExtension(Path filePath) {
        String filePathStr = filePath.toString();
        String filePathExtension = filePathStr.substring(filePathStr.lastIndexOf(".") + 1);
        return RecordExtension.resolveExtension(filePathExtension);
    }
}
