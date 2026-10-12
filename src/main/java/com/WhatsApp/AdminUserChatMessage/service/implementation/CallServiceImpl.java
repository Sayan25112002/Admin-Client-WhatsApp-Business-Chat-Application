package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.*;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.*;
import com.WhatsApp.AdminUserChatMessage.entity.type.CallType;
import com.WhatsApp.AdminUserChatMessage.service.CallService;
import com.WhatsApp.AdminUserChatMessage.websocket.ActiveCallManager;
import com.WhatsApp.AdminUserChatMessage.websocket.OnlineUserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CallServiceImpl implements CallService {

    public final ActiveCallManager activeCallManager;
    public final OnlineUserManager onlineUserManager;
    public final SimpMessagingTemplate simpMessagingTemplate;

    private final Map<String, CallType> callTypes = new ConcurrentHashMap<>();
    private final Map<String, Long> privateCallReceivers = new ConcurrentHashMap<>();

    @Override
    public void online(CallStartRequestDto callStartRequestDto) {
        if(callStartRequestDto==null){
            return;
        }
        Long userId = callStartRequestDto.getCallerId();
        String userName = callStartRequestDto.getCallerName();
        if(userId==null || userName==null || userName.isBlank()){
            return;
        }
        onlineUserManager.addUser(userId, userName);
        String callId = callStartRequestDto.getCallId();
        if(callId==null || callId.isBlank() || !activeCallManager.isParticipant(callId, userId)){
            return;
        }
        Map<Long, String> participants = new HashMap<>(activeCallManager.getParticipants(callId));
        participants.remove(userId);
        simpMessagingTemplate.convertAndSend(
                "/topic/call/"+userId,
                CallParticipantsResponseDto.builder()
                        .callId(callId)
                        .status("PARTICIPANTS")
                        .participants(participants)
                        .build()
        );
    }

    @Override
    public void startCall(CallStartRequestDto callStartRequestDto) {
        String callId = callStartRequestDto.getCallId();
        Long callerId = callStartRequestDto.getCallerId();
        String callerName = callStartRequestDto.getCallerName();
        CallType callType = callStartRequestDto.getCallType();
        if(callId==null || callId.isBlank() || callerId==null || callerName==null || callerName.isBlank() || callType==null){
            return;
        }
        if(activeCallManager.callExists(callId)){
            sendNotification(callerId,callerId,callerName,callId,"CALL_ACTIVE");
            return;
        }
        String registeredName = onlineUserManager.getOnlineUsers().getOrDefault(callerId, callerName);
        activeCallManager.createCall(callId);
        activeCallManager.addParticipant(callId, callerId, registeredName);
        callTypes.put(callId,callType);
        if(callType==CallType.PRIVATE){
            Long receiverId = callStartRequestDto.getReceiverId();
            if(receiverId==null||receiverId.equals(callerId)){
                activeCallManager.removeCall(callId);
                callTypes.remove(callId);
                return;
            }
            privateCallReceivers.put(callId,receiverId);
            String receiverName = onlineUserManager.getOnlineUsers().get(receiverId);
            if(receiverName==null){
                sendNotification(callerId,receiverId,null,callId,"USER_OFFLINE");
                activeCallManager.removeCall(callId);
                callTypes.remove(callId);
                privateCallReceivers.remove(callId);
                return;
            }
            sendNotification(callerId,receiverId,receiverName,callId,"RINGING");
            return;
        }
        onlineUserManager.getOnlineUsers().forEach((userId, userName) -> {
            if(!userId.equals(callerId) && !activeCallManager.isParticipant(callId,userId)){
                sendNotification(callerId,userId,userName,callId,"RINGING");
            }
        });
    }

    @Override
    public void acceptCall(CallAcceptRequestDto callAcceptRequestDto) {
        String callId = callAcceptRequestDto.getCallId();
        Long callerId = callAcceptRequestDto.getCallerId();
        Long acceptorId = callAcceptRequestDto.getAcceptorId();
        if(callId==null||callerId==null||acceptorId==null||!activeCallManager.isCallActive(callId)){
            if(acceptorId!=null){
                sendNotification(callerId,acceptorId,null,callId,"ENDED");
            }
            return;
        }
        CallType callType = callTypes.get(callId);
        if(callType==null){
            sendNotification(callerId,acceptorId,null,callId,"ENDED");
            return;
        }
        if(callType==CallType.PRIVATE){
            Long intendedReceiver = privateCallReceivers.get(callId);
            if(intendedReceiver==null || !intendedReceiver.equals(acceptorId) || !activeCallManager.isParticipant(callId, callerId)){
                sendNotification(callerId, acceptorId, null, callId,"NOT_ALLOWED");
                return;
            }
        }
        if(activeCallManager.isParticipant(callId, acceptorId)){
            sendParticipantToUser(callId,acceptorId);
            return;
        }
        String acceptorName = onlineUserManager.getOnlineUsers().get(acceptorId);
        if(acceptorName==null){
            sendNotification(callerId,acceptorId,null,callId,"USER_OFFLINE");
            return;
        }
        activeCallManager.addParticipant(callId, acceptorId, acceptorName);
        sendParticipantToUser(callId,acceptorId);
        Map<Long, String> participants = new HashMap<>(activeCallManager.getParticipants(callId));
        participants.remove(acceptorId);
        participants.forEach((userId, userName) -> {
            simpMessagingTemplate.convertAndSend("/topic/call/"+userId,
                    CallNotificationResponseDto.builder()
                            .callId(callId)
                            .callerId(callerId)
                            .callerName(onlineUserManager.getOnlineUsers().get(callerId))
                            .userId(acceptorId)
                            .userName(acceptorName)
                            .status("USER_JOINED")
                            .callType(callType)
                            .build());
        });
    }

    @Override
    public void rejectCall(CallRejectRequestDto callRejectRequestDto) {
        String callId = callRejectRequestDto.getCallId();
        Long callerId = callRejectRequestDto.getCallerId();
        Long rejecterId = callRejectRequestDto.getRejecterId();
        if(callId==null||callerId==null||rejecterId==null||
                !activeCallManager.isCallActive(callId)||
                !activeCallManager.isParticipant(callId,callerId)){
            return;
        }
        if(callTypes.get(callId) == CallType.PRIVATE && !rejecterId.equals(privateCallReceivers.get(callId))){
            return;
        }
        simpMessagingTemplate.convertAndSend("/topic/call/"+callerId,
                CallNotificationResponseDto.builder()
                        .callId(callId)
                        .callerId(callerId)
                        .userId(rejecterId)
                        .userName(onlineUserManager.getOnlineUsers().get(rejecterId))
                        .status("REJECTED")
                        .callType(callTypes.get(callId))
                        .build()
        );
        if(callTypes.get(callId) == CallType.PRIVATE){
            activeCallManager.removeCall(callId);
            callTypes.remove(callId);
            privateCallReceivers.remove(callId);
        }
    }

    @Override
    public void endCall(CallEndRequestDto callEndRequestDto) {
        String callId = callEndRequestDto.getCallId();
        Long userId = callEndRequestDto.getUserId();
        if(callId==null||userId==null||!activeCallManager.callExists(callId)||!activeCallManager.isParticipant(callId, userId)){
            return;
        }
        boolean endForEveryone = Boolean.TRUE.equals(callEndRequestDto.getEndForEveryone());
        CallType callType = callTypes.get(callId);
        if(endForEveryone||callType==CallType.PRIVATE){
            Set<Long> recipients = new HashSet<>(activeCallManager.getParticipants(callId).keySet());
            recipients.remove(userId);
            recipients.forEach((recipientId) -> sendNotification(userId,recipientId,null,callId,"ENDED"));
            activeCallManager.removeCall(callId);
            callTypes.remove(callId);
            privateCallReceivers.remove(callId);
            return;
        }
        activeCallManager.removeParticipant(callId, userId);
        activeCallManager.getParticipants(callId).forEach((recipientId,name)->
                sendNotification(userId,recipientId,name,callId,"LEFT")
        );
        if(!activeCallManager.isCallActive(callId)){
            callTypes.remove(callId);
            privateCallReceivers.remove(callId);
        }
    }

    @Override
    public void sendOffer(CallOfferRequestDto callOfferRequestDto) {
        if(!isValidSignalRoute(callOfferRequestDto.getCallId(),callOfferRequestDto.getSenderId(),callOfferRequestDto.getReceiverId())){
            return;
        }
        simpMessagingTemplate.convertAndSend(
                "/topic/call/"+callOfferRequestDto.getReceiverId(),
                CallOfferResponseDto.builder()
                        .senderId(callOfferRequestDto.getSenderId())
                        .receiverId(callOfferRequestDto.getReceiverId())
                        .offer(callOfferRequestDto.getOffer())
                        .callId(callOfferRequestDto.getCallId())
                        .build());
    }

    @Override
    public void sendAnswer(CallAnswerRequestDto callAnswerRequestDto) {
        if(!isValidSignalRoute(callAnswerRequestDto.getCallId(),callAnswerRequestDto.getSenderId(),callAnswerRequestDto.getReceiverId())){
            return;
        }
        simpMessagingTemplate.convertAndSend(
                "/topic/call/"+callAnswerRequestDto.getReceiverId(),
                CallAnswerResponseDto.builder()
                        .senderId(callAnswerRequestDto.getSenderId())
                        .receiverId(callAnswerRequestDto.getReceiverId())
                        .answer(callAnswerRequestDto.getAnswer())
                        .callId(callAnswerRequestDto.getCallId())
                        .build()
        );
    }

    @Override
    public void sendIceCandidate(IceCandidateRequestDto iceCandidateRequestDto) {
        if(!isValidSignalRoute(iceCandidateRequestDto.getCallId(),iceCandidateRequestDto.getSenderId(),iceCandidateRequestDto.getReceiverId())){
            return;
        }
        simpMessagingTemplate.convertAndSend(
                "/topic/call/"+iceCandidateRequestDto.getReceiverId(),
                IceCandidateResponseDto.builder()
                        .senderId(iceCandidateRequestDto.getSenderId())
                        .receiverId(iceCandidateRequestDto.getReceiverId())
                        .candidate(iceCandidateRequestDto.getCandidate())
                        .callId(iceCandidateRequestDto.getCallId())
                        .build()
        );
    }

    private void sendNotification(Long callerId, Long userId, String userName, String callId, String status) {
        String callerName = callerId==null?"":onlineUserManager.getOnlineUsers().get(callerId);
        simpMessagingTemplate.convertAndSend(
                "/topic/call/"+userId,
                CallNotificationResponseDto.builder()
                        .callId(callId)
                        .callerId(callerId)
                        .callerName(callerName)
                        .userId(userId)
                        .userName(userName)
                        .status(status)
                        .callType(callTypes.get(callId))
                        .build()
        );
    }

    private void sendParticipantToUser(String callId, Long userId) {
        Map<Long,String> participants = new HashMap<>(activeCallManager.getParticipants(callId));
        participants.remove(userId);
        simpMessagingTemplate.convertAndSend("/topic/call/"+userId,CallParticipantsResponseDto.builder()
                        .callId(callId)
                        .status("PARTICIPANTS")
                        .participants(participants)
                        .build());
    }

    private boolean isValidSignalRoute(String callId, Long senderId, Long receiverId) {
        return callId!=null &&
                senderId!=null &&
                receiverId!=null &&
                activeCallManager.isCallActive(callId) &&
                activeCallManager.isParticipant(callId,senderId) &&
                activeCallManager.isParticipant(callId,receiverId);
    }
}
