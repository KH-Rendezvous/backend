package edu.kh.project.email.model.service;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.email.model.mapper.EmailMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
	private final EmailMapper mapper;
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	private String loadHtml(String htmlName, Context context) {
		// templates/email 폴더 안의 html 파일을 읽음
		return templateEngine.process("email/" + htmlName, context);
	}

	@Override
	public int sendApproveEmail(String htmlName, String email) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject("[Rendezvous] 회원가입 승인 완료 메일입니다.");

			Context context = new Context();
			context.setVariable("message", "회원가입 승인이 완료되었습니다.<br>이제 서비스를 자유롭게 이용하세요.");

			String htmlContent = loadHtml(htmlName, context);
			helper.setText(htmlContent, true);

			helper.addInline("logo", new ClassPathResource("static/images/logo.png"));
			mailSender.send(mimeMessage);
			return 1;
		} catch (Exception e) {
			log.error("메일 발송 중 에러 발생 : {}", e.getMessage());
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public int sendQnaEmail(String htmlName, Map<String, String> params) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			String email = params.get("email");
			String title = params.get("title");
			String content = params.get("content");

			if (email == null || email.isEmpty()) {
				log.error("메일 수신자 주소(email)가 누락되었습니다.");
				return 0;
			}

			helper.setTo(email);
			helper.setSubject("[Rendezvous] 문의하신 내용에 대한 답변입니다.");

			Context context = new Context();
			context.setVariable("title", title); // HTML의 ${title} 에 매핑
			context.setVariable("content", content); // HTML의 ${content} 에 매핑

			String htmlContent = loadHtml(htmlName, context);
			helper.setText(htmlContent, true);

			helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

			mailSender.send(mimeMessage);
			return 1;

		} catch (Exception e) {
			log.error("QnA 메일 발송 중 에러 발생 : {}", e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int sendAnswerEmail(String htmlName, String email) {

		return 0;
	}

}
