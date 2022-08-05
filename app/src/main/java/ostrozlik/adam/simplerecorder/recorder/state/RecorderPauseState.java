package ostrozlik.adam.simplerecorder.recorder.state;

import android.content.Context;

import java.util.function.Consumer;

import ostrozlik.adam.simplerecorder.recorder.RecorderMediator;
import ostrozlik.adam.simplerecorder.recorder.RecorderState;

public class RecorderPauseState extends AbstractRecorderState {

    protected RecorderPauseState(RecorderMediator recorderMediator, MediaRecorderWrapper mediaRecorderWrapper) {
        super(recorderMediator, mediaRecorderWrapper);
    }

    @Override
    public RecorderState handleRecord(Context context) {
        this.mediaRecorderWrapper.resume();
        this.recorderMediator.resumeRecording();
        return new RecorderRecordingState(this.recorderMediator, this.mediaRecorderWrapper);
    }
}
