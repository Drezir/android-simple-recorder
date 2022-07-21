package ostrozlik.adam.simplerecorder.record;

import java.time.Duration;
import java.time.Instant;

public interface Record {

    String getName();

    Duration getDuration();

    Instant getCreationTime();

    long getSizeInBytes();
}
