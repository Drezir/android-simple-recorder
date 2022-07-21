package ostrozlik.adam.simplerecorder.record.player;

import java.time.Duration;

public interface PlayerMediator {
    void seekTo(int progress);

    void setMaxSeek(Duration duration);

    void playingDone();
}
