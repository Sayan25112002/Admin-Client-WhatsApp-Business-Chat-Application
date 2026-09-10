package com.WhatsApp.AdminUserChatMessage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @JoinColumn(name = "adminId", nullable = false)
    @JsonIgnore
    private User admin;

    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    @JsonIgnore
    private User client;

    @Column(nullable=false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "conversation")
    @JsonIgnore
    private List<ChatMessage> chatMessages = new ArrayList<>();

}
