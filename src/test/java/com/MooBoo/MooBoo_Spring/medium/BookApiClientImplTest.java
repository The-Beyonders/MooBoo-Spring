package com.MooBoo.MooBoo_Spring.medium;

import com.MooBoo.MooBoo_Spring.domain.bookapi.BookApi;
import com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto.SearchParam;
import com.MooBoo.MooBoo_Spring.application.port.outbound.external.bookapi.BookApiClient;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@SpringBootTest
public class BookApiClientImplTest {

    @Autowired
    BookApiClient bookApiClient;

    @Test
    public void getBooksBySearchParamTest() throws Exception {
        //given
        // FIXME: 결정적인 테스트 코드가 아니므로, 주의해야 함
        SearchParam searchParam = new SearchParam();
        searchParam.setKeyword("java");
        searchParam.setMaxResults(1);
        searchParam.setFormat("JS");

        //when
        List<BookApi> bookApis = bookApiClient.getBooksBySearchParam(searchParam).block();

        String isbn13 = (String) ReflectionTestUtils.getField(bookApis.get(0), "isbn13");
        String title = (String) ReflectionTestUtils.getField(bookApis.get(0), "title");
        String description = (String) ReflectionTestUtils.getField(bookApis.get(0), "description");
        String author = (String) ReflectionTestUtils.getField(bookApis.get(0), "author");
        String cover = (String) ReflectionTestUtils.getField(bookApis.get(0), "cover");
        String publisher = (String) ReflectionTestUtils.getField(bookApis.get(0), "publisher");
        String pubDate = (String) ReflectionTestUtils.getField(bookApis.get(0), "pubDate");

        //then
        Assertions.assertThat(isbn13).isEqualTo("9791192932767");
        Assertions.assertThat(title).isEqualTo("명품 JAVA Programming - 개정5판");
        Assertions.assertThat(description).isEqualTo("자바 프로그래밍 개념과 방법을 충실히 정리하고 적절한 예제를 수록하였다. 많은 그림과 삽화로 개념을 쉽게 이해하도록 하였다. 각 절마다 Check Time 문제를 두어 배운 내용을 확인할 수 있도록 하였다. 연습문제는 이론문제와 실습문제로 나누었고, 홀수 번과 짝수 번을 유사한 문제로 꾸며, 반복 연습해 볼 수 있도록 구성하였다.");
        Assertions.assertThat(author).isEqualTo("황기태, 김효수 (지은이)");
        Assertions.assertThat(cover).isEqualTo("https://image.aladin.co.kr/product/34293/21/coversum/k362932270_1.jpg");
        Assertions.assertThat(publisher).isEqualTo("생능");
        Assertions.assertThat(pubDate).isEqualTo("2024-07-23");
    }

    @Test
    public void getBookByIsbnTest() throws Exception {
        //given
        // FIXME: 결정적인 테스트 코드가 아니므로, 주의해야 함
        BookApi bookApi = bookApiClient.getBookByIsbn("9791192932767").block();

        //when
        String isbn13 = (String) ReflectionTestUtils.getField(bookApi, "isbn13");
        String title = (String) ReflectionTestUtils.getField(bookApi, "title");
        String description = (String) ReflectionTestUtils.getField(bookApi, "description");
        String author = (String) ReflectionTestUtils.getField(bookApi, "author");
        String cover = (String) ReflectionTestUtils.getField(bookApi, "cover");
        String publisher = (String) ReflectionTestUtils.getField(bookApi, "publisher");
        String pubDate = (String) ReflectionTestUtils.getField(bookApi, "pubDate");
        String itemPage = (String) ReflectionTestUtils.getField(bookApi, "itemPage");

        //then
        Assertions.assertThat(isbn13).isEqualTo("9791192932767");
        Assertions.assertThat(title).isEqualTo("명품 JAVA Programming - 개정5판");
        Assertions.assertThat(description).isEqualTo("자바 프로그래밍 개념과 방법을 충실히 정리하고 적절한 예제를 수록하였다. 많은 그림과 삽화로 개념을 쉽게 이해하도록 하였다. 각 절마다 Check Time 문제를 두어 배운 내용을 확인할 수 있도록 하였다. 연습문제는 이론문제와 실습문제로 나누었고, 홀수 번과 짝수 번을 유사한 문제로 꾸며, 반복 연습해 볼 수 있도록 구성하였다.");
        Assertions.assertThat(author).isEqualTo("황기태, 김효수 (지은이)");
        Assertions.assertThat(cover).isEqualTo("https://image.aladin.co.kr/product/34293/21/coversum/k362932270_1.jpg");
        Assertions.assertThat(publisher).isEqualTo("생능");
        Assertions.assertThat(pubDate).isEqualTo("2024-07-23");
        Assertions.assertThat(itemPage).isEqualTo("844");
    }

}
