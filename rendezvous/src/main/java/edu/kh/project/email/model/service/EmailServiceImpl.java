package edu.kh.project.email.model.service;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.email.model.mapper.EmailMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class EmailServiceImpl implements EmailService {

	private final EmailMapper mapper;
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	private String loadHtml(String htmlName, Context context) {
		return templateEngine.process("email/" + htmlName, context);
	}

	/**
	 * 회원가입 승인 메일 발송 (AdminServiceImpl에서 호출)
	 */
	@Override
	public int sendApproveEmail(String htmlName, String email) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setTo(email);
			helper.setSubject("[Rendezvous] 회원가입 승인 완료 메일입니다.");

			Context context = new Context();
			// html 템플릿에 들어갈 변수 설정
			context.setVariable("message", "회원가입 승인이 완료되었습니다.<br>이제 서비스를 자유롭게 이용하세요.");

			String htmlContent = loadHtml(htmlName, context);
			helper.setText(htmlContent, true);
			helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

			mailSender.send(mimeMessage);
			return 1;
		} catch (Exception e) {
			log.error("회원가입 승인 메일 발송 중 에러 발생 : {}", e.getMessage());
			return 0;
		}
	}

	/**
	 * QnA 답변 메일 발송 + DB 업데이트
	 */
	@Override
	public int sendQnaEmail(String htmlName, Map<String, String> params) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			String email = params.get("email");
			String qnaNo = params.get("qnaNo"); // React에서 보낸 qnaNo 받기

			// 유효성 검사
			if (email == null || email.isEmpty()) {
				log.error("메일 수신자 주소(email)가 누락되었습니다.");
				return 0;
			}

			helper.setTo(email);
			helper.setSubject("[Rendezvous] QnA 문의에 대한 답변입니다.");

			Context context = new Context();
			context.setVariable("title", params.get("title"));
			context.setVariable("content", params.get("content"));

			String htmlContent = loadHtml(htmlName, context);
			helper.setText(htmlContent, true);
			helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

			// 1. 메일 발송
			mailSender.send(mimeMessage);

			// 2. DB 업데이트 (추가된 부분)
			// params에는 qnaNo, content(답변내용) 등이 들어있습니다.
			int updateResult = mapper.updateQnaStatus(params);

			if (updateResult > 0) {
				log.info("QnA 답변 완료 처리 성공 (번호: {})", qnaNo);
				return 1;
			} else {
				log.error("메일은 발송되었으나 DB 상태 업데이트 실패 (번호: {})", qnaNo);
				throw new RuntimeException("DB 상태 업데이트 실패");
			}

		} catch (Exception e) {
			log.error("QnA 메일 발송 중 에러 발생 : {}", e.getMessage());
			// 트랜잭션 롤백을 위해 예외를 다시 던짐
			throw new RuntimeException("메일 서비스 처리 중 오류 발생", e);
		}
	}

	/**
	 * Support 답변 메일 발송 + DB 업데이트
	 */
	@Override
	public int sendSupplyEmail(String htmlName, Map<String, String> params) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			String email = params.get("email");
			String supportNo = params.get("supportNo");

			if (email == null || email.isEmpty()) {
				throw new RuntimeException("수신자 이메일 주소가 없습니다.");
			}

			helper.setTo(email);
			helper.setSubject("[Rendezvous] 문의하신 내용에 대한 답변입니다.");

			Context context = new Context();
			context.setVariable("title", params.get("title"));
			context.setVariable("content", params.get("content"));

			String htmlContent = loadHtml(htmlName, context);
			helper.setText(htmlContent, true);
			helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

			mailSender.send(mimeMessage);

			int updateResult = mapper.updateSupportStatus(params);

			if (updateResult > 0) {
				log.info("답변 완료 처리 성공 (번호: {})", supportNo);
				return 1;
			} else {
				throw new RuntimeException("DB 업데이트 실패 (번호: " + supportNo + ")");
			}

		} catch (Exception e) {
			log.error("메일 발송 중 오류: {}", e.getMessage());
			throw new RuntimeException("메일 발송 실패", e);
		}
	}
}