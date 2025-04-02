package com.MooBoo.MooBoo_Spring.adapter.outbound.external.jwt.dto;

import com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth.dto.OAuth2UserInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateAccessToken {
    private String userId;
    private List<String> roles;
    private String nickName;

    //== 생성 메서드 ==//
    public static CreateAccessToken to(String userId, List<String> roles, OAuth2UserInfo oAuth2UserInfo) {
        return CreateAccessToken.builder()
                .userId(userId)
                .roles(roles)
                .nickName(oAuth2UserInfo.getUserName())
                .build();
    }
}
