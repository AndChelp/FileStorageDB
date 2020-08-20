package filestoragedb.service;

import filestoragedb.exception.FileDataNotFoundException;
import filestoragedb.exception.FileTypeNotSupportedException;
import filestoragedb.model.FileInfo;
import filestoragedb.model.Type;
import filestoragedb.repository.FileInfoRepository;
import filestoragedb.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UtilService {

    @Value("${upload.path}")
    String fileUploadPath;

    private final FileInfoRepository fileInfoRepository;
    private final TypeRepository typeRepository;

    @Autowired
    public UtilService(FileInfoRepository fileInfoRepository, TypeRepository typeRepository) {
        this.fileInfoRepository = fileInfoRepository;
        this.typeRepository = typeRepository;
    }

    FileInfo getFileInfoByUuidOrElseThrow(UUID uuid) {
        return fileInfoRepository.findById(uuid)
                .orElseThrow(() -> new FileDataNotFoundException("File data with UUID = " + uuid + " not found"));
    }

    Type findType(String filename) {
        int lastDotPosition = filename.lastIndexOf(".") + 1;
        String fileType = filename.substring(lastDotPosition);

        if (lastDotPosition == 0)
            throw new FileTypeNotSupportedException("Please specify the type");

        Optional<Type> type = typeRepository.findByName(fileType);

        if (!type.isPresent())
            throw new FileTypeNotSupportedException("Type '" + fileType + "' not supported");
        return type.get();
    }

    void checkIfExistsOrElseThrow(UUID uuid) {
        if (!fileInfoRepository.existsById(uuid))
            throw new FileDataNotFoundException("File data with UUID = " + uuid + " not found");
    }

    File findLocalFile(UUID uuid) {
        return new File(fileUploadPath + "/" + uuid);
    }

    byte[] findLocalFileAsBytes(UUID uuid) throws IOException {
        return FileCopyUtils.copyToByteArray(new File(fileUploadPath + "/" + uuid));
    }
}
