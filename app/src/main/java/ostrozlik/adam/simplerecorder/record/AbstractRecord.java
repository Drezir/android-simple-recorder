package ostrozlik.adam.simplerecorder.record;

import android.media.MediaMetadataRetriever;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class AbstractRecord implements Record {

    private final String name;
    private final Duration duration;
    private final Instant creationTime;
    private final long sizeInBytes;

    protected AbstractRecord(String name, Duration duration, Instant creationTime, long sizeInBytes) {
        this.name = name;
        this.duration = duration;
        this.creationTime = creationTime;
        this.sizeInBytes = sizeInBytes;
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
        AbstractRecord record = (AbstractRecord) o;
        return sizeInBytes == record.sizeInBytes && Objects.equals(name, record.name) && Objects.equals(duration, record.duration) && Objects.equals(creationTime, record.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration, creationTime, sizeInBytes);
    }
}
