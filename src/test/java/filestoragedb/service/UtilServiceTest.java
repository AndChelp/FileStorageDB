package filestoragedb.service;

import filestoragedb.exception.FileDataNotFoundException;
import filestoragedb.exception.FileTypeNotSupportedException;
import filestoragedb.model.Type;
import filestoragedb.repository.FileInfoRepository;
import filestoragedb.repository.TypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UtilServiceTest {
    @InjectMocks
    private UtilService utilService;

    @Mock
    private FileInfoRepository fileInfoRepository;
    @Mock
    private TypeRepository typeRepository;

    @Test
    void testFindTypeSuccess() {
        String filename = "file.test";
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        Type searchedType = new Type((short) 1, fileType);
        Mockito.when(typeRepository.findByName(fileType)).thenReturn(Optional.of(searchedType));
        Type returnedType = utilService.findType(filename);
        Mockito.verify(typeRepository, Mockito.times(1)).findByName(fileType);
        assertThat(searchedType).isEqualTo(returnedType);
    }

    @Test
    void testFindTypeErrorWithNoType() {
        Assertions.assertThrows(FileTypeNotSupportedException.class, () -> utilService.findType("test"));
        Mockito.verify(typeRepository, Mockito.times(0)).findByName("test");
    }

    @Test
    void testFindTypeErrorWithTypeNotSupported() {
        Assertions.assertThrows(FileTypeNotSupportedException.class, () -> utilService.findType("test.test"));
        Mockito.verify(typeRepository).findByName("test");
    }

    @Test
    void testGetFileInfoByUuidOrElseThrowError() {
        UUID uuid = UUID.randomUUID();
        Assertions.assertThrows(FileDataNotFoundException.class, () -> utilService.getFileInfoByUuidOrElseThrow(uuid));
        Mockito.verify(fileInfoRepository).findById(uuid);
    }

    @Test
    void testCheckIfExistsOrElseThrowError() {
        UUID uuid = UUID.randomUUID();
        Assertions.assertThrows(FileDataNotFoundException.class, () -> utilService.checkIfExistsOrElseThrow(uuid));
        Mockito.verify(fileInfoRepository).existsById(uuid);
    }

    @Test
    void testFindLocalFile() {
        ReflectionTestUtils.setField(utilService, "fileUploadPath", "test");
        UUID uuid = UUID.randomUUID();
        File returnedFile = utilService.findLocalFile(uuid);
        File expectedFile = new File(utilService.fileUploadPath + "/" + uuid);
        Assertions.assertEquals(returnedFile, expectedFile);
    }

}