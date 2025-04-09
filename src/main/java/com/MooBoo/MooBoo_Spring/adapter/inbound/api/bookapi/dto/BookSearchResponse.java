package com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto.BookApiDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 응답을 위한 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchResponse {
    private String isbn13;
    private String title;
    private String description;
    private String author;
    private String cover;
    private String publisher;
    private String pubDate;
    private String itemPage;

    public static BookSearchResponse to(BookApiDto bookApiDto) {
        return BookSearchResponse.builder()
                .isbn13(bookApiDto.getIsbn13())
                .title(bookApiDto.getTitle())
                .description(bookApiDto.getDescription())
                .author(bookApiDto.getAuthor())
                .cover(bookApiDto.getCover())
                .publisher(bookApiDto.getPublisher())
                .pubDate(bookApiDto.getPubDate())
                .itemPage(bookApiDto.getItemPage())
                .build();
    }
}

