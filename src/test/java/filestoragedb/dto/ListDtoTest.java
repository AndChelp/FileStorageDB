package filestoragedb.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ListDtoTest {

    @Autowired
    private JacksonTester<ListDto> json;

    @Test
    void testListDtoSerialization() throws IOException {
        ListDto listDto = new ListDto(Arrays.asList(1, 2));
        JsonContent<ListDto> resultJson = this.json.write(listDto);
        assertThat(resultJson).hasJsonPathNumberValue("count");
        assertThat(resultJson).extractingJsonPathNumberValue("count").isEqualTo(listDto.getItems().size());
        assertThat(resultJson).hasJsonPathValue("items");
    }
}