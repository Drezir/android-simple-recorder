package ostrozlik.adam.simplerecorder.recorder.state;

import ostrozlik.adam.simplerecorder.recorder.RecorderMediator;
import ostrozlik.adam.simplerecorder.recorder.RecorderState;

public class RecorderPauseState extends AbstractRecorderState {

    protected RecorderPauseState(RecorderMediator recorderMediator, MediaRecorderWrapper mediaRecorderWrapper) {
        super(recorderMediator, mediaRecorderWrapper);
    }

    @Override
    public RecorderState handleRecord() {
        this.mediaRecorderWrapper.resume();
        this.recorderMediator.resumeRecording();
        return new RecorderRecordingState(this.recorderMediator, this.mediaRecorderWrapper);
    }
}
