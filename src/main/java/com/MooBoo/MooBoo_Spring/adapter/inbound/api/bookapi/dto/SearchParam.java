package com.MooBoo.MooBoo_Spring.adapter.inbound.api.bookapi.dto;

import lombok.Data;

/**
 * 도서 검색시 필요한 파라미터 저장할 DTO
 */
@Data
public class SearchParam {
    private String keyword;
    private Integer MaxResults;
    private String format;
}
