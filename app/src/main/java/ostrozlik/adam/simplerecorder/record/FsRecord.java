package ostrozlik.adam.simplerecorder.record;

import android.media.MediaMetadataRetriever;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import ostrozlik.adam.simplerecorder.record.constant.RecordExtension;

public class FsRecord extends AbstractRecord {

    private final Path filePath;

    public FsRecord(String name, Duration duration, Instant creationTime, long sizeInBytes, Path filePath) {
        super(name, duration, creationTime, sizeInBytes, resolveExtension(filePath));
        this.filePath = filePath;
    }

    private static RecordExtension resolveExtension(Path filePath) {
        String filePathStr = filePath.toString();
        String filePathExtension = filePathStr.substring(filePathStr.lastIndexOf(".") + 1);
        return RecordExtension.resolveExtension(filePathExtension);
    }

    public Path getFilePath() {
        return filePath;
    }

    public static FsRecord newFsInstance(Path filePath) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(filePath)) {
            String fileName = filePath.getFileName().toString();
            String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
            return new FsRecord(
                    nameWithoutExtension,
                    resolveDuration(filePath),
                    resolveCreationTime(filePath),
                    fileChannel.size(),
                    filePath
            );
        }
    }

    private static Duration resolveDuration(Path filePath) {
        try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
            mmr.setDataSource(filePath.toString());
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long millis = Long.parseLong(duration);
            return Duration.ofMillis(millis);
        }
    }

    private static Instant resolveCreationTime(Path filePath) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);
        return basicFileAttributes.creationTime().toInstant();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FsRecord fsRecord = (FsRecord) o;
        return Objects.equals(filePath, fsRecord.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), filePath);
    }
}
