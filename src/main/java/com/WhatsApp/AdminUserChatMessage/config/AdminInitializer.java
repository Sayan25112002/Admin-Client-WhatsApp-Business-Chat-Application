package com.WhatsApp.AdminUserChatMessage.config;

import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.entity.type.Role;
import com.WhatsApp.AdminUserChatMessage.entity.type.UserStatus;
import com.WhatsApp.AdminUserChatMessage.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if(userRepository.count()==0){
            User user = User.builder()
                    .name("ADMIN")
                    .contact("9876543210")
                    .email("admin123@gmail.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .status(UserStatus.ONLINE)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }
    }
}
