package com.WhatsApp.AdminUserChatMessage.entity;

import com.WhatsApp.AdminUserChatMessage.entity.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class PresentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private LocalDateTime dateTime;

}
