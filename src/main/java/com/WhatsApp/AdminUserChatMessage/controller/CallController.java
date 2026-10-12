package com.WhatsApp.AdminUserChatMessage.controller;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.*;
import com.WhatsApp.AdminUserChatMessage.service.CallService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;

    @MessageMapping("/call.online")
    public void online(@Payload CallStartRequestDto callStartRequestDto){
        callService.online(callStartRequestDto);
    }

    @MessageMapping("/call.start")
    public void startCall(@Payload CallStartRequestDto callStartRequestDto, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("callUserId", callStartRequestDto.getCallerId());
        callService.startCall(callStartRequestDto);
    }

    @MessageMapping("/call.accept")
    public void acceptCall(@Payload CallAcceptRequestDto callAcceptRequestDto, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("callUserId", callAcceptRequestDto.getAcceptorId());
        callService.acceptCall(callAcceptRequestDto);
    }

    @MessageMapping("/call.reject")
    public void rejectCall(CallRejectRequestDto callRejectRequestDto, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("callUserId", callRejectRequestDto.getRejecterId());
        callService.rejectCall(callRejectRequestDto);
    }

    @MessageMapping("/call.end")
    public void endCall(CallEndRequestDto callEndRequestDto){
        callService.endCall(callEndRequestDto);
    }

    @MessageMapping("/call.offer")
    public void sendOffer(CallOfferRequestDto callOfferRequestDto){
        callService.sendOffer(callOfferRequestDto);
    }

    @MessageMapping("/call.answer")
    public void sendAnswer(CallAnswerRequestDto callAnswerRequestDto){
        callService.sendAnswer(callAnswerRequestDto);
    }

    @MessageMapping("/call.ice")
    public void sendIceCandidate(IceCandidateRequestDto iceCandidateRequestDto){
        callService.sendIceCandidate(iceCandidateRequestDto);
    }

}
