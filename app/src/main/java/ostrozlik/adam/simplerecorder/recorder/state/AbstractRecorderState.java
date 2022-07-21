package ostrozlik.adam.simplerecorder.recorder.state;

import ostrozlik.adam.simplerecorder.recorder.RecorderMediator;
import ostrozlik.adam.simplerecorder.recorder.RecorderState;

public abstract class AbstractRecorderState implements RecorderState {
    protected final RecorderMediator recorderMediator;
    protected final MediaRecorderWrapper mediaRecorderWrapper;

    protected AbstractRecorderState(RecorderMediator recorderMediator, MediaRecorderWrapper mediaRecorderWrapper) {
        this.recorderMediator = recorderMediator;
        this.mediaRecorderWrapper = mediaRecorderWrapper;
    }

    @Override
    public RecorderState handleStop() {
        this.mediaRecorderWrapper.stop();
        this.recorderMediator.stopRecording(this.mediaRecorderWrapper.getOutputFile());
        return new RecorderStopState(this.recorderMediator, this.mediaRecorderWrapper);
    }

    @Override
    public RecorderState handleRecord() {
        return this;
    }

    @Override
    public RecorderState handlePause() {
        return this;
    }
}
