package filestoragedb.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class FileInfoDtoTest {

    @Autowired
    private JacksonTester<FileInfoDto> json;

    @Test
    void testSerializeFileInfoDto() throws IOException {
        FileInfoDto fileInfoDto = new FileInfoDto(UUID.randomUUID(), "test", ".test", 123, 321, 213);
        JsonContent<FileInfoDto> resultJson = this.json.write(fileInfoDto);

        assertThat(resultJson).hasJsonPathValue("uuid");
        assertThat(resultJson).hasJsonPathStringValue("name");
        assertThat(resultJson).hasJsonPathStringValue("type");
        assertThat(resultJson).hasJsonPathNumberValue("size");
        assertThat(resultJson).hasJsonPathNumberValue("uploadTime");
        assertThat(resultJson).hasJsonPathNumberValue("lastChangeTime");
        assertThat(resultJson).hasJsonPathStringValue("downloadUri");
        assertThat(resultJson).extractingJsonPathStringValue("downloadUri").isEqualTo(fileInfoDto.getDownloadUri());
    }

}