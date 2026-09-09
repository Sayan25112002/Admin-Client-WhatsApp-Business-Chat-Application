package com.WhatsApp.AdminUserChatMessage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(
        name="conversation",
        uniqueConstraints ={
                @UniqueConstraint(
                        name = "unique_admin_client_id",
                        columnNames = {"adminId","clientId"}
                )
        }
)
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adminId")
    @JsonIgnore
    private User admin;

    @ManyToOne
    @JoinColumn(name = "clientId")
    @JsonIgnore
    private User client;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conversation")
    @JsonIgnore
    private List<ChatMessage> chatMessages;

}
