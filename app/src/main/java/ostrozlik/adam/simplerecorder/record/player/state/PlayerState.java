package ostrozlik.adam.simplerecorder.record.player.state;

import android.content.Context;
import android.net.Uri;

import java.time.Duration;

public interface PlayerState {
    PlayerState play(Context context, Uri uri, Duration duration);
    PlayerState stop();
    void seekTo(int progress);
}
