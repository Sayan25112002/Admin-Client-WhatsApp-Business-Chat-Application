package com.WhatsApp.AdminUserChatMessage.repository;

import com.WhatsApp.AdminUserChatMessage.entity.BroadCastMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadCastRepository extends JpaRepository<BroadCastMessage, Long> {
}
