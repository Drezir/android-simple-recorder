package ostrozlik.adam.simplerecorder.record.player;

import static ostrozlik.adam.simplerecorder.SimpleRecorderUtils.formatDurationWithoutHours;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.time.Duration;

import ostrozlik.adam.simplerecorder.R;

public class PlayerMediatorImpl implements PlayerMediator {

    private final SeekBar playerSeekBar;
    private final TextView passedTimeText;
    private final TextView leftTimeText;
    private final Duration recordDuration;
    private final ImageButton playButton;
    private final Context context;

    public PlayerMediatorImpl(SeekBar playerSeekBar, TextView passedTimeText, TextView leftTimeText, Duration recordDuration, ImageButton playButton, Context context) {
        this.playerSeekBar = playerSeekBar;
        this.passedTimeText = passedTimeText;
        this.leftTimeText = leftTimeText;
        this.recordDuration = recordDuration;
        this.playButton = playButton;
        this.context = context;
    }

    @Override
    public void seekTo(int progress) {
        this.playerSeekBar.setProgress(progress);
        Duration passed = Duration.ofMillis(progress);
        Duration left = recordDuration.minus(passed);
        this.passedTimeText.setText(formatDurationWithoutHours(passed));
        this.leftTimeText.setText(formatDurationWithoutHours(left));
    }

    @Override
    public void setMaxSeek(Duration duration) {
        this.playerSeekBar.setMax((int) duration.toMillis());
    }

    @Override
    public void release() {
        this.playerSeekBar.setProgress(0);
        String zeroText = "00:00";
        this.passedTimeText.setText(zeroText);
        this.leftTimeText.setText(zeroText);
        this.playButton.setImageIcon(Icon.createWithResource(this.context, R.drawable.ic_play));
    }

    @Override
    public void startPlaying() {
        this.playButton.setImageIcon(Icon.createWithResource(this.context, R.drawable.ic_pause));
    }

    @Override
    public void pause() {
        this.playButton.setImageIcon(Icon.createWithResource(this.context, R.drawable.ic_play));
    }
}
