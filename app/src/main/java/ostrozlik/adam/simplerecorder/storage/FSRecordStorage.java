package ostrozlik.adam.simplerecorder.storage;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ostrozlik.adam.simplerecorder.record.FsRecord;
import ostrozlik.adam.simplerecorder.record.Record;
import ostrozlik.adam.simplerecorder.record.constant.RecordExtension;

public class FSRecordStorage implements RecordStorage {

    private final Path directory;

    public FSRecordStorage(Path directory) {
        this.directory = directory;
    }

    @Override
    public List<Record> findRecords() throws IOException {
        return Files.walk(directory)
                .filter(FSRecordStorage::isFile)
                .filter(RecordExtension::supports)
                .map(FSRecordStorage::readFileOrEmpty)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean removeRecord(Record record) {
        if (record instanceof FsRecord) {
            try {
                return Files.deleteIfExists(((FsRecord) record).getFilePath());
            } catch (IOException e) {
                Log.e("record-delete", "Error deleting FS record", e);
            }
        }
        return false;
    }

    @Override
    public Record saveRecordedFile(FsRecord newRecord) {
        return newRecord; // already saved
    }

    @Override
    public boolean renameRecord(Record record, String text) {
        if (record instanceof FsRecord) {
            Path filePath = ((FsRecord) record).getFilePath();
            try {
                Files.move(filePath, filePath.resolveSibling(text + "." + record.getRecordExtension().getExtension()));
                return true;
            } catch (IOException e) {
                Log.e("record-rename", "Error renaming FS record", e);
            }
        }
        return false;
    }

    private static boolean isFile(Path path) {
        return path.toFile().isFile();
    }

    private static FsRecord readFileOrEmpty(Path filePath) {
        try {
            return FsRecord.newFsInstance(filePath);
        } catch (Exception e) {
            Log.e("record-read", "Error reading file " + filePath, e);
            return null;
        }
    }
}
