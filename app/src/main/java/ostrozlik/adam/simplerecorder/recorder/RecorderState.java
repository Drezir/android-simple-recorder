package ostrozlik.adam.simplerecorder.recorder;

public interface RecorderState {
    RecorderState handleStop();
    RecorderState handleRecord();
    RecorderState handlePause();
}
