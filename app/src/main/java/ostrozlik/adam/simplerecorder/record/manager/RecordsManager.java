package ostrozlik.adam.simplerecorder.record.manager;

import java.util.List;

import ostrozlik.adam.simplerecorder.record.FsRecord;
import ostrozlik.adam.simplerecorder.record.Record;

public interface RecordsManager {
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
    Record recordAtPosition(int position);

    /**
     * Insert new record.
     *
     * @param newInstance new record
     */
    void newRecordedFile(FsRecord newInstance);

    /**
     * Delete record at index.
     *
     * @param index record index
     * @return
     */
    boolean deleteAtPosition(int index);

    /**
     * Rename record at position to given text. Blank text is considered invalid and method returns false.
     *
     * @param index record index
     * @param text  record name
     * @return true if renamed, false otherwise
     */
    boolean renameRecord(int index, String text);
}
