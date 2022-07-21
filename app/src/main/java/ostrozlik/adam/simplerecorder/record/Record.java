package ostrozlik.adam.simplerecorder.record;

import java.time.Duration;
import java.time.Instant;

import ostrozlik.adam.simplerecorder.record.constant.RecordExtension;

public interface Record {

    String getName();

    Duration getDuration();

    Instant getCreationTime();

    long getSizeInBytes();

    RecordExtension getRecordExtension();

    void rename(String newName);
}
