package com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class OAuth2UserInfo {
    private String providerId;
    private String userName;
    private String connectedAt;
}
