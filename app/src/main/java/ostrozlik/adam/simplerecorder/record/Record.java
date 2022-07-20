package ostrozlik.adam.simplerecorder.record;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Record {

    private final String name;
    private final Duration duration;
    private final Instant creationTime;
    private final long sizeInBytes;

    private Record(String name, Duration duration, Instant creationTime, long sizeInBytes) {
        this.name = name;
        this.duration = duration;
        this.creationTime = creationTime;
        this.sizeInBytes = sizeInBytes;
    }

    public static Record newInstance(Path filePath) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(filePath)) {
            return new Record(
                    filePath.getFileName().toString(),
                    resolveDuration(filePath),
                    resolveCreationTime(filePath),
                    fileChannel.size()
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

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return sizeInBytes == record.sizeInBytes && Objects.equals(name, record.name) && Objects.equals(duration, record.duration) && Objects.equals(creationTime, record.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration, creationTime, sizeInBytes);
    }
}
