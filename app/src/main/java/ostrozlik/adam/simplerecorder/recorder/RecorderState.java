package ostrozlik.adam.simplerecorder.recorder;

import android.content.Context;

public interface RecorderState {
    RecorderState handleStop();
    RecorderState handleRecord(Context context);
}
