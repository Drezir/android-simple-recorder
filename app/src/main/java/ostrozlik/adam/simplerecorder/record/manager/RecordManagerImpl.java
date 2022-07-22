package ostrozlik.adam.simplerecorder.record.manager;

import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ostrozlik.adam.simplerecorder.record.FsRecord;
import ostrozlik.adam.simplerecorder.record.Record;
import ostrozlik.adam.simplerecorder.recorder.RecorderMediator;
import ostrozlik.adam.simplerecorder.storage.FSRecordStorage;
import ostrozlik.adam.simplerecorder.storage.RecordStorage;

public class RecordManagerImpl implements RecordManager {

    private final List<Record> records;
    private final RecordStorage recordStorage;
    private final RecorderMediator recorderMediator;

    private RecordManagerImpl(List<Record> records, RecordStorage recordStorage, RecorderMediator recorderMediator) {
        this.records = new ArrayList<>(records);
        this.recordStorage = recordStorage;
        this.recorderMediator = recorderMediator;
    }

    public static RecordManagerImpl newFsInstance(File recordsDir, RecorderMediator recorderMediator) {
        List<Record> records = new ArrayList<>();
        RecordStorage recordStorage = new FSRecordStorage(recordsDir.toPath());
        try {
            records = recordStorage.findRecords();
        } catch (IOException e) {
            Log.e("record-read", "Error walking files in directory " + recordsDir, e);
        }
        return new RecordManagerImpl(records, recordStorage, recorderMediator);
    }

    @Override
    public List<Record> getRecords() {
        return Collections.unmodifiableList(this.records);
    }

    @Override
    public Record findByIndex(int position) {
        return this.records.get(position);
    }

    @Override
    public void newRecordedFile(FsRecord newRecord) {
        Record savedRecord = this.recordStorage.saveRecordedFile(newRecord);
        this.records.add(savedRecord);
        this.recorderMediator.recordsChanged();
    }

    @Override
    public boolean renameRecord(Record record, String text) {
        if (!text.trim().isEmpty()) {
            if (this.recordStorage.renameRecord(record, text)) {
                record.rename(text);
                this.recorderMediator.recordsChanged();
                return true;
            }
        }
        return false;
    }

    @Override
    public Uri uriToPlay(Record record) {
        return this.recordStorage.resolveUriToPlayRecordFrom(record);
    }

    @Override
    public void deleteRecord(Record record) {
        if (this.recordStorage.removeRecord(record)) {
            this.records.remove(record);
            this.recorderMediator.recordsChanged();
        }
    }
}
