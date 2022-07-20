package ostrozlik.adam.simplerecorder.recorder.state;

import android.media.MediaRecorder;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import ostrozlik.adam.simplerecorder.record.constant.RecordExtension;
import ostrozlik.adam.simplerecorder.record.constant.RecordNameFormat;
import ostrozlik.adam.simplerecorder.recorder.RecorderMediator;
import ostrozlik.adam.simplerecorder.recorder.RecorderState;

public class RecorderStopState extends AbstractRecorderState {

    public RecorderStopState(RecorderMediator recorderMediator, MediaRecorderWrapper mediaRecorderWrapper) {
        super(recorderMediator, mediaRecorderWrapper);
    }

    @Override
    public RecorderState handleRecord() {
        if (this.recorderMediator.ensurePermissionToRecord()) {
            this.recorderMediator.startRecording();
            return createRecordingState();
        }
        return this;
    }

    private RecorderState createRecordingState() {
        try {
            MediaRecorderWrapper newMediaRecorderWrapper = newMediaRecorder();
            newMediaRecorderWrapper.start();
            return new RecorderRecordingState(this.recorderMediator, newMediaRecorderWrapper);
        } catch (IOException e) {
            Log.e("media-recorder", "Error creating new media recorder", e);
            return this;
        }
    }

    private MediaRecorderWrapper newMediaRecorder() throws IOException {
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        File file = resolveOutputFile();
        mediaRecorder.setOutputFile(file);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.prepare();
        return new MediaRecorderWrapper(mediaRecorder, file.toPath());
    }

    @NonNull
    private File resolveOutputFile() {
        String extension = RecordExtension.resolveExtension(MediaRecorder.OutputFormat.MPEG_4);
        String fileName = RecordNameFormat.STANDARD.formatDate(LocalDateTime.now()) + "." + extension;
        return new File(this.recorderMediator.getSaveDirectory(), fileName);
    }
}
