package filestoragedb.repository;

import filestoragedb.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {
    @Query("SELECT u.name FROM FileInfo u WHERE u.uuid = :uuid")
    Optional<String> getFilenameById(UUID uuid);

    @Query("SELECT u.name FROM FileInfo u")
    List<String> getFilenameList();
}
