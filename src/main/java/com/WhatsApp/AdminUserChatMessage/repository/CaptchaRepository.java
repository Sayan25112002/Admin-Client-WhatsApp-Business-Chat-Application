package com.WhatsApp.AdminUserChatMessage.repository;

import com.WhatsApp.AdminUserChatMessage.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha,Long> {
}
