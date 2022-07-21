package ostrozlik.adam.simplerecorder.record.player.state;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import ostrozlik.adam.simplerecorder.record.player.PlayerMediator;

public class PlayerPauseState extends AbstractPlayerState {
    protected PlayerPauseState(MediaPlayer mediaPlayer, Timer timer, PlayerMediator playerMediator) {
        super(mediaPlayer, timer, playerMediator);
    }

    @Override
    public PlayerState play(Context context, Uri uri, Duration duration) {
        this.mediaPlayer.start();
        Timer timer = new Timer();
        scheduleTimeTask(timer, this.mediaPlayer);
        return new PlayerPlayingState(this.mediaPlayer, timer, this.playerMediator);
    }

    @Override
    public PlayerState stop() {
        this.mediaPlayer.release();
        this.timer.cancel();
        return new PlayerStopState(this.playerMediator);
    }
}
