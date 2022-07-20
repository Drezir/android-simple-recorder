package ostrozlik.adam.simplerecorder.record;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ostrozlik.adam.simplerecorder.record.constant.RecordExtension;

public class RecordsManagerImpl implements RecordsManager {

    private final List<Record> records;

    private RecordsManagerImpl(List<Record> records) {
        this.records = new ArrayList<>(records);
    }

    public static RecordsManagerImpl newInstance(File recordsDir) {
        List<Record> records = Collections.emptyList();
        try {
            records = Files.walk(recordsDir.toPath())
                    .filter(RecordsManagerImpl::isFile)
                    .filter(RecordExtension::supports)
                    .map(RecordsManagerImpl::readFileOrEmpty)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            Log.e("record-read", "Error walking files in directory " + recordsDir, e);
        }
        return new RecordsManagerImpl(records);
    }

    private static boolean isFile(Path path) {
        return path.toFile().isFile();
    }

    private static Record readFileOrEmpty(Path filePath) {
        try {
            return Record.newInstance(filePath);
        } catch (Exception e) {
            Log.e("record-read", "Error reading file " + filePath, e);
            return null;
        }
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
    public void insertRecord(Record newRecord) {
        this.records.add(newRecord);
    }
}
