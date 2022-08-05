package ostrozlik.adam.simplerecorder.recorder.state;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import ostrozlik.adam.simplerecorder.R;
import ostrozlik.adam.simplerecorder.record.constant.RecordExtension;
import ostrozlik.adam.simplerecorder.record.constant.RecordNameFormat;
import ostrozlik.adam.simplerecorder.recorder.RecorderMediator;
import ostrozlik.adam.simplerecorder.recorder.RecorderState;

public class RecorderStopState extends AbstractRecorderState {

    public RecorderStopState(RecorderMediator recorderMediator, MediaRecorderWrapper mediaRecorderWrapper) {
        super(recorderMediator, mediaRecorderWrapper);
    }

    @Override
    public RecorderState handleRecord(Context context) {
        if (this.recorderMediator.ensurePermissionToRecord()) {
            MediaRecorderWrapper mediaRecorderWrapper = null;
            try {
                mediaRecorderWrapper = newMediaRecorder(context);
                this.recorderMediator.startRecording();
                mediaRecorderWrapper.start();
                return new RecorderRecordingState(this.recorderMediator, mediaRecorderWrapper);
            } catch (Exception e) {
                Log.e("media-recorder", "Error creating new media recorder", e);
                Toast.makeText(context, R.string.cannot_record, Toast.LENGTH_SHORT).show();
                if (mediaRecorderWrapper != null) {
                    mediaRecorderWrapper.clear();
                }
            }
        }
        return this;
    }

    private MediaRecorderWrapper newMediaRecorder(Context context) throws IOException {
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        int format = applyOutputFormatSettings(context, mediaRecorder);
        applyEncoderSettings(context, mediaRecorder);
        File file = newOutputFile(format);
        mediaRecorder.setOutputFile(file);
        mediaRecorder.prepare();
        return new MediaRecorderWrapper(mediaRecorder, file.toPath());
    }

    private int applyOutputFormatSettings(Context context, MediaRecorder mediaRecorder) {
        String formatValueStr = PreferenceManager.getDefaultSharedPreferences(context).getString("audioFormat", String.valueOf(MediaRecorder.OutputFormat.DEFAULT));
        int outputFormat = Integer.parseInt(formatValueStr);
        mediaRecorder.setOutputFormat(outputFormat);
        return outputFormat;
    }

    private void applyEncoderSettings(Context context, MediaRecorder mediaRecorder) {
        String formatValueStr = PreferenceManager.getDefaultSharedPreferences(context).getString("audioEncoder", String.valueOf(MediaRecorder.AudioEncoder.DEFAULT));
        mediaRecorder.setAudioEncoder(Integer.parseInt(formatValueStr));
    }

    @NonNull
    private File newOutputFile(int format) {
        String extension = RecordExtension.resolveExtension(format);
        String fileName = RecordNameFormat.STANDARD.formatDate(LocalDateTime.now()) + "." + extension;
        return new File(this.recorderMediator.getSaveDirectory(), fileName);
    }
}
