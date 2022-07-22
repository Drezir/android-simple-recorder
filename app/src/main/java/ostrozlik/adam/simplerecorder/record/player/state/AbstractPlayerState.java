package ostrozlik.adam.simplerecorder.record.player.state;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import ostrozlik.adam.simplerecorder.record.player.PlayerMediator;

public abstract class AbstractPlayerState implements PlayerState {

    protected final MediaPlayer mediaPlayer;
    protected final Timer timer;
    protected final PlayerMediator playerMediator;

    protected AbstractPlayerState(MediaPlayer mediaPlayer, Timer timer, PlayerMediator playerMediator) {
        this.mediaPlayer = mediaPlayer;
        this.timer = timer;
        this.playerMediator = playerMediator;
    }

    @Override
    public PlayerState play(Context context, Uri uri, Duration duration, PlayerMediator playerMediator) {
        return this;
    }

    @Override
    public PlayerState stop() {
        return this;
    }

    @Override
    public void seekTo(int progress) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.seekTo(progress);
        }
    }

    protected void scheduleTimeTask(Timer timer, MediaPlayer mediaPlayer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    AbstractPlayerState.this.playerMediator.seekTo(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0L, Duration.ofSeconds(1L).toMillis());
    }

    protected void stopCommon() {
        this.mediaPlayer.release();
        this.timer.cancel();
        this.timer.purge();
        this.playerMediator.release();
    }
}
