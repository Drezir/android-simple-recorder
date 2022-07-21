package ostrozlik.adam.simplerecorder.record;

import android.media.MediaMetadataRetriever;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;

public class FsRecord extends AbstractRecord {

    private final Path filePath;

    public FsRecord(String name, Duration duration, Instant creationTime, long sizeInBytes, Path filePath) {
        super(name, duration, creationTime, sizeInBytes);
        this.filePath = filePath;
    }

    public Path getFilePath() {
        return filePath;
    }

    public static FsRecord newFsInstance(Path filePath) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(filePath)) {
            return new FsRecord(
                    filePath.getFileName().toString(),
                    resolveDuration(filePath),
                    resolveCreationTime(filePath),
                    fileChannel.size(),
                    filePath
            );
        }
    }

    private static Duration resolveDuration(Path filePath) {
        try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
            mmr.setDataSource(filePath.toString());
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long millis = Long.parseLong(duration);
            return Duration.ofMillis(millis);
        }
    }

    private static Instant resolveCreationTime(Path filePath) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);
        return basicFileAttributes.creationTime().toInstant();
    }
}
