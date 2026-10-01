package com.WhatsApp.AdminUserChatMessage.repository;

import com.WhatsApp.AdminUserChatMessage.entity.BroadCastMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BroadCastRepository extends JpaRepository<BroadCastMessage, Long> {
    List<BroadCastMessage> findByCreatedAtAfterOrderByIdAsc(LocalDateTime loginTime);
}
