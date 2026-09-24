package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.entity.User;

public interface RefreshTokenService {

    void saveRefreshToken(User user, String refreshToken);

    boolean validateRefreshToken(String refreshToken);

    void revokeRefreshToken(String refreshToken);

    void revokeAllRefreshTokens(User user);

}
