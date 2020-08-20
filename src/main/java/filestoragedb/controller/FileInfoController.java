package filestoragedb.controller;

import filestoragedb.dto.FileInfoDto;
import filestoragedb.dto.ListDto;
import filestoragedb.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@RequestMapping("/api/file/info")
@RestController
public class FileInfoController {
    @Autowired
    private FileInfoService fileInfoService;

    @GetMapping("/names")
    public ResponseEntity<ListDto> getFileNames() {
        return new ResponseEntity<>(
                new ListDto(fileInfoService.getFileNames()),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ListDto> getFilesInfo(@RequestParam(required = false, name = "name_contains")
                                                        String nameContains,
                                                @RequestParam(required = false, name = "from_date")
                                                        Long fromDate,
                                                @RequestParam(required = false, name = "till_date")
                                                        Long tillDate,
                                                @RequestParam(required = false)
                                                        Set<String> types) {

        return new ResponseEntity<>(
                new ListDto(fileInfoService.getFileInfoList(nameContains, fromDate, tillDate, types)),
                HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public FileInfoDto getFileInfo(@PathVariable UUID uuid) {
        return fileInfoService.getFileInfo(uuid);
    }

    @PutMapping("/{uuid}")
    public void updateFileName(@PathVariable UUID uuid, @RequestParam String name) {
        fileInfoService.updateFileName(uuid, name);
    }
}