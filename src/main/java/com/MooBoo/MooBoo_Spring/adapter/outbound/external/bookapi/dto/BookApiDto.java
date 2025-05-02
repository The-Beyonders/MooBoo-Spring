package com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto;

import lombok.*;

/**
 * 조회한 도서 정보를 반환할 때 사용하는 DTO
 */
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BookApiDto {
    private String isbn13;
    private String title;
    private String description;
    private String author;
    private String cover;
    private String publisher;
    private String pubDate;
    private String itemPage;


    //== 변환 메서드 ==//
    public static BookApiDto to(BookItem bookItem) {
        return BookApiDto.builder()
                .isbn13(bookItem.getIsbn13())
                .title(bookItem.getTitle())
                .description(bookItem.getDescription())
                .author(bookItem.getAuthor())
                .cover(bookItem.getCover())
                .publisher(bookItem.getPublisher())
                .pubDate(bookItem.getPubDate())
                .itemPage(bookItem.getBookInfo().getItemPage())
                .build();
    }
}
