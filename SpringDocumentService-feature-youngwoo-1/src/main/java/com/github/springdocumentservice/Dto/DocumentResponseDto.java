package com.github.springdocumentservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDto {
    private Long id;
    private String name;
    private String content;
    private boolean confirmed;
    private Long userId;
}
