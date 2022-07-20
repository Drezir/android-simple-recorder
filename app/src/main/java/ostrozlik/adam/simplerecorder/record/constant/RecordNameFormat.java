package ostrozlik.adam.simplerecorder.record.constant;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public enum RecordNameFormat {

    STANDARD("yyyy-MM-dd_HHMMss")
    ;

    private final String format;

    RecordNameFormat(String format) {
        this.format = format;
    }

    public String formatDate(TemporalAccessor temporalAccessor) {
        return DateTimeFormatter.ofPattern(this.format).format(temporalAccessor);
    }
}
