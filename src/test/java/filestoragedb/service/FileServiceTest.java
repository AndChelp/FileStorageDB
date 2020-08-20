package filestoragedb.service;

import filestoragedb.exception.FileDataNotFoundException;
import filestoragedb.exception.NullFileException;
import filestoragedb.repository.FileInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @InjectMocks
    private FileService fileService;

    @Mock
    private UtilService utilService;
    @Mock
    private FileInfoRepository fileInfoRepository;
    @Mock
    private MultipartFile file;

    @Test
    void testAddFileError() {
        Mockito.when(file.getOriginalFilename()).thenReturn("");
        Assertions.assertThrows(NullFileException.class, () -> fileService.addFile(file));
    }

    @Test
    void testGetFilenameError() {
        Mockito.when(fileInfoRepository.getFilenameById(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileDataNotFoundException.class, () -> fileService.getFilename(Mockito.any()));
    }

    @Test
    void testDeleteFileExistsException() {
        Mockito.doThrow(new FileDataNotFoundException("test")).when(utilService).checkIfExistsOrElseThrow(Mockito.any());
        Assertions.assertThrows(FileDataNotFoundException.class, () -> fileService.deleteFile(Mockito.any()));
    }

    @Test
    void testGetFileExistsException() {
        Mockito.doThrow(new FileDataNotFoundException("test")).when(utilService).checkIfExistsOrElseThrow(Mockito.any());
        Assertions.assertThrows(FileDataNotFoundException.class, () -> fileService.getFile(Mockito.any()));
    }
}