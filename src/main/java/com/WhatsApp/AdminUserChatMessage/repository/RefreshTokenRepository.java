package com.WhatsApp.AdminUserChatMessage.repository;

import com.WhatsApp.AdminUserChatMessage.entity.RefreshToken;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    List<RefreshToken> findAllByUser(User user);
}
