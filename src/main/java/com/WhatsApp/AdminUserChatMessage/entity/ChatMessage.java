package com.WhatsApp.AdminUserChatMessage.entity;

import com.WhatsApp.AdminUserChatMessage.entity.type.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversationId", nullable = false)
    @JsonIgnore
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable=false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private MessageType messageType;

    @Column(nullable=false)
    private LocalDateTime createdAt;

}
