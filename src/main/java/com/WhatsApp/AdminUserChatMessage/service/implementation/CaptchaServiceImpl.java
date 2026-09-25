package com.WhatsApp.AdminUserChatMessage.service.implementation;

import com.WhatsApp.AdminUserChatMessage.dto.responseDto.CaptchaResponseDto;
import com.WhatsApp.AdminUserChatMessage.entity.Captcha;
import com.WhatsApp.AdminUserChatMessage.exception.InvalidCaptchaException;
import com.WhatsApp.AdminUserChatMessage.repository.CaptchaRepository;
import com.WhatsApp.AdminUserChatMessage.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final CaptchaRepository captchaRepository;

    private String generateCaptchaText(){
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder captchaText = new StringBuilder();
        for(int i = 0; i < 6; i++){
            captchaText.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return captchaText.toString();
    }

    private BufferedImage generateCaptchaImage(String captchaText){
        int width = 200;
        int height = 200;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
        Random rnd = new Random();
        for(int i=0;i<10;i++){
            g2d.setColor(new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
            int x1 = rnd.nextInt(width);
            int y1 = rnd.nextInt(height);
            int x2 = rnd.nextInt(width);
            int y2 = rnd.nextInt(height);
            g2d.drawLine(x1, y1, x2, y2);
        }
        for(int i=0;i<100;i++){
            g2d.setColor(new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
            int x = rnd.nextInt(width);
            int y = rnd.nextInt(height);
            g2d.fillOval(x, y, 2, 2);
        }
        g2d.setFont(new Font("Times New Roman", Font.BOLD, 30));
        g2d.setColor(Color.BLACK);
        g2d.drawString(captchaText, 50, 50);
        g2d.dispose();
        return image;
    }

    private String convertToBase64(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return String.format("data:image/%s;base64,%s", format, Base64.getEncoder().encodeToString(baos.toByteArray()));
    }

    @Override
    public CaptchaResponseDto generateImageCaptcha() {
        List<Captcha> existingCaptcha = captchaRepository.findAll();
        existingCaptcha.forEach(captcha -> {
            captcha.setIsValid(false);
            captcha.setExpiresAt(LocalDateTime.now());
        });
        captchaRepository.saveAll(existingCaptcha);
        String captchaText = generateCaptchaText();
        BufferedImage image = generateCaptchaImage(captchaText);
        String imageBase64 = null;
        try {
            imageBase64 = convertToBase64(image, "png");
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate captcha image");
        }
        Captcha captcha = Captcha.builder()
                .captchaId(UUID.randomUUID().toString())
                .captchaAnswer(captchaText)
                .captchaImage(imageBase64)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .createdAt(LocalDateTime.now())
                .build();
        captchaRepository.save(captcha);
        return new CaptchaResponseDto(captcha.getCaptchaId(), captcha.getCaptchaAnswer(), captcha.getCaptchaImage());
    }

    @Override
    public void validateCaptcha(String captchaId, String captchaAnswer) {
        if(captchaId == null || captchaId.isBlank() || captchaAnswer == null || captchaAnswer.isBlank()){
            throw new InvalidCaptchaException("Captcha is Required");
        }
        Captcha captcha = captchaRepository.findByCaptchaId(captchaId).orElseThrow(()->new InvalidCaptchaException("Captcha Not Found"));
        if(!captcha.getExpiresAt().isAfter(LocalDateTime.now())){
            throw new InvalidCaptchaException("Captcha Expired. Please request a new one");
        }
        if(!captcha.getCaptchaAnswer().equals(captchaAnswer)){
            throw new InvalidCaptchaException("Invalid Captcha");
        }
        captcha.setIsValid(true);
        captcha.setIsVerified(true);
        captcha.setExpiresAt(LocalDateTime.now().plusSeconds(30));
        captchaRepository.save(captcha);
    }
}
