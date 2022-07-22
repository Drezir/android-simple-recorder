package ostrozlik.adam.simplerecorder.record.manager;

import android.net.Uri;

import java.util.List;

import ostrozlik.adam.simplerecorder.record.FsRecord;
import ostrozlik.adam.simplerecorder.record.Record;

public interface RecordManager {
    /**
     * Get unmodifiable list of records.
     *
     * @return unmodifiable record list
     */
    List<Record> getRecords();

    /**
     * Find record on given position/index.
     *
     * @param position position
     * @return record on position
     */
    Record findByIndex(int position);

    /**
     * Insert new record.
     *
     * @param newInstance new record
     */
    void newRecordedFile(FsRecord newInstance);

    /**
     * Rename record at position to given text. Blank text is considered invalid and method returns false.
     *
     * @param index record index
     * @param text  record name
     * @return true if renamed, false otherwise
     */
    boolean renameRecord(Record index, String text);

    /**
     * Prepare record to play as local file.
     *
     * @param record record to play
     * @return uri to initiate record data stream
     */
    Uri uriToPlay(Record record);

    /**
     * Delete record.
     *
     * @param record record to delete
     */
    void deleteRecord(Record record);
}
