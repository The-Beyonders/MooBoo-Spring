package com.MooBoo.MooBoo_Spring.small.bookapi.domain;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto.BookApiResponse;
import com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto.BookInfo;
import com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto.BookItem;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto.BookSearchResponse;
import com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto.BookApiDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

public class BookApiDtoTest {

    @Test
    public void fromBookApiResponseTest() throws Exception {

        //given
        BookApiResponse bookApiResponse = getBookApiResponse();

        //when
        BookApiDto bookApiDto1 = BookApiDto.to(bookApiResponse.getItem().get(0));

        String bookApi1_isbn13 = (String) ReflectionTestUtils.getField(bookApiDto1, "isbn13");
        String bookApi1_title = (String) ReflectionTestUtils.getField(bookApiDto1, "title");
        String bookApi1_description = (String) ReflectionTestUtils.getField(bookApiDto1, "description");
        String bookApi1_author = (String) ReflectionTestUtils.getField(bookApiDto1, "author");
        String bookApi1_cover = (String) ReflectionTestUtils.getField(bookApiDto1, "cover");
        String bookApi1_publisher = (String) ReflectionTestUtils.getField(bookApiDto1, "publisher");
        String bookApi1_pubDate = (String) ReflectionTestUtils.getField(bookApiDto1, "pubDate");
        String bookApi1_itemPage = (String) ReflectionTestUtils.getField(bookApiDto1, "itemPage");

        //then
        Assertions.assertThat(bookApi1_isbn13).isEqualTo("9788994492049");
        Assertions.assertThat(bookApi1_title).isEqualTo("자바의 정석");
        Assertions.assertThat(bookApi1_description).isEqualTo("자바에 대한 책입니다.");
        Assertions.assertThat(bookApi1_author).isEqualTo("남궁성");
        Assertions.assertThat(bookApi1_cover).isEqualTo("https://image.aladin.co.kr/product/48/91/coversum/8972833894_1.gif");
        Assertions.assertThat(bookApi1_publisher).isEqualTo("도우출판");
        Assertions.assertThat(bookApi1_pubDate).isEqualTo("2025-03-21");
        Assertions.assertThat(bookApi1_itemPage).isEqualTo("533");

    }
    
    @Test
    public void toBookSearchResponseTest() throws Exception {
        //given
        BookApiDto bookApiDto = BookApiDto.builder()
                .isbn13("9788994492049")
                .title("자바의 정석")
                .description("자바에 대한 책입니다.")
                .author("남궁성")
                .cover("https://image.aladin.co.kr/product/48/91/coversum/8972833894_1.gif")
                .publisher("도우출판")
                .pubDate("2025-03-21")
                .itemPage("533")
                .build();
        //when

        BookSearchResponse bookSearchResponse = BookSearchResponse.to(bookApiDto);

        //then
        Assertions.assertThat(bookSearchResponse.getIsbn13()).isEqualTo("9788994492049");
        Assertions.assertThat(bookSearchResponse.getTitle()).isEqualTo("자바의 정석");
        Assertions.assertThat(bookSearchResponse.getDescription()).isEqualTo("자바에 대한 책입니다.");
        Assertions.assertThat(bookSearchResponse.getAuthor()).isEqualTo("남궁성");
        Assertions.assertThat(bookSearchResponse.getCover()).isEqualTo("https://image.aladin.co.kr/product/48/91/coversum/8972833894_1.gif");
        Assertions.assertThat(bookSearchResponse.getPublisher()).isEqualTo("도우출판");
        Assertions.assertThat(bookSearchResponse.getPubDate()).isEqualTo("2025-03-21");
        Assertions.assertThat(bookSearchResponse.getItemPage()).isEqualTo("533");

    }

    private static BookApiResponse getBookApiResponse() {
        BookInfo bookInfo1 = new BookInfo();
        bookInfo1.setItemPage("533");

        BookItem bookItem1 = BookItem.builder()
                .isbn13("9788994492049")
                .title("자바의 정석")
                .description("자바에 대한 책입니다.")
                .author("남궁성")
                .cover("https://image.aladin.co.kr/product/48/91/coversum/8972833894_1.gif")
                .publisher("도우출판")
                .pubDate("2025-03-21")
                .bookInfo(bookInfo1)
                .build();

        BookInfo bookInfo2 = new BookInfo();
        bookInfo2.setItemPage("533");

        BookItem bookItem2 = BookItem.builder()

                .isbn13("9788994492049")
                .title("자바의 정석")
                .description("자바에 대한 책입니다.")
                .author("남궁성")
                .cover("https://image.aladin.co.kr/product/48/91/coversum/8972833894_1.gif")
                .publisher("도우출판")
                .pubDate("2025-03-21")
                .bookInfo(bookInfo2)
                .build();

        List<BookItem> bookItems = new ArrayList<>();
        bookItems.add(bookItem1);
        bookItems.add(bookItem2);


        BookApiResponse bookApiResponse = BookApiResponse.builder()
                .totalResults(10)
                .query("자바의 정석")
                .item(bookItems)
                .build();

        return bookApiResponse;
    }
}
