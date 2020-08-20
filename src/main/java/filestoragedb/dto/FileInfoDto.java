package filestoragedb.dto;

import filestoragedb.model.FileInfo;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FileInfoDto {
    private UUID uuid;
    private String name;
    private String type;
    private long size;
    private long uploadTime;
    private long lastChangeTime;
    private String downloadUri;

    public FileInfoDto(UUID uuid, FileInfo fileInfo) {
        this.uuid = uuid;
        name = fileInfo.getName();
        type = fileInfo.getType().getName();
        size = fileInfo.getSize();
        uploadTime = fileInfo.getUploadTime().getTime();
        lastChangeTime = fileInfo.getLastChangeTime().getTime();
        downloadUri = getUri();
    }

    public FileInfoDto(FileInfo fileInfo) {
        uuid = fileInfo.getUuid();
        name = fileInfo.getName();
        type = fileInfo.getType().getName();
        size = fileInfo.getSize();
        uploadTime = fileInfo.getUploadTime().getTime();
        lastChangeTime = fileInfo.getLastChangeTime().getTime();
        downloadUri = getUri();
    }

    public FileInfoDto(UUID uuid, String name, String type, long size, long uploadTime, long lastChangeTime) {
        this.uuid = uuid;
        this.name = name;
        this.type = type;
        this.size = size;
        this.uploadTime = uploadTime;
        this.lastChangeTime = lastChangeTime;
        downloadUri = getUri();
    }

    private String getUri() {
        return "/api/file/" + uuid.toString();
    }
}
