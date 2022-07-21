package ostrozlik.adam.simplerecorder.record.player;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.SeekBar;

import java.io.IOException;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import ostrozlik.adam.simplerecorder.record.Record;
import ostrozlik.adam.simplerecorder.record.manager.RecordManager;

public class RecordPlayerManager {
    private MediaPlayer mediaPlayer;
    private final Context context;
    private Timer timer;
    private Uri playingRecord;

    public RecordPlayerManager(Context context) {
        this.context = context;
    }

    public void playOrPause(Uri uri, Duration duration, SeekBar seekBar) throws IOException {
        if (shouldRelease(uri)) {
            release();
        }
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setDataSource(this.context, uri);
            this.mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());
            this.mediaPlayer.prepareAsync();
            seekBar.setMax((int) duration.toMillis());
            seekBar.setOnSeekBarChangeListener(createSeekBarListener());
            this.mediaPlayer.setOnPreparedListener(mp -> {
                syncSeekBar(seekBar, mp);
                mp.start();
            });
            this.mediaPlayer.setOnCompletionListener(mp -> release());
        } else if (!this.mediaPlayer.isPlaying()) {
            syncSeekBar(seekBar, this.mediaPlayer);
            this.mediaPlayer.start();
        } else {
            pause();
        }
        this.playingRecord = uri;
    }

    private boolean shouldRelease(Uri uri) {
        return this.mediaPlayer != null && !uri.equals(playingRecord) && playingRecord != null;
    }

    private SeekBar.OnSeekBarChangeListener createSeekBarListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            private final AtomicBoolean seeking = new AtomicBoolean();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (RecordPlayerManager.this.mediaPlayer != null && seeking.get()) {
                    RecordPlayerManager.this.mediaPlayer.seekTo(progress);
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

    private void syncSeekBar(SeekBar seekBar, MediaPlayer mp) {
        cancelPreviousTimer();
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mp.getCurrentPosition());
            }
        }, 0L, Duration.ofMillis(500L).toMillis());
    }

    private void cancelPreviousTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    public void pause() {
        cancelPreviousTimer();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.pause();
        }
    }

    public void release() {
        cancelPreviousTimer();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        this.playingRecord = null;
    }

    public void releaseIfPlaying(Uri record) {
        if (record.equals(this.playingRecord)) {
            release();
        }
    }
}
