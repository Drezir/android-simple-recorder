package ostrozlik.adam.simplerecorder.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import ostrozlik.adam.simplerecorder.R;
import ostrozlik.adam.simplerecorder.settings.SettingsActivity;
import ostrozlik.adam.simplerecorder.settings.SettingsFragment;
import ostrozlik.adam.simplerecorder.record.FsRecord;
import ostrozlik.adam.simplerecorder.record.RecordListAdapter;
import ostrozlik.adam.simplerecorder.record.manager.RecordManager;
import ostrozlik.adam.simplerecorder.record.manager.RecordManagerImpl;
import ostrozlik.adam.simplerecorder.recorder.RecorderMediator;
import ostrozlik.adam.simplerecorder.recorder.RecorderState;
import ostrozlik.adam.simplerecorder.recorder.state.RecorderStopState;

public class Dashboard extends AppCompatActivity implements RecorderMediator {

    private static final int RECORD_AUDIO_REQUEST_CODE = 1;

    private FloatingActionButton recordButton;
    private FloatingActionButton stopButton;
    private RecorderState recorderState;
    private RecordManager recordManager;

    private RecordListAdapter recordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.recordButton = findViewById(R.id.recordButton);
        this.stopButton = findViewById(R.id.stopButton);

        this.recordManager = RecordManagerImpl.newFsInstance(getSaveDirectory(), this);
        this.recordListAdapter = new RecordListAdapter(recordManager, this, this::runOnUiThread);

        ExpandableListView recordListView = findViewById(R.id.recordsListView);
        recordListView.setAdapter(this.recordListAdapter);
        recordListView.setOnGroupExpandListener(new PreviousGroupCollapseListener(recordListView));

        this.recordButton.setOnClickListener(new RecordButtonStateListener());
        this.stopButton.setOnClickListener(new StopButtonStateListener());
        this.recorderState = new RecorderStopState(this, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            showSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.recorderState = this.recorderState.handleRecord(this);
        } else {
            Snackbar.make(findViewById(R.id.recordButton), "Permission to record audio is mandatory for this app", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void startRecording() {
        this.stopButton.setVisibility(View.VISIBLE);
        this.recordButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.pause_icon));
    }

    @Override
    public void pauseRecording() {
        this.recordButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mic_icon));
    }

    @Override
    public void resumeRecording() {
        startRecording();
    }

    @Override
    public void stopRecording(Path outputFile) {
        this.stopButton.setVisibility(View.GONE);
        this.recordButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mic_icon));
        try {
            this.recordManager.newRecordedFile(FsRecord.newFsInstance(outputFile));
        } catch (IOException e) {
            Log.e("record-read", "Error reading record", e);
        }
    }

    @Override
    public File getSaveDirectory() {
        return getApplicationContext().getFilesDir();
    }

    @Override
    public boolean ensurePermissionToRecord() {
        String permissionToRecordAudio = Manifest.permission.RECORD_AUDIO;
        if (ContextCompat.checkSelfPermission(this, permissionToRecordAudio) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions(this, new String[]{permissionToRecordAudio}, Dashboard.RECORD_AUDIO_REQUEST_CODE);
        return false;
    }

    @Override
    public void recordsChanged() {
        this.recordListAdapter.notifyDataSetChanged();
    }

    private class RecordButtonStateListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Dashboard.this.recorderState = Dashboard.this.recorderState.handleRecord(Dashboard.this);
        }
    }

    private class StopButtonStateListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Dashboard.this.recorderState = Dashboard.this.recorderState.handleStop();
        }
    }

}
