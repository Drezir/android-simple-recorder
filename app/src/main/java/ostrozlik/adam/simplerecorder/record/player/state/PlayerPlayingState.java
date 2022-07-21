package ostrozlik.adam.simplerecorder.record.player.state;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.time.Duration;
import java.util.Timer;

import ostrozlik.adam.simplerecorder.record.player.PlayerMediator;

public class PlayerPlayingState extends AbstractPlayerState {

    protected PlayerPlayingState(MediaPlayer mediaPlayer, Timer timer, PlayerMediator playerMediator) {
        super(mediaPlayer, timer, playerMediator);
    }

    @Override
    public PlayerState play(Context context, Uri uri, Duration duration) {
        this.mediaPlayer.pause();
        this.timer.cancel();
        return new PlayerPauseState(this.mediaPlayer, this.timer, this.playerMediator);
    }

    @Override
    public PlayerState stop() {
        this.mediaPlayer.release();
        this.timer.cancel();
        this.playerMediator.seekTo(0);
        return new PlayerStopState(this.playerMediator);
    }
}
