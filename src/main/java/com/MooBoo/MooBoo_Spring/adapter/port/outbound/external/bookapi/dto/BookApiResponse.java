package com.MooBoo.MooBoo_Spring.adapter.port.outbound.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 외부 API 로부터 데이터를 저장할 DTO
 */
@Data
@Builder
public class BookApiResponse {

    @JsonProperty("totalResults")
    public Integer totalResults;
    @JsonProperty("query")
    public String query;
    @JsonProperty("item")
    public List<BookItem> item;

}
