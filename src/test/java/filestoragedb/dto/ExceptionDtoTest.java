package filestoragedb.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ExceptionDtoTest {
    @Autowired
    private JacksonTester<ExceptionDto> json;

    @Test
    void testSerializeExceptionDto() throws Exception {
        ExceptionDto exceptionDto = new ExceptionDto("Test message", HttpStatus.I_AM_A_TEAPOT);
        JsonContent<ExceptionDto> resultJson = this.json.write(exceptionDto);
        assertThat(resultJson).hasJsonPathNumberValue("timestamp");
        assertThat(resultJson).extractingJsonPathNumberValue("timestamp").isEqualTo(exceptionDto.getTimestamp());
        assertThat(resultJson).hasJsonPathNumberValue("status");
        assertThat(resultJson).extractingJsonPathNumberValue("status").isEqualTo(exceptionDto.getStatus());
        assertThat(resultJson).hasJsonPathStringValue("type");
        assertThat(resultJson).extractingJsonPathStringValue("type").isEqualTo(exceptionDto.getType());
        assertThat(resultJson).hasJsonPathStringValue("message");
        assertThat(resultJson).extractingJsonPathStringValue("message").isEqualTo(exceptionDto.getMessage());
    }
}