package com.WhatsApp.AdminUserChatMessage.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActiveCallManager {

    private final Map<Long, String> participants = new ConcurrentHashMap<>();

    public void addParticipant(Long userId, String userName) {
        participants.put(userId, userName);
    }

    public void removeParticipant(Long userId) {
        participants.remove(userId);
    }

    public Map<Long, String> getParticipants() {
        return participants;
    }

    public Boolean isParticipant(Long userId) {
        return participants.containsKey(userId);
    }

    public Boolean isCallActive() {
        return !participants.isEmpty();
    }

}
