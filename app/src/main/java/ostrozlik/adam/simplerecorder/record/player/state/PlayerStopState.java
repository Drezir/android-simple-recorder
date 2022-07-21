package ostrozlik.adam.simplerecorder.record.player.state;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.time.Duration;
import java.util.Timer;

import ostrozlik.adam.simplerecorder.record.player.PlayerMediator;

public class PlayerStopState extends AbstractPlayerState {
    public PlayerStopState(PlayerMediator playerMediator) {
        super(null, null, playerMediator);
    }

    @Override
    public PlayerState play(Context context, Uri uri, Duration duration) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, uri);
        } catch (IOException e) {
            Log.e("record-play", "Error playing record " + uri, e);
            return this;
        }
        initMediaPlayer(duration, mediaPlayer);
        Timer timer = createTimer(mediaPlayer);
        return new PlayerPlayingState(mediaPlayer, timer, this.playerMediator);
    }

    private Timer createTimer(MediaPlayer mediaPlayer) {
        Timer timer = new Timer();
        scheduleTimeTask(timer, mediaPlayer);
        return timer;
    }

    private void initMediaPlayer(Duration duration, MediaPlayer mediaPlayer) {
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());
        mediaPlayer.prepareAsync();
        this.playerMediator.setMaxSeek(duration);
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        mediaPlayer.setOnCompletionListener(mp -> this.playerMediator.playingDone());
    }
}
