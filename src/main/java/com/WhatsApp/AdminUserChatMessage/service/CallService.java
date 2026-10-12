package com.WhatsApp.AdminUserChatMessage.service;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.*;

public interface CallService {

    void online(CallStartRequestDto callStartRequestDto);

    void startCall(CallStartRequestDto callStartRequestDto);

    void acceptCall(CallAcceptRequestDto callAcceptRequestDto);

    void rejectCall(CallRejectRequestDto callRejectRequestDto);

    void endCall(CallEndRequestDto callEndRequestDto);

    void sendOffer(CallOfferRequestDto callOfferRequestDto);

    void sendAnswer(CallAnswerRequestDto callAnswerRequestDto);

    void sendIceCandidate(IceCandidateRequestDto iceCandidateRequestDto);

}
