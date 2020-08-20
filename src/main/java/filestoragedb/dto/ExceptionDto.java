package filestoragedb.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Getter
public class ExceptionDto {
    private long timestamp;
    private int status;
    private String type;
    private String message;

    public ExceptionDto(String message, HttpStatus httpStatus) {
        timestamp = System.currentTimeMillis();
        status = httpStatus.value();
        type = httpStatus.name();
        this.message = message;
    }
}
