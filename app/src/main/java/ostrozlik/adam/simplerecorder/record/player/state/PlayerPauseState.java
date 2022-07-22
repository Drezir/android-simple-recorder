package ostrozlik.adam.simplerecorder.record.player.state;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.time.Duration;
import java.util.Timer;

import ostrozlik.adam.simplerecorder.record.player.PlayerMediator;

public class PlayerPauseState extends AbstractPlayerState {
    protected PlayerPauseState(MediaPlayer mediaPlayer, Timer timer, PlayerMediator playerMediator) {
        super(mediaPlayer, timer, playerMediator);
    }

    @Override
    public PlayerState play(Context context, Uri uri, Duration duration, PlayerMediator playerMediator) {
        this.mediaPlayer.start();
        playerMediator.startPlaying();
        return new PlayerPlayingState(this.mediaPlayer, this.timer, playerMediator);
    }

    @Override
    public PlayerState stop() {
        stopCommon();
        return new PlayerStopState();
    }
}
