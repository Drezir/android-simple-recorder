package ostrozlik.adam.simplerecorder.recorder.state;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MediaRecorderWrapper {
    private final MediaRecorder mediaRecorder;
    private final Path outputFile;

    public MediaRecorderWrapper(MediaRecorder mediaRecorder, Path outputFile) {
        this.mediaRecorder = mediaRecorder;
        this.outputFile = outputFile;
    }

    public void stop() {
        this.mediaRecorder.stop();
        this.mediaRecorder.release();
    }

    public void resume() {
        this.mediaRecorder.resume();
    }

    public void pause() {
        this.mediaRecorder.pause();
    }

    public void start() {
        this.mediaRecorder.start();
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public void clear() {
        try {
            Files.deleteIfExists(outputFile);
        } catch (IOException e) {
            Log.e("fs", "Cannot delete file", e);
        }
    }
}
