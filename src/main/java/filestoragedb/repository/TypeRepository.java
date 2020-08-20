package filestoragedb.repository;

import filestoragedb.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Short> {
    Optional<Type> findByName(String name);
}
