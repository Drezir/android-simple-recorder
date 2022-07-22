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
        scheduleTimeTask(timer, mediaPlayer);
    }

    @Override
    public PlayerState play(Context context, Uri uri, Duration duration, PlayerMediator playerMediator) {
        this.mediaPlayer.pause();
        this.timer.cancel();
        playerMediator.pause();
        return new PlayerPauseState(this.mediaPlayer, new Timer(), playerMediator);
    }

    @Override
    public PlayerState stop() {
        stopCommon();
        return new PlayerStopState();
    }
}
