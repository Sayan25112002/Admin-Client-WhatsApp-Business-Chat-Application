package com.WhatsApp.AdminUserChatMessage.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActiveCallManager {

    private final Map<String, Map<Long,String>> activeCalls = new ConcurrentHashMap<>();

    public void createCall(String callId){
        activeCalls.putIfAbsent(callId, new ConcurrentHashMap<>());
    }

    public void addParticipant(String callId, Long userId, String userName) {
        createCall(callId);
        activeCalls.get(callId).put(userId, userName);
    }

    public void removeParticipant(String callId, Long userId) {
        Map<Long, String> participants = activeCalls.get(callId);
        participants.remove(userId);
        if(participants.isEmpty()){
            activeCalls.remove(callId);
        }
    }

    public Map<Long, String> getParticipants(String callId) {
        return activeCalls.getOrDefault(callId, new ConcurrentHashMap<>());
    }

    public Boolean isParticipant(String callId, Long userId) {
        Map<Long, String> participants = activeCalls.get(callId);
        return participants!=null && participants.containsKey(userId);
    }

    public Boolean isCallActive(String callId) {
        Map<Long, String> participants = activeCalls.get(callId);
        return participants!=null && participants.isEmpty();
    }

    public Boolean callExists(String callId) {
        return activeCalls.containsKey(callId);
    }

    public void removeCall(String callId) {
        activeCalls.remove(callId);
    }

    public Map<String, Map<Long, String>> getActiveCalls() {
        return activeCalls;
    }

}
