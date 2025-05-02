package com.MooBoo.MooBoo_Spring.adapter.outbound.external.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuth2UserInfo {
    private String providerId;
    private String userName;
    private String connectedAt;
    private String profileImage;
    private String thumbnailImage;

    // OAuth2ServiceUserInfoFactory에서 사용
    public OAuth2UserInfo(String providerId, String userName, String connectedAt) {
        this.providerId = providerId;
        this.userName = userName;
        this.connectedAt = connectedAt;
    }

    // OAuth2SuccessUserInfoFactory에서 사용
    public OAuth2UserInfo(String providerId, String userName,  String profileImage, String thumbnailImage) {
        this.userName = userName;
        this.providerId = providerId;
        this.profileImage = profileImage;
        this.thumbnailImage = thumbnailImage;
    }
}
