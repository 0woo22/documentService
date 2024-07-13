package com.github.springdocumentservice.repository;

import com.github.springdocumentservice.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Document save(Document document);
    Optional<Document> findById(Long id);

    List<Document> findByUserUserId(Long userId);
}
