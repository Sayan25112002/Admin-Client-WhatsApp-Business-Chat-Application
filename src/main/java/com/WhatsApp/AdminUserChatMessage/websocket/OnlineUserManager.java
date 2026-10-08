package com.WhatsApp.AdminUserChatMessage.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUserManager {

    private final Map<Long, String> onlineUsers = new ConcurrentHashMap<>();

    public void addUser(Long userId, String userName) {
        onlineUsers.put(userId, userName);
    }

    public void removeUser(Long userId) {
        onlineUsers.remove(userId);
    }

    public Map<Long, String> getOnlineUsers() {
        return onlineUsers;
    }

    public Boolean isUserOnline(Long userId) {
        return onlineUsers.containsKey(userId);
    }

}
