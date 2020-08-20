package filestoragedb.service;

import filestoragedb.model.FileInfo;
import filestoragedb.model.Type;
import filestoragedb.repository.FileInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class FileInfoServiceTest {

    @InjectMocks
    private FileInfoService fileInfoService;

    @Mock
    private UtilService utilService;
    @Mock
    private FileInfoRepository fileInfoRepository;

    @Test
    void testGetFileNames() {
        fileInfoService.getFileNames();
        Mockito.verify(fileInfoRepository).getFilenameList();
    }

    @Test
    void testUpdateFileName() {
        String name = "test.te";
        UUID uuid = UUID.randomUUID();
        Mockito.when(utilService.findType(name)).thenReturn(new Type((short) 1, "te"));
        Mockito.when(utilService.getFileInfoByUuidOrElseThrow(uuid)).thenReturn(new FileInfo());
        fileInfoService.updateFileName(uuid, name);
        Mockito.verify(fileInfoRepository).save(Mockito.any(FileInfo.class));
    }

}