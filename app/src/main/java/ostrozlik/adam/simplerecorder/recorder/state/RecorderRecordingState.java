package ostrozlik.adam.simplerecorder.recorder.state;

import ostrozlik.adam.simplerecorder.recorder.RecorderMediator;
import ostrozlik.adam.simplerecorder.recorder.RecorderState;

public class RecorderRecordingState extends AbstractRecorderState {

    protected RecorderRecordingState(RecorderMediator recorderMediator, MediaRecorderWrapper mediaRecorderWrapper) {
        super(recorderMediator, mediaRecorderWrapper);
    }

    @Override
    public RecorderState handlePause() {
        this.recorderMediator.pauseRecording();
        this.mediaRecorderWrapper.pause();
        return new RecorderPauseState(this.recorderMediator, this.mediaRecorderWrapper);
    }
}
