package ostrozlik.adam.simplerecorder.record;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import ostrozlik.adam.simplerecorder.record.constant.RecordExtension;

public class AbstractRecord implements Record {

    private String name;
    private final Duration duration;
    private final Instant creationTime;
    private final long sizeInBytes;
    private final RecordExtension recordExtension;

    protected AbstractRecord(String name, Duration duration, Instant creationTime, long sizeInBytes, RecordExtension recordExtension) {
        this.name = name;
        this.duration = duration;
        this.creationTime = creationTime;
        this.sizeInBytes = sizeInBytes;
        this.recordExtension = recordExtension;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public Instant getCreationTime() {
        return creationTime;
    }

    @Override
    public long getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
    public RecordExtension getRecordExtension() {
        return recordExtension;
    }

    @Override
    public void rename(String newName) {
        this.name = newName;
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
