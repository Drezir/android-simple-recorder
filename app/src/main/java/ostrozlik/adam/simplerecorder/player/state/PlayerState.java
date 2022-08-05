package ostrozlik.adam.simplerecorder.player.state;

import android.content.Context;
import android.net.Uri;

import java.time.Duration;

import ostrozlik.adam.simplerecorder.player.PlayerMediator;

public interface PlayerState {
    PlayerState play(Context context, Uri uri, Duration duration, PlayerMediator playerMediator);
    PlayerState stop();

    void seekTo(int progress);
}
