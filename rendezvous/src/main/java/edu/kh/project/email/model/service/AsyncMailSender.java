package edu.kh.project.email.model.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsyncMailSender {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine; // 타임리프 엔진 주입

    // 이 메서드는 별도 스레드에서 돔. 여기서 3초 걸려도 사용자는 바로 응답 받음.
    @Async("mailExecutor") 
    public void sendHtmlMail(String toEmail, String subject, String templateName, Context context) {
        try {
            // 1. 타임리프 템플릿을 HTML 문자열로 변환 (겁나 빠름)
            // templateName: "email/signup-auth" (경로는 templates 폴더 기준)
            String htmlContent = templateEngine.process(templateName, context);

            // 2. 메일 전송 (이게 느린 거임 -> 근데 비동기라 상관없음)
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true: HTML 모드

            javaMailSender.send(mimeMessage);
            System.out.println("메일 전송 성공: " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
            // 비동기라 여기서 에러나도 사용자한테는 모름. 에러 로그 잘 남겨야 함.
        }
    }
}