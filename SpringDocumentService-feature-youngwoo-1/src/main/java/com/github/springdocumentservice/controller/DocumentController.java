package com.github.springdocumentservice.controller;

import com.github.springdocumentservice.Dto.DocumentResponseDto;
import com.github.springdocumentservice.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "문서 작성", description = "사용자가 문서를 작성합니다.")
    public ResponseEntity<DocumentResponseDto> createDocument(Principal principal,
                                                              @RequestParam("name") String name,
                                                              @RequestParam("content") String content) {
        String email = principal.getName();
        DocumentResponseDto responseDTO = documentService.createDocument(email, name, content);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/confirm/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "문서 확인", description = "관리자만 문서를 확인할 수 있습니다.")
    public ResponseEntity<Void> confirmDocument(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        documentService.confirmDocument(id, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 문서 조회", description = "user_id를 통해 문서를 조회합니다.")
    public ResponseEntity<List<DocumentResponseDto>> getDocumentsByUserId(@PathVariable Long userId) {
        List<DocumentResponseDto> documents = documentService.getDocumentsByUserId(userId);
        return ResponseEntity.ok(documents);
    }


}
