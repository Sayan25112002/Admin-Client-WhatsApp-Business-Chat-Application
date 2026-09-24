package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.dto.requestDto.LoginRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.requestDto.RegisterRequestDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.LoginResponseDto;
import com.WhatsApp.AdminUserChatMessage.dto.responseDto.RegisterResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.Captcha;
import com.WhatsApp.AdminUserChatMessage.entity.OtpVerification;
import com.WhatsApp.AdminUserChatMessage.entity.User;
import com.WhatsApp.AdminUserChatMessage.entity.type.Role;
import com.WhatsApp.AdminUserChatMessage.entity.type.UserStatus;
import com.WhatsApp.AdminUserChatMessage.repository.CaptchaRepository;
import com.WhatsApp.AdminUserChatMessage.repository.OtpVerificationRepository;
import com.WhatsApp.AdminUserChatMessage.repository.UserRepository;
import com.WhatsApp.AdminUserChatMessage.security.AuthUtil;
import com.WhatsApp.AdminUserChatMessage.service.AuthenticationService;
import com.WhatsApp.AdminUserChatMessage.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        validateCaptcha(loginRequestDto.getCaptchaId(),loginRequestDto.getCaptchaAnswer());
        User user = userRepository
                .findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("Email Not Registered"));
        if(!passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword())){
            throw new UsernameNotFoundException("Wrong Password");
        }
        String accessToken = authUtil.generateAccessToken(user);
        String refreshToken = authUtil.generateRefreshToken(user);
        refreshTokenService.saveRefreshToken(user,refreshToken);
        return LoginResponseDto
                .builder()
                .message("Login Successful")
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .verified(true)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .contact(user.getContact())
                .role(user.getRole())
                .userStatus(user.getStatus())
                .build();
    }

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        validateCaptcha(registerRequestDto.getCaptchaId(),registerRequestDto.getCaptchaAnswer());
        if(userRepository.existsByEmail(registerRequestDto.getEmail())){
            throw new UsernameNotFoundException("Email Already Exists. Please Login");
        }
        OtpVerification otpVerification = otpVerificationRepository
                .findByVerificationId(registerRequestDto.getVerificationId())
                .orElseThrow(()->new RuntimeException("Verification ID Not Found"));
        if(!Boolean.TRUE.equals(otpVerification.getEmailVerified())){
            throw new RuntimeException("Please Verify Your Email First");
        }
        if(!Boolean.TRUE.equals(otpVerification.getContactVerified())){
            throw new RuntimeException("Please Verify Your Contact First");
        }
        if(!otpVerification.getEmail().equals(registerRequestDto.getEmail())){
            throw new RuntimeException("Email Doesn't Match Verified Email");
        }
        if(!otpVerification.getContact().equals(registerRequestDto.getContact())){
            throw new RuntimeException("Contact Doesn't Match Verified Contact");
        }
        User user = User.builder()
                .name(registerRequestDto.getName())
                .email(registerRequestDto.getEmail())
                .contact(registerRequestDto.getContact())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(Role.CLIENT)
                .status(UserStatus.OFFLINE)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return RegisterResponseDto.builder()
                .message("Registration Successful. Please Login")
                .verificationId(registerRequestDto.getVerificationId())
                .build();
    }

    private void validateCaptcha(String captchaId, String captchaAnswer) {
        Captcha captcha = captchaRepository
                .findByCaptchaId(captchaId)
                .orElseThrow(()->new RuntimeException("Captcha Not Found"));
        if(Boolean.TRUE.equals(captcha.getUsed())){
            throw new RuntimeException("Captcha Already Used");
        }
        if(captcha.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Captcha Expired");
        }
        if(!captcha.getCaptchaAnswer().equals(captchaAnswer)){
            throw new RuntimeException("Invalid Captcha Answer");
        }
        captcha.setUsed(true);
        captcha.setIsVerified(true);
        captcha.setIsValid(true);
        captchaRepository.save(captcha);
    }
}
