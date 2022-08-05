package ostrozlik.adam.simplerecorder.player;

import java.time.Duration;

public interface PlayerMediator {
    void seekTo(int progress);

    void setMaxSeek(Duration duration);

    void release();

    void startPlaying();

    void pause();
}
