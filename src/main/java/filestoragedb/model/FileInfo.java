package filestoragedb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "file_info")
@NoArgsConstructor
public class FileInfo {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "size", updatable = false, nullable = false)
    private int size;
    @Column(name = "last_change_time", nullable = false)
    private Timestamp lastChangeTime;
    @Column(name = "upload_time", updatable = false, nullable = false)
    private Timestamp uploadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type", nullable = false)
    private Type type;


    public FileInfo(MultipartFile file, Type type) {
        name = file.getOriginalFilename();
        assert name != null;
        this.type = type;
        size = (int) file.getSize();
        uploadTime = new Timestamp(System.currentTimeMillis());
        lastChangeTime = new Timestamp(System.currentTimeMillis());
    }
}
