package ostrozlik.adam.simplerecorder.record.player;

import android.content.Context;
import android.net.Uri;
import android.widget.SeekBar;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import ostrozlik.adam.simplerecorder.record.player.state.PlayerPlayingState;
import ostrozlik.adam.simplerecorder.record.player.state.PlayerState;
import ostrozlik.adam.simplerecorder.record.player.state.PlayerStopState;

public class RecordPlayerManager implements PlayerMediator {
    private final Context context;
    private SeekBar seekBar;
    private PlayerState playerState;
    private Uri playingRecord;

    private Runnable playingDoneListener;

    public RecordPlayerManager(Context context) {
        this.context = context;
        this.playerState = new PlayerStopState(this);
    }

    public void playOrPause(Uri uri, Duration duration, SeekBar seekBar) throws IOException {
        releaseResources(uri);
        this.seekBar = seekBar;
        this.playingRecord = uri;
        this.playerState = this.playerState.play(this.context, uri, duration);
        this.seekBar.setOnSeekBarChangeListener(createSeekBarListener());
    }

    private void releaseResources(Uri uri) {
        if (isPlayingSomethingElse(uri)) {
            this.playerState = this.playerState.stop();
        }
        if (this.seekBar != null) {
            this.seekBar.setProgress(0);
        }
    }

    private boolean isPlayingSomethingElse(Uri uri) {
        return !uri.equals(playingRecord) && playingRecord != null;
    }

    private SeekBar.OnSeekBarChangeListener createSeekBarListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            private final AtomicBoolean seeking = new AtomicBoolean();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seeking.get()) {
                    RecordPlayerManager.this.playerState.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seeking.set(true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seeking.set(false);
            }
        };
    }

    public void releaseIfPlaying(Uri record) {
        if (record.equals(this.playingRecord)) {
            playingDone();
            this.playingRecord = null;
        }
    }

    public void registerDonePlayingListener(Runnable runnable) {
        this.playingDoneListener = runnable;
    }

    @Override
    public void seekTo(int progress) {
        this.seekBar.setProgress(progress);
    }

    @Override
    public void setMaxSeek(Duration duration) {
        this.seekBar.setMax((int) duration.toMillis());
    }

    @Override
    public void playingDone() {
        this.playerState = this.playerState.stop();
        if (this.playingDoneListener != null) {
            this.playingDoneListener.run();
        }
    }

    public boolean isPlaying() {
        return this.playerState instanceof PlayerPlayingState;
    }
}
