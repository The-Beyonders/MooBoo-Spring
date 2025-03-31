package com.MooBoo.MooBoo_Spring.adapter.outbound.external.bookapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookInfo {
    @JsonProperty("itemPage")
    private String itemPage;
}
