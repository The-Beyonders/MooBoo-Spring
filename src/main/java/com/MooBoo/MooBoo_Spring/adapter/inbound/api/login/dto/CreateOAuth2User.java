package com.MooBoo.MooBoo_Spring.adapter.inbound.api.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOAuth2User {
    private String userName;
    private String provider;
    private String providerId;
    private String image;
}
