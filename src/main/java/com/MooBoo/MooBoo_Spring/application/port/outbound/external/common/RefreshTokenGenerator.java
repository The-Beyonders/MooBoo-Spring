package com.MooBoo.MooBoo_Spring.application.port.outbound.external.common;

public interface RefreshTokenGenerator {
    String createOpaqueToken();
}
