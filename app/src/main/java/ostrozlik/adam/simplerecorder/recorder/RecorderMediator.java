package ostrozlik.adam.simplerecorder.recorder;

import java.io.File;
import java.nio.file.Path;

public interface RecorderMediator {
    /**
     * Start recording.
     */
    void startRecording();

    /**
     * Pause recording.
     */
    void pauseRecording();

    /**
     * Resume recording.
     */
    void resumeRecording();

    /**
     * Stop recording.
     *
     * @param outputFile output file
     */
    void stopRecording(Path outputFile);

    /**
     * Resolve directory to save records.
     *
     * @return directory for records
     */
    File getSaveDirectory();

    /**
     * Evaluate if there is a permission granted for audio record.
     *
     * @return true if authorized, false otherwise
     */
    boolean ensurePermissionToRecord();

}
