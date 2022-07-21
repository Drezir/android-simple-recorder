package ostrozlik.adam.simplerecorder.storage;

import java.io.IOException;
import java.util.List;

import ostrozlik.adam.simplerecorder.record.FsRecord;
import ostrozlik.adam.simplerecorder.record.Record;

public interface RecordStorage {
    List<Record> findRecords() throws IOException;

    boolean removeRecord(Record record);

    Record saveRecordedFile(FsRecord newRecord);

    boolean renameRecord(Record record, String text);
}
