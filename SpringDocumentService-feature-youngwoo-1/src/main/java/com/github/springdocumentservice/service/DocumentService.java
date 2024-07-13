package com.github.springdocumentservice.service;

import com.github.springdocumentservice.Dto.DocumentResponseDto;
import com.github.springdocumentservice.domain.Document;
import com.github.springdocumentservice.domain.User;
import com.github.springdocumentservice.repository.DocumentRepository;
import com.github.springdocumentservice.repository.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {


    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DocumentResponseDto createDocument(String email, String name, String content) {
        User user = userRepository.findByEmail2(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Document document = new Document();
        document.setName(name);
        document.setContent(content);
        document.setConfirmed(false);
        document.setUser(user);
        document = documentRepository.save(document);

        return new DocumentResponseDto(document.getId(), name, content, false, user.getUserId());
    }

    @Transactional
    public void confirmDocument(Long id, String email) {
        User user = userRepository.findByEmail2(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"admin".equalsIgnoreCase(user.getUserStatus())) {
            throw new RuntimeException("You do not have permission to confirm the document");
        }

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setConfirmed(true);
        documentRepository.save(document);
    }

    @Transactional
    public List<DocumentResponseDto> getDocumentsByUserId(Long userId) {
        List<Document> documents = documentRepository.findByUserUserId(userId);
        return documents.stream()
                .map(document -> new DocumentResponseDto(
                        document.getId(),
                        document.getName(),
                        document.getContent(),
                        document.isConfirmed(),
                        document.getUser().getUserId()))
                .collect(Collectors.toList());
    }
}
