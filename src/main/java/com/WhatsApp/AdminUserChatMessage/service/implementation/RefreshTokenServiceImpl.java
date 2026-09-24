package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.entity.RefreshToken;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.repository.RefreshTokenRepository;
import com.WhatsApp.AdminUserChatMessage.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void saveRefreshToken(User user, String refreshToken) {
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenEntity.setRevoked(false);
        refreshTokenEntity.setCreatedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository
                .findByRefreshToken(refreshToken)
                .orElse(null);
        if(refreshTokenEntity == null){
            return false;
        }
        if(Boolean.TRUE.equals(refreshTokenEntity.getRevoked())){
            return false;
        }
        if(!refreshTokenEntity.getExpiresAt().isAfter(LocalDateTime.now())){
            return false;
        }
        return true;
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(()-> new RuntimeException("RefreshToken not found"));
        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public void revokeAllRefreshTokens(User user) {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUser(user);
        refreshTokens.forEach(refreshToken->refreshToken.setRevoked(true));
        refreshTokenRepository.saveAll(refreshTokens);
    }

}
