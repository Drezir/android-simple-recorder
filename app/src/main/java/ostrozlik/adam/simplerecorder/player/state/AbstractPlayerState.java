package ostrozlik.adam.simplerecorder.player.state;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import ostrozlik.adam.simplerecorder.player.PlayerMediator;

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

    protected void scheduleTimeTask(Timer timer, MediaPlayer mediaPlayer, PlayerMediator playerMediator) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    playerMediator.seekTo(mediaPlayer.getCurrentPosition());
                } catch (IllegalStateException ignored) {
                }
            }
        }, 0L, Duration.ofMillis(500L).toMillis());
    }

    protected void purgeTimer(Timer timer) {
        timer.cancel();
        timer.purge();
    }

    protected void stopCommon() {
        purgeTimer(this.timer);
        this.mediaPlayer.release();
        this.playerMediator.release();
    }
}
