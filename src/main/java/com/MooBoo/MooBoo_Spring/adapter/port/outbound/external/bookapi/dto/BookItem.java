package com.MooBoo.MooBoo_Spring.adapter.port.outbound.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 외부 API 로부터 데이터를 저장할 DTO
 */
@Data
@Builder
public class BookItem {

    private String isbn13;
    private String title;
    private String description;
    private String author;
    private String cover;
    private String publisher;
    private String pubDate;
    @JsonProperty("subInfo")
    private BookInfo bookInfo;


}
