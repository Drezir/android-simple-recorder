package ostrozlik.adam.simplerecorder.record.manager;

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

public class RecordsManagerImpl implements RecordsManager {

    private final List<Record> records;
    private final RecordStorage recordStorage;
    private final RecorderMediator recorderMediator;

    private RecordsManagerImpl(List<Record> records, RecordStorage recordStorage, RecorderMediator recorderMediator) {
        this.records = new ArrayList<>(records);
        this.recordStorage = recordStorage;
        this.recorderMediator = recorderMediator;
    }

    public static RecordsManagerImpl newFsInstance(File recordsDir, RecorderMediator recorderMediator) {
        List<Record> records = new ArrayList<>();
        RecordStorage recordStorage = new FSRecordStorage(recordsDir.toPath());
        try {
            records = recordStorage.findRecords();
        } catch (IOException e) {
            Log.e("record-read", "Error walking files in directory " + recordsDir, e);
        }
        return new RecordsManagerImpl(records, recordStorage, recorderMediator);
    }

    @Override
    public List<Record> getRecords() {
        return Collections.unmodifiableList(this.records);
    }

    @Override
    public Record recordAtPosition(int position) {
        return this.records.get(position);
    }

    @Override
    public void newRecordedFile(FsRecord newRecord) {
        Record savedRecord = this.recordStorage.saveRecordedFile(newRecord);
        this.records.add(savedRecord);
        this.recorderMediator.recordsChanged();
    }

    @Override
    public boolean deleteAtPosition(int index) {
        if (0 <= index && index < this.records.size()) {
            Record record = this.records.get(index);
            if (this.recordStorage.removeRecord(record)) {
                this.records.remove(index);
                this.recorderMediator.recordsChanged();
                return true;
            }
        }
        return false;
    }
}
