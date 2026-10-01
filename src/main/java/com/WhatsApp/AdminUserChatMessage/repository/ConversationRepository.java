package com.WhatsApp.AdminUserChatMessage.repository;

import com.WhatsApp.AdminUserChatMessage.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    Optional<Conversation> findByAdminIdAndClientId(Long adminId, Long clientId);

    Optional<Conversation> findByIdAndAdminId(Long id, Long adminId);

}
