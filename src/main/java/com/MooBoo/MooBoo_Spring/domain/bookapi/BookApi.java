package com.MooBoo.MooBoo_Spring.domain.bookapi;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto.BookItem;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto.BookSearchResponse;
import lombok.*;

/**
 * 외부 도서 API를 위한 도메인 엔티티
 */
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BookApi {
    private String isbn13;
    private String title;
    private String description;
    private String author;
    private String cover;
    private String publisher;
    private String pubDate;
    private String itemPage;


    //== 변환 메서드 ==//
    public static BookApi fromBookItem(BookItem bookItem) {
        return BookApi.builder()
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

    public BookSearchResponse toBookSearchResponse() {
        return BookSearchResponse.builder()
                .isbn13(this.isbn13)
                .title(this.title)
                .description(this.description)
                .author(this.author)
                .cover(this.cover)
                .publisher(this.publisher)
                .pubDate(this.pubDate)
                .itemPage(this.itemPage)
                .build();
    }

}
