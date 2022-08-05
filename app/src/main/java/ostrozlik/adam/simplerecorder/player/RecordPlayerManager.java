package ostrozlik.adam.simplerecorder.player;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.time.Duration;

import ostrozlik.adam.simplerecorder.player.state.PlayerState;
import ostrozlik.adam.simplerecorder.player.state.PlayerStopState;

public class RecordPlayerManager {
    private final Context context;
    private PlayerState playerState;
    public RecordPlayerManager(Context context) {
        this.context = context;
        this.playerState = new PlayerStopState();
    }

    public void playOrPause(Uri uri, Duration duration, PlayerMediator playerMediator) throws IOException {
        this.playerState = this.playerState.play(this.context, uri, duration, playerMediator);
    }

    public void playingDone() {
        this.playerState = this.playerState.stop();
    }

    public void reset() {
        this.playerState = new PlayerStopState();
    }

    public void seekTo(int progress) {
        this.playerState.seekTo(progress);
    }
}
