package com.spa.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Boc logic gui email that qua SMTP. Khi notification.email.enabled=false
 * (mac dinh, xem application.yaml) - KHONG goi that ra ngoai, chi log ra
 * console, de chay duoc dev/demo ma khong can tai khoan SMTP that. Khi nhom
 * co SMTP that (Gmail App Password, SendGrid...), chi can dien lai
 * spring.mail.* + doi notification.email.enabled=true, khong phai sua code.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${notification.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${notification.email.from}")
    private String fromAddress;

    /**
     * @return true neu gui thanh cong (hoac dang o che do mock), false neu
     * gui that that bai - caller dung gia tri nay de quyet dinh status
     * SENT/FAILED cua Notification.
     */
    public boolean send(String to, String subject, String content) {
        if (!emailEnabled) {
            log.info("[MOCK EMAIL] to={}, subject={}, content={}", to, subject, content);
            return true;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("Gửi email thất bại tới {}: {}", to, e.getMessage());
            return false;
        }
    }
}
