package ostrozlik.adam.simplerecorder.record.constant;

import android.media.MediaRecorder.OutputFormat;

import java.nio.file.Path;
import java.util.Arrays;

public enum RecordExtension {

    AAC_ADTS(OutputFormat.AAC_ADTS, "aac"),
    AMR_NB(OutputFormat.AMR_NB, "amr"),
    AMR_WB(OutputFormat.AMR_WB, "amr"),
    MPEG_2_TS(OutputFormat.MPEG_2_TS, "ts"),
    MPEG_4(OutputFormat.MPEG_4, "mp4"),
    OGG(OutputFormat.OGG, "ogg"),
    THREE_GPP(OutputFormat.THREE_GPP, "3gpp"),
    WEBM(OutputFormat.WEBM, "webm");

    private static final RecordExtension[] CACHE = RecordExtension.values();
    private final int mediaOutputFormat;
    private final String extension;

    RecordExtension(int mediaOutputFormat, String extension) {
        this.mediaOutputFormat = mediaOutputFormat;
        this.extension = extension;
    }

    public static boolean supports(Path path) {
        String fileNameString = path.getFileName().toString();
        return Arrays.stream(CACHE).anyMatch(recordExtension -> fileNameString.endsWith("." + recordExtension.getExtension()));
    }

    public static String resolveExtension(int mediaOutputFormat) {
        return Arrays.stream(CACHE)
                .filter(recordExtension -> recordExtension.getMediaOutputFormat() == mediaOutputFormat)
                .findFirst()
                .map(RecordExtension::getExtension)
                .orElseThrow(() -> new IllegalArgumentException("Invalid extension " + mediaOutputFormat));
    }

    public int getMediaOutputFormat() {
        return mediaOutputFormat;
    }

    public String getExtension() {
        return extension;
    }
}
