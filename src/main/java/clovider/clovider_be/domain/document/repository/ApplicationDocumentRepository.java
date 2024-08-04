package clovider.clovider_be.domain.document.repository;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.document.Document;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByApplicationId(Long id);
}
