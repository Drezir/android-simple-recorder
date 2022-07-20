package ostrozlik.adam.simplerecorder.record;

import java.util.List;

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
    void insertRecord(Record newInstance);
}
