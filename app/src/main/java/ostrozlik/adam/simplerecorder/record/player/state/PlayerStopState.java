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
    public PlayerStopState() {
        super(null, null, null);
    }

    @Override
    public PlayerState play(Context context, Uri uri, Duration duration, PlayerMediator playerMediator) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, uri);
        } catch (IOException e) {
            Log.e("record-play", "Error playing record " + uri, e);
            return this;
        }
        Timer timer = new Timer();
        initMediaPlayer(mediaPlayer, playerMediator, timer);
        playerMediator.setMaxSeek(duration);
        playerMediator.startPlaying();
        return new PlayerPlayingState(mediaPlayer, timer, playerMediator);
    }

    private void initMediaPlayer(MediaPlayer mediaPlayer, PlayerMediator playerMediator, Timer timer) {
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        mediaPlayer.setOnCompletionListener(mp -> {
            timer.cancel();
            mp.release();
            playerMediator.release();
        });
    }
}
