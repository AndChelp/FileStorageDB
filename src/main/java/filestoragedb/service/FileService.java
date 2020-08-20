package filestoragedb.service;

import filestoragedb.exception.FileDataNotFoundException;
import filestoragedb.exception.NullFileException;
import filestoragedb.model.FileInfo;
import filestoragedb.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    @Autowired
    private UtilService utilService;
    @Autowired
    private FileInfoRepository fileInfoRepository;


    public void addFile(MultipartFile receivedFile) throws IOException {
        String filename = receivedFile.getOriginalFilename();

        if (filename == null || filename.isEmpty())
            throw new NullFileException("Empty filename or file not received");

        FileInfo fileInfo = fileInfoRepository.save(new FileInfo(receivedFile, utilService.findType(filename)));

        File fileUploadDir = new File(utilService.fileUploadPath);
        if (!fileUploadDir.exists())
            fileUploadDir.mkdirs();
        File localFile = utilService.findLocalFile(fileInfo.getUuid());
        receivedFile.transferTo(localFile);
    }

    public void deleteFile(UUID uuid) {
        utilService.checkIfExistsOrElseThrow(uuid);
        fileInfoRepository.deleteById(uuid);
        File localFile = utilService.findLocalFile(uuid);
        localFile.delete();
    }

    public byte[] getFile(UUID uuid) throws IOException {
        utilService.checkIfExistsOrElseThrow(uuid);
        return utilService.findLocalFileAsBytes(uuid);
    }

    public String getFilename(UUID uuid) {
        return fileInfoRepository.getFilenameById(uuid)
                .orElseThrow(() -> new FileDataNotFoundException("File data with UUID = " + uuid + " not found"));
    }

    public byte[] getFilesAsArchive(UUID[] uuids) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ZipOutputStream zos = new ZipOutputStream(bos)) {
                for (UUID uuid : uuids) {
                    zos.putNextEntry(new ZipEntry(getFilename(uuid)));
                    zos.write(utilService.findLocalFileAsBytes(uuid));
                    zos.closeEntry();
                }
            }
            return bos.toByteArray();
        }
    }
}
