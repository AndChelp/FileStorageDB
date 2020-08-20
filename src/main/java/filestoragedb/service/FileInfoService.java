package filestoragedb.service;

import filestoragedb.dto.FileInfoDto;
import filestoragedb.model.FileInfo;
import filestoragedb.model.Type;
import filestoragedb.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileInfoService {

    private final FileInfoRepository fileInfoRepository;
    private final EntityManager entityManager;
    private final UtilService utilService;

    @Autowired
    public FileInfoService(FileInfoRepository fileInfoRepository, EntityManager entityManager, UtilService utilService) {
        this.fileInfoRepository = fileInfoRepository;
        this.entityManager = entityManager;
        this.utilService = utilService;
    }

    public List<String> getFileNames() {
        return fileInfoRepository.getFilenameList();
    }

    public void updateFileName(UUID uuid, String name) {
        Type type = utilService.findType(name);
        fileInfoRepository.getOne(uuid);
        FileInfo fileInfo = utilService.getFileInfoByUuidOrElseThrow(uuid);
        fileInfo.setName(name);
        fileInfo.setType(type);
        fileInfo.setLastChangeTime(new Timestamp(System.currentTimeMillis()));
        fileInfoRepository.save(fileInfo);
    }

    public FileInfoDto getFileInfo(UUID uuid) {
        return new FileInfoDto(uuid, utilService.getFileInfoByUuidOrElseThrow(uuid));
    }

    public List<FileInfoDto> getFileInfoList(String nameContains, Long fromDate, Long tillDate, Set<String> types) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileInfo> criteriaQuery = criteriaBuilder.createQuery(FileInfo.class);
        Root<FileInfo> fileInfoRoot = criteriaQuery.from(FileInfo.class);

        List<Predicate> predicateList = new LinkedList<>();
        if (nameContains != null && !nameContains.isEmpty())
            predicateList.add(criteriaBuilder.like(fileInfoRoot.get("name"), "%" + nameContains + "%"));
        if (fromDate != null)
            predicateList.add(criteriaBuilder.greaterThan(fileInfoRoot.get("lastChangeTime"), new Timestamp(fromDate)));
        if (tillDate != null)
            predicateList.add(criteriaBuilder.lessThan(fileInfoRoot.get("lastChangeTime"), new Timestamp(tillDate)));
        if (types != null && !types.isEmpty())
            predicateList.add(criteriaBuilder.in(fileInfoRoot.join("type").get("name")).value(types));

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        TypedQuery<FileInfo> fileInfoTypedQuery = entityManager.createQuery(criteriaQuery);
        return fileInfoTypedQuery.getResultList().stream().map(FileInfoDto::new).collect(Collectors.toList());
    }
}
