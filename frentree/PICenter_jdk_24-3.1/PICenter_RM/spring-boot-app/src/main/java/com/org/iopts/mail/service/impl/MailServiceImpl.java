package com.org.iopts.mail.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.inject.Inject;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.http.ParseException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.org.iopts.csp.comm.Config;
import com.org.iopts.csp.comm.CspUtil;
import com.org.iopts.csp.comm.vo.HeaderVo;
import com.org.iopts.csp.comm.vo.ResultVo;
import com.org.iopts.detection.dao.piApprovalDAO;
import com.org.iopts.mail.dao.MailDAO;
import com.org.iopts.mail.service.MailService;
import com.org.iopts.mail.vo.MailVo;
import com.org.iopts.mail.vo.UserVo;
import com.org.iopts.setting.dao.Pi_SetDAO;
import com.org.iopts.util.SessionUtil;

import com.google.gson.JsonArray;

@Service
@Transactional
public class MailServiceImpl implements MailService {

	private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Value("${recon.id}")
	private String recon_id;

	@Value("${recon.password}")
	private String recon_password;

	@Value("${recon.url}")
	private String recon_url;

	@Value("${recon.api.version}")
	private String api_ver;

	@Value("${user.key}")
	private String key;
	
	@Value("${send.host}")
	private String send_host;
	
	@Value("${send.port}")
	private String send_port;
	
	@Inject
	private piApprovalDAO approvalDao;
	
	@Inject
	private Pi_SetDAO setDao;
	
	@Inject
	private MailDAO dao;
	
	private static final String ALGORITHM = "AES";
	
	/*CBH 메일 전송*/
	private static String title = "";
	private static String content = "";
	/*private static String sendmail = "";*/
	private static String receivermail = "";
	
	
	@Override
	public Map<String, Object> serverGroupMailContent(HttpServletRequest request) throws Exception {
		int mailType = Integer.parseInt(request.getParameter("mailType"));
		Map<String, Object> userMap = new HashMap<>();
		/*List<MailVo> mailList = dao.selectDate();*/
		
		 String mail_con = "";
		 String mailFlag = "";
		 
		 Map<String, Object> contentList = setDao.selectContentList("mail_scan"+mailType);
		 
		 title = contentList.get("NAME").toString();
		 mail_con = contentList.get("CON").toString();
		 
		userMap.put("mail_con", mail_con);
		userMap.put("title", title);
		return userMap;
	}
	@Override
	public Map<String, List<String>> serverGroupMail(HttpServletRequest request) throws Exception {
		// JSP에서 받은 파라미터 (user_no)
		String assetnosch = request.getParameter("assetnosch");
		String apnosch = request.getParameter("apnosch");
		String detailCon = request.getParameter("detailCon");
		String mailTitle = request.getParameter("mailTitle");
		String senderUserNo = request.getParameter("mailSender");      // 발신자 user_no
		String receiverUserNos = request.getParameter("mailReceiver"); // 수신자 user_no (콤마 구분)
		String ccUserNos = request.getParameter("mailCc");             // 참조자 user_no (콤마 구분)

		Map<String, List<String>> resultMap = new HashMap<>();
		List<String> successList = new ArrayList<>();
		List<String> failList = new ArrayList<>();

		logger.info("=== 메일 발송 시작 ===");
		logger.info("발신자 user_no: " + senderUserNo);
		logger.info("수신자 user_no: " + receiverUserNos);
		logger.info("참조자 user_no: " + ccUserNos);
		logger.info("제목: " + mailTitle);

		// user_no를 이메일로 변환
		String senderEmail = null;
		if (senderUserNo != null && !senderUserNo.trim().isEmpty()) {
			senderEmail = dao.selectUserEmailByUserNo(senderUserNo.trim());
			logger.info("발신자 이메일: " + senderEmail);
		}

		// 수신자 user_no -> 이메일 변환
		List<String> receiverEmails = new ArrayList<>();
		if (receiverUserNos != null && !receiverUserNos.trim().isEmpty()) {
			String[] userNos = receiverUserNos.split(",");
			for (String userNo : userNos) {
				if (!userNo.trim().isEmpty()) {
					String email = dao.selectUserEmailByUserNo(userNo.trim());
					if (email != null && !email.trim().isEmpty()) {
						receiverEmails.add(email.trim());
					}
				}
			}
		}
		logger.info("수신자 이메일 목록: " + receiverEmails);

		// 참조자 user_no -> 이메일 변환
		List<String> ccEmails = new ArrayList<>();
		if (ccUserNos != null && !ccUserNos.trim().isEmpty()) {
			String[] userNos = ccUserNos.split(",");
			for (String userNo : userNos) {
				if (!userNo.trim().isEmpty()) {
					String email = dao.selectUserEmailByUserNo(userNo.trim());
					if (email != null && !email.trim().isEmpty()) {
						ccEmails.add(email.trim());
					}
				}
			}
		}
		logger.info("참조자 이메일 목록: " + ccEmails);

		// SMTP 설정 (하드코딩) - 로컬 테스트용
		final String SMTP_HOST = "smtp.worksmobile.com";
		final String SMTP_PORT = "465";
		final String SMTP_PASSWORD = "t9oLXnnECvv3";
		// SMTP 설정 (하드코딩) - LG생건 (인증 불필요)
		// final String SMTP_HOST = "203.247.132.88";
		// final String SMTP_PORT = "25";
		// final String SMTP_PASSWORD = "";  // 비어있으면 인증 없이 발송

		// DB에서 발신자 이메일 가져오기
		Map<String, Object> mailMap = dao.selectMailUser();
		String sendmail = mailMap.get("COM").toString();
		String passwd = SMTP_PASSWORD;

		// 이미지 URL (하드코딩) - 로컬 테스트용
		final String IMAGE_URL_LOGO = "http://localhost:8080/resources/assets/images/mail_title_lg.png";
		final String IMAGE_URL_ICON = "http://localhost:8080/resources/assets/images/mail_icon.png";
		// 이미지 URL (하드코딩) - LG생건
		// final String IMAGE_URL_LOGO = "https://10.40.122.248:8080/resources/assets/images/mail_title_lg.png";
		// final String IMAGE_URL_ICON = "https://10.40.122.248:8080/resources/assets/images/mail_icon.png";

		// 수신자 검증
		if (receiverEmails.isEmpty()) {
			logger.warn("수신자가 없습니다.");
			resultMap.put("success", successList);
			resultMap.put("fail", failList);
			return resultMap;
		}

		// 선택된 서버 정보 조회 및 검출건수 테이블 생성
		Gson gson = new Gson();
		JsonArray assetnoschJArr = gson.fromJson(assetnosch, JsonArray.class);
		JsonArray apnoschJArr = apnosch != null ? gson.fromJson(apnosch, JsonArray.class) : new JsonArray();
		StringBuilder serverTableContent = new StringBuilder();

		for (int i = 0; i < assetnoschJArr.size(); i++) {
			String targetId = assetnoschJArr.get(i).getAsString();
			String apNo = (apnoschJArr.size() > i) ? String.valueOf(apnoschJArr.get(i).getAsInt()) : "0";
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("target_id", targetId);
			paramMap.put("ap_no", apNo);

			// 검출 건수 조회
			Map<String, Object> summary = dao.selectTargetSummaryLg(paramMap);
			// 담당자 조회
			Map<String, Object> manager = dao.selectTargetManagerLg(paramMap);

			// summary 또는 manager 중 하나라도 있으면 행 생성
			if (summary != null || manager != null) {
				// 서버 기본 정보 (담당자 쿼리에서)
				String serverName = "-";
				String serviceNm = "-";
				String manager1 = "-";
				String manager2 = "-";
				if (manager != null) {
					serverName = manager.get("SERVER_NAME") != null ? manager.get("SERVER_NAME").toString() : "-";
					serviceNm = manager.get("SERVICE_NM") != null ? manager.get("SERVICE_NM").toString() : "-";
					manager1 = manager.get("MANAGER1") != null ? manager.get("MANAGER1").toString() : "-";
					manager2 = manager.get("MANAGER2") != null ? manager.get("MANAGER2").toString() : "-";
				}

				// 검출 건수 (검출 쿼리에서, 없으면 0)
				long totalCnt = 0, phone = 0, email = 0, account = 0, card = 0, ssn = 0, foreigner = 0, driver = 0, passport = 0;
				if (summary != null) {
					totalCnt = summary.get("TOTAL_CNT") != null ? Long.parseLong(summary.get("TOTAL_CNT").toString()) : 0;
					phone = summary.get("PHONE") != null ? Long.parseLong(summary.get("PHONE").toString()) : 0;
					email = summary.get("EMAIL") != null ? Long.parseLong(summary.get("EMAIL").toString()) : 0;
					account = summary.get("ACCOUNT") != null ? Long.parseLong(summary.get("ACCOUNT").toString()) : 0;
					card = summary.get("CARD") != null ? Long.parseLong(summary.get("CARD").toString()) : 0;
					ssn = summary.get("SSN") != null ? Long.parseLong(summary.get("SSN").toString()) : 0;
					foreigner = summary.get("FOREIGNER") != null ? Long.parseLong(summary.get("FOREIGNER").toString()) : 0;
					driver = summary.get("DRIVER") != null ? Long.parseLong(summary.get("DRIVER").toString()) : 0;
					passport = summary.get("PASSPORT") != null ? Long.parseLong(summary.get("PASSPORT").toString()) : 0;
				}

				serverTableContent.append("<tr>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(serverName).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(serviceNm).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(manager1).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(manager2).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center; background-color: #FFFF00; font-weight: bold;\">").append(formatNumber(totalCnt)).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(formatNumber(phone)).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(formatNumber(email)).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(formatNumber(account)).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(formatNumber(card)).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(formatNumber(ssn)).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(formatNumber(foreigner)).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(formatNumber(driver)).append("</td>");
				serverTableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; text-align: center;\">").append(formatNumber(passport)).append("</td>");
				serverTableContent.append("</tr>");
			}
		}

		// HTML 메일 본문 생성 (수신자 이메일 목록을 콤마로 연결해서 표시)
		String receiverEmailsStr = String.join(", ", receiverEmails);
		String content = buildMailContentLg(receiverEmailsStr, detailCon, serverTableContent.toString());

		// SMTP 설정
		Properties prop = new Properties();
		prop.put("mail.smtp.host", SMTP_HOST);
		prop.put("mail.smtp.port", SMTP_PORT);
		prop.put("mail.smtp.connectiontimeout", "10000");
		prop.put("mail.smtp.timeout", "10000");

		// 메일 세션 생성 (비밀번호 있으면 인증, 없으면 인증 없이)
		Session session;
		if (passwd != null && !passwd.isEmpty()) {
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.ssl.enable", "true");
			prop.put("mail.smtp.ssl.trust", SMTP_HOST);
			final String finalSendmail = sendmail;
			final String finalPasswd = passwd;
			session = Session.getInstance(prop, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(finalSendmail, finalPasswd);
				}
			});
		} else {
			prop.put("mail.smtp.auth", "false");
			session = Session.getInstance(prop);
		}

		String sendResult = "SUCCESS";
		String errorMsg = null;

		try {
			MimeMessage message = new MimeMessage(session);

			// 발신자 설정 (실제 발송은 SMTP 계정으로, 표시 이름만 변경)
			if (senderEmail != null && !senderEmail.trim().isEmpty()) {
				message.setFrom(new InternetAddress(sendmail, senderEmail, "UTF-8"));
			} else {
				message.setFrom(new InternetAddress(sendmail));
			}

			// 수신자 설정
			for (String receiver : receiverEmails) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver.trim()));
			}

			// 참조자 설정
			for (String cc : ccEmails) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc.trim()));
			}

			// 제목 설정
			message.setSubject("[PICenter] " + mailTitle, "UTF-8");

			// 본문 설정
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(content, "text/html; charset=UTF-8");

			// 이미지 첨부 (로고)
			MimeBodyPart imagePart1 = new MimeBodyPart();
			try {
				// SSL 인증서 무시 설정
				javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{
					new javax.net.ssl.X509TrustManager() {
						public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
						public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
						public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
					}
				};
				javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, session1) -> true);

				InputStream imageStream1 = new URL(IMAGE_URL_LOGO).openStream();
				DataSource fds1 = new ByteArrayDataSource(imageStream1, "image/png");
				imagePart1.setDataHandler(new DataHandler(fds1));
				imagePart1.setHeader("Content-ID", "<lg_logo>");
				imagePart1.setDisposition(MimeBodyPart.INLINE);
			} catch (Exception e) {
				logger.warn("로고 이미지 로드 실패: " + e.getMessage());
			}

			// 이미지 첨부 (아이콘)
			MimeBodyPart imagePart2 = new MimeBodyPart();
			try {
				InputStream imageStream2 = new URL(IMAGE_URL_ICON).openStream();
				DataSource fds2 = new ByteArrayDataSource(imageStream2, "image/png");
				imagePart2.setDataHandler(new DataHandler(fds2));
				imagePart2.setHeader("Content-ID", "<mail_logo>");
				imagePart2.setDisposition(MimeBodyPart.INLINE);
			} catch (Exception e) {
				logger.warn("아이콘 이미지 로드 실패: " + e.getMessage());
			}

			// 멀티파트 결합
			MimeMultipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(imagePart1);
			multipart.addBodyPart(imagePart2);

			message.setContent(multipart);

			// 메일 발송
			Transport.send(message);

			successList.addAll(receiverEmails);
			logger.info("메일 발송 성공: " + receiverEmails);

		} catch (Exception e) {
			failList.addAll(receiverEmails);
			sendResult = "FAIL";
			errorMsg = e.getMessage();
			logger.error("메일 발송 실패: " + e.getMessage(), e);
			throw e;
		} finally {
			// 메일 발송 이력 저장
			try {
				saveMailHistory(mailTitle, detailCon, senderUserNo, senderEmail,
						receiverEmails, ccEmails, sendResult, errorMsg,
						assetnoschJArr, apnoschJArr);
			} catch (Exception e) {
				logger.error("메일 이력 저장 실패: " + e.getMessage(), e);
			}
		}

		resultMap.put("success", successList);
		resultMap.put("fail", failList);

		logger.info("=== 메일 발송 완료 ===");
		return resultMap;
	}

	// HTML 메일 본문 생성 (LG생건 3단 헤더 테이블)
	private String buildMailContentLg(String receiverInfo, String detailCon, String serverTableContent) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body style=\"font-family: 'Noto Sans KR', sans-serif; padding: 0; margin: 0;\">");
		sb.append("<table style=\"border: 1px solid #ccc;\" border=\"0\" width=\"1400\" cellspacing=\"0\" align=\"center\">");
		sb.append("<tbody><tr><td align=\"center\">");

		// 헤더 (로고)
		sb.append("<table style=\"border-bottom: 2px solid #000;\" border=\"0\" width=\"1350\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td>");
		sb.append("<img src=\"cid:lg_logo\" style=\"width: 100%; max-width: 1350px;\" />");
		sb.append("</td></tr></tbody></table>");

		// 아이콘 및 안내문구
		sb.append("<table border=\"0\" width=\"1350\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td style=\"height: 60px;\" align=\"center\" valign=\"bottom\">");
		sb.append("<img src=\"cid:mail_logo\" style=\"vertical-align: bottom;\" width=\"50\" height=\"50\" />");
		sb.append("</td></tr><tr>");
		sb.append("<td style=\"font-size: 12px; color: #999; font-weight: bold; height: 20px;\" align=\"center\" valign=\"bottom\">");
		sb.append("개인정보 검출관리센터(PICenter)에서 발송되는 안내 메일입니다.</td>");
		sb.append("</tr><tr><td style=\"height: 20px;\">&nbsp;</td></tr></tbody></table>");

		// 본문 내용
		sb.append("<table style=\"border: 1px solid #cccccc; padding: 20px;\" border=\"0\" width=\"1350\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td style=\"padding: 20px;\">");

		// 수신자 정보
		sb.append("<table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr>");
		sb.append("<td style=\"font-size: 13px; color: #999; font-weight: bold; width: 80px;\">수신자</td>");
		sb.append("<td style=\"font-size: 13px; color: #222; font-weight: bold;\">").append(receiverInfo != null ? receiverInfo : "-").append("</td>");
		sb.append("</tr></tbody></table>");

		// 메일 내용
		sb.append("<div style=\"margin-top: 20px; padding: 15px; background-color: #f9f9f9; border-radius: 5px;\">");
		sb.append("<pre style=\"font-family: 'Noto Sans KR', sans-serif; font-size: 13px; line-height: 1.6; white-space: pre-wrap; margin: 0;\">").append(detailCon != null ? detailCon : "").append("</pre>");
		sb.append("</div>");

		// DB 개인정보 검출 결과 테이블 (3단 헤더)
		if (serverTableContent != null && !serverTableContent.isEmpty()) {
			sb.append("<div style=\"margin-top: 20px;\">");
			sb.append("<p style=\"font-size: 13px; color: #999999; font-weight: bold; margin-bottom: 10px;\">서버별 개인정보 검출 결과</p>");
			sb.append("<table style=\"width: 1200px; border-collapse: collapse; font-size: 12px; text-align: center; table-layout: fixed;\">");
			// 헤더 1단 (rowspan/colspan 구조)
			sb.append("<thead>");
			sb.append("<tr>");
			sb.append("<td rowspan=\"2\" style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5; width: 200px;\">서버명</td>");
			sb.append("<td rowspan=\"2\" style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5; width: 120px;\">용도</td>");
			sb.append("<td rowspan=\"2\" style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">담당자(정)</td>");
			sb.append("<td rowspan=\"2\" style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">담당자(부)</td>");
			sb.append("<td rowspan=\"2\" style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">총 검출 건수</td>");
			sb.append("<td colspan=\"2\" style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">개인정보</td>");
			sb.append("<td colspan=\"2\" style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">금융정보</td>");
			sb.append("<td colspan=\"4\" style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">고유식별정보</td>");
			sb.append("</tr>");
			// 헤더 2단
			sb.append("<tr>");
			sb.append("<td style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">휴대폰 번호</td>");
			sb.append("<td style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">이메일</td>");
			sb.append("<td style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">계좌번호</td>");
			sb.append("<td style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">카드번호</td>");
			sb.append("<td style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">주민등록번호</td>");
			sb.append("<td style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">외국인번호</td>");
			sb.append("<td style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">운전면허번호</td>");
			sb.append("<td style=\"white-space: nowrap; font-weight: bold; border: 1px solid #cccccc; background-color: #f5f5f5;\">여권번호</td>");
			sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>").append(serverTableContent).append("</tbody>");
			sb.append("</table>");
			// 상세정보 확인 링크
			sb.append("<p style=\"margin-top: 15px; font-size: 13px;\">");
			sb.append("<span style=\"background-color: #e6e6e6; padding: 5px 10px;\">상세정보 확인 (<a href=\"http://picenter.lghnh.com\" target=\"_blank\">http://picenter.lghnh.com</a>)</span>");
			sb.append("</p>");
			sb.append("</div>");
		}

		sb.append("</td></tr></tbody></table>");

		// 푸터
		sb.append("<table border=\"0\" width=\"1400\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td style=\"height: 40px;\">&nbsp;</td></tr></tbody></table>");
		sb.append("<table style=\"border-top: 1px solid #ccc; width: 100%;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td bgcolor=\"#e6e6e6\" height=\"60\" align=\"center\">");
		sb.append("<span style=\"font-size: 12px; color: #666; font-weight: bold;\">Personal Information Center</span>");
		sb.append("</td></tr></tbody></table>");

		sb.append("</td></tr></tbody></table></body>");

		return sb.toString();
	}

	// 숫자 포맷팅 (천 단위 콤마)
	private String formatNumber(long number) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("###,###");
		return df.format(number);
	}   
	
	@Override
	public List<UserVo> approvalSendMail(HashMap<String, Object> params) throws Exception {
		
		logger.info("params >>>>> " + params);
		String user_no = SessionUtil.getSession("memberSession", "USER_NO");
		String user_name = SessionUtil.getSession("memberSession", "USER_NAME");
		String team_name = SessionUtil.getSession("memberSession", "TEAM_NAME");
		params.put("user_no", user_no);
		
		List<UserVo> selectSendUserList = new ArrayList<>();
		List<UserVo> selectUserList = new ArrayList<>();
		
		selectSendUserList = dao.selectUserList(params);
		selectUserList = dao.selectSendUserList(params);
		
		if(selectUserList.get(0).getUser_email() != null) {
			content = "";
			receivermail = selectUserList.get(0).getUser_email();
			/*String _tnetproxy_url = "http://tnetproxy.sktelecom.com:8081/proxy/notes_redirect.jsp?RequestURL=http://hqappr.netswork.co.kr/sktaprv/approval.nsf/fssplitmainframe?readform&frame=fsleft_3&db=approval&ele=ing?openview&Start=1&Count=18";
			String _tnetproxy_url_ = "http://tnetproxy.sktelecom.com";*/
			String _edms_url = "http://tnet.sktelecom.com/SmartTalk/Main/Pages/Main.aspx";
			String _url = "https://pimc.sktelecom.com";
			String _type = "조치계획 ";
			String _type2 = "요청 의견 ";
			String email = "pimc@sktelecom.com";
			String number = "02-6400-8842";
			String SK_title_con1 =  "<p>개인정보보호법 및 그룹 자경단 개인정보관리실태 점검 기준에 따라 당사 운용 서버 </p>\r\n" + 
									"<p>내 불필요하게 저장된 개인정보 파일에 대한 정기 검출 및 삭제/보호 조치 작업을 </p>\r\n" + 
									"<p> 진행 중에 있습니다.</p>\r\n";
			
			String SK_title_con2 =  "<p>금번 개인정보 검출 작업 결과에 대한 서비스 담당자의 조치계획이 전자 결재 문서로 </p>\r\n" + 
									"<p>생성되었습니다. 아래 링크로 접속하신 후에 상세 내용 검토 및 소속 부서장의 승인이</p>\r\n" + 
									"<p>완료될 수 있도록 조치해 주시기 바랍니다.</p> <br>\r\n" + 
									"<p><span style=\"font-size:13px; font-weight: bold;\">기타 문의사항은 본 메일에 회신 또는 아래 문의처로 연락주시기 바랍니다. <br></span><p>\r\n" + 
									"<p><span style=\"font-size:13px; font-weight: bold;\">-관련 문의처: IT보안 운영실("+number+", "+email+")</span><p>" ;
			String SK_title_note =  selectUserList.get(0).getComment();
			String send_user_name = selectUserList.get(0).getUser_name();
			String send_user_sosok = selectUserList.get(0).getSosok();
			String detailContent = "";
			int detailConCount = 0 ;
			title = "[PIMC] 서버 내 개인정보 검출결과 조치계획 확인요청";
			
			if(selectUserList.size() < 4) {
				for(UserVo ao : selectUserList) {
					detailContent += "<tr><td valign=\"bottom\" style=\"font-size: 13px; font-weight: bold;\">" + ao.getDetail_con() + "</td><tr>\r\n";
					detailContent += "<tr><td valign=\"bottom\" style=\"font-size: 13px;\"> &nbsp;&nbsp; > " + ao.getNotePad() + "</td><tr>\r\n";
				}
			}else {
				for(int i=0 ; i < selectUserList.size() ; i++) {
					if(i < 3) {
						detailContent += "<tr><td valign=\"bottom\" style=\"font-size: 13px; font-weight: bold;\">" + selectUserList.get(i).getDetail_con() + "</td><tr>\r\n";
						detailContent += "<tr><td valign=\"bottom\" style=\"font-size: 13px;\"> &nbsp;&nbsp; > " + selectUserList.get(i).getNotePad() + "</td><tr>\r\n";
					}else {
						detailConCount++;
					}
				}
				detailContent += "<tr><td>&nbsp;</td></tr>\r\n";
				detailContent += "<tr><td style=\"font-size: 13px; font-weight: bold;\">+외 "+detailConCount+"건</td></tr>\r\n";
			}
			
			content += "<body style=\"font-family:'Noto Sans KR',sans-serif;padding:0;margin:0;\"> \r\n" + 
					"<table style=\"border: 1px solid #ccc;\" border=\"0\" width=\"700\" cellspacing=\"0\" align=\"center\"> \r\n" + 
					"	<tbody> \r\n" + 
					"		<tr> \r\n" + 
					"			<td align=\"center\"> \r\n" + 
					"			<table style=\"border-bottom: 2px solid #000;\" border=\"0\" width=\"620\" cellspacing=\"0\" cellpadding=\"0\"> \r\n" + 
					"				<tbody> \r\n" + 
					"					<tr> \r\n" + 
					"						<td><img src=\"https://pimc.sktelecom.com/resources/assets/images/SKT_mail_title_1.png\" width=\"620\" height=\"91\" /></td> \r\n" + 
					"					</tr> \r\n" + 
					"				</tbody> \r\n" + 
					"			</table> \r\n" + 
					"			<table border=\"0\" width=\"620\" cellspacing=\"0\" cellpadding=\"0\"> \r\n" + 
					"				<tbody> \r\n" + 
					"					<tr> \r\n" + 
					"						<td style=\"height: 90px;\" align=\"center\" valign=\"bottom\"><img src =\"https://pimc.sktelecom.com/resources/assets/images/SKT_mail_icon_2.png\" style=\"vertical-align: bottom;\" width=\"59\" height=\"60\" /></td> \r\n" + 
					"					</tr> \r\n" + 
					"					<tr> \r\n" + 
					"						<td style=\"font-size: 13px; color: #999; font-weight: bold; height: 22px; letter-spacing: -0.7px; line-height: 13px;\" align=\"center\" valign=\"bottom\">개인정보 검출관리센터(PIMC)에서 발송되는 안내 메일입니다.</td> \r\n" + 
					"					</tr>\r\n" + 
					"					<tr> \r\n" + 
					"						<td style=\"height: 28px;\">&nbsp;</td>\r\n" + 
					"					</tr>\r\n" + 
					"				</tbody>\r\n" + 
					"			</table>\r\n" + 
					"			<table style=\"border: 1px solid #cccccc; padding-top: 30px; height: 239px;\" border=\"0\" width=\"620\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
					"				<tbody>\r\n" + 
					"					<tr style=\"height: 84px;\">\r\n" + 
					"						<td style=\"padding-left: 30px; height: 84px; width: 0px;\">&nbsp;</td>\r\n" + 
					"						<td style=\"font-size: 13px; font-weight: bold; height: 84px; letter-spacing: -0.5px; width: 590px;\">\r\n" + 
					"							<div style=\"line-height: 15px;\">							\r\n" + 
					"							<p>정보보호담당 IT보안팀에서 안내 드립니다.</p> <br>							\r\n" + 
					"							<p style=\"white-space: pre-wrap;\">"+SK_title_con1 + 
					"							</p>							\r\n" + 
					"							<p style=\"white-space: pre-wrap; margin-top: 30px;\">"+SK_title_con2 + 
					"							</p><br>\r\n" + 
					"							<p>\r\n" + 
					"								<span style=\"background-color:#e6e6e6;\">전자결재 시스템(EDMS) 바로가기(<a href=\""+_edms_url+"\" target=\"_blank\">"+_edms_url+"</a>)</span>\r\n" + 
					"							</p>							\r\n" + 
					"							<p>&nbsp;</p>						\r\n" + 
					"							</div>\r\n" + 
					"						</td>\r\n" + 
					"					</tr>\r\n" + 
					"					<tr style=\"height: 51px;\">\r\n" + 
					"						<td style=\"padding-left: 30px; height: 51px; width: 0px;\">&nbsp;</td>\r\n" + 
					"						<td style=\"height: 51px; width: 590px;\">\r\n" + 
					"							<table style=\"height: 16px;\" border=\"0\" width=\"590\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
					"								<tbody>\r\n" + 
					"									<tr> \r\n" + 
					"										<td align=\"left\" width=\"120\" style=\"font-size:13px;color:#999;font-weight:700;line-height:15px;\">조지계획 등록자</td>\r\n" + 
					"										<td style=\"font-size: 13px; color: #222222; font-weight: bold; height: 16px; width: 586px;\" align=\"left\">"+user_name+"</td>\r\n" + 
					"									</tr>\r\n" + 
					"								</tbody>\r\n" + 
					"							</table>\r\n" + 
					"							<table style=\"padding-top: 10px;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
					"								<tbody>\r\n" + 
					"									<tr>\r\n" + 
					"										<td style=\"font-size: 13px; color: #999; font-weight: bold; line-height: 15px;\" align=\"left\" width=\"60\">소속부서</td>\r\n" + 
					"										<td style=\"font-size: 13px; color: #222; font-weight: bold;\" width=\"575\">"+team_name+"</td>\r\n" + 
					"									</tr>\r\n" + 
					"								</tbody>\r\n" + 
					"							</table>\r\n" + 
					"						</td>\r\n" + 
					"					</tr>\r\n" + 
					"					<tr>\r\n" + 
					"						<td style=\"padding-left:30;\"></td>\r\n" + 
					"						<td style=\"height:20px;\"></td>\r\n" + 
					"						<tr style=\"height: 42px;\">\r\n" + 
					"							<td style=\"padding-left: 30px; height: 42px; width: 0px;\">&nbsp;</td>\r\n" + 
					"							<td style=\"height: 42px; font-size: 13px; color: #999999; font-weight: bold; line-height: 13px; width: 590px;\">"+_type+"</td>\r\n" + 
					"						</tr>\r\n" + 
					"						<tr style=\"height: 22px;\">\r\n" + 
					"							<td style=\"padding-left: 30px; height: 22px; width: 0px;\">&nbsp;</td>\r\n" + 
					"							<td style=\"height: 22px; width: 590px;\">\r\n" + 
					"								<table border=\"0\" width=\"590\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
					"									<tbody>\r\n" + detailContent + 
					"									</tbody>\r\n" + 
					"								</table>\r\n" + 
					"							</td>\r\n" + 
					"						</tr>\r\n" + 
					"						<tr style=\"height: 30px;\">\r\n" + 
					"							<td style=\"padding-left: 30px; height: 30px; width: 0px;\">&nbsp;</td>\r\n" + 
					"							<td style=\"height: 30px; width: 590px;\">&nbsp;</td>\r\n" + 
					"						</tr>\r\n" + 
					"					</tbody>\r\n" + 
					"				</table>\r\n" + 
					"				<table border=\"0\" width=\"700\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
					"					<tbody>\r\n" + 
					"						<tr>\r\n" + 
					"							<td style=\"height: 80px;\">&nbsp;</td>\r\n" + 
					"						</tr>\r\n" + 
					"					</tbody>\r\n" + 
					"				</table>\r\n" + 
					"				<table style=\"border-top: 1px solid #ccc;\" border=\"0\" width=\"700\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
					"					<tbody>\r\n" + 
					"						<tr>\r\n" + 
					"							<td bgcolor=\"#e6e6e6\" height=\"86\">\r\n" + 
					"								<table>\r\n" + 
					"									<tbody>\r\n" + 
					"										<tr>\r\n" + 
					"											<td style=\"padding-left: 40px;\" width=\"110\"><img src=\"https://pimc.sktelecom.com/resources/assets/images/SKT_mail_footer%20logo.png\" width=\"82\" height=\"32\" /></td>\r\n" + 
				"												<td style=\"letter-spacing: -0.7px; font-size: 13px; color: #000000; font-weight: bold;\" align=\"center\" height=\"42\" width=\"390\">Personal Information Management Center</td>\r\n" + 
					"										<!-- <td style=\"font-size: 13px; color: #FF5000; font-weight: bold;\" align=\"center\" height=\"42\" width=\"390\">Personal Information Management Center</td> -->\r\n" + 
					"										</tr>\r\n" + 
					"									</tbody>\r\n" + 
					"								</table>\r\n" + 
					"							</td>\r\n" + 
					"						</tr>\r\n" + 
					"					</tbody>\r\n" + 
					"				</table>\r\n" + 
					"				<table border=\"0\" width=\"700\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
					"					<tbody>\r\n" + 
					"						<tr>\r\n" + 
					"						</tr>\r\n" + 
					"					</tbody>\r\n" + 
					"				</table>\r\n" + 
					"			</td>\r\n" + 
					"		</tr>\r\n" + 
					"	</tbody>\r\n" + 
					"</table>\r\n" + 
					"</body>";
			
	    	
			/*logger.info("content >>>>> " + content);*/
			
		}else {
		}
		
		return selectUserList;
	}
	
	@Override
	public Map<String, Object> templateInsert(HttpServletRequest request) throws Exception {
		String template_con = request.getParameter("template_con");
		int mailType = Integer.parseInt(request.getParameter("mailType"));
		
		String name = "";
		if(mailType == 1) {
			name = "mail1";
		}else {
			name = "mail2";
		}
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("template_con", template_con);
		input.put("name", name);
		
		logger.info("input >> " + input);
		dao.templateUpdate(input);
		dao.templateInsert(input);
		
		return input;
	}
	// LG생건 전용 메일 발송 (서버별 검출건수 포함)
	@Override
	public Map<String, Object> serverGroupMailLg(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		java.text.DecimalFormat decFormat = new java.text.DecimalFormat("###,###");

		String assetnosch = request.getParameter("assetnosch");
		String detailCon = request.getParameter("detailCon");
		String mailTitle = request.getParameter("mailTitle");
		String mailReceiver = request.getParameter("mailReceiver");
		String mailCc = request.getParameter("mailCc");

		logger.info("LG생건 메일 발송 시작");
		logger.info("assetnosch: " + assetnosch);
		logger.info("mailReceiver: " + mailReceiver);

		// assetnosch를 List<String>으로 변환 (JsonPrimitive 오류 방지)
		Gson gson = new Gson();
		JsonArray assetnoschJArr = gson.fromJson(assetnosch, JsonArray.class);
		List<String> assetnoschList = new ArrayList<>();
		for (int i = 0; i < assetnoschJArr.size(); i++) {
			assetnoschList.add(assetnoschJArr.get(i).getAsString());
		}

		// 메일 설정 조회
		Map<String, Object> mailMap = new HashMap<>();
		try {
			mailMap = approvalDao.selectMailUser();
		} catch (Exception e) {
			logger.error("메일 설정 조회 실패", e);
			resultMap.put("result", "fail");
			resultMap.put("message", "메일 설정 조회 실패");
			return resultMap;
		}

		String sendmail = mailMap.get("COM").toString();
		String passwd = AESDecrypt(mailMap.get("PWD").toString());

		// 수신자 목록
		List<String> receiverEmails = new ArrayList<>();
		if (mailReceiver != null && !mailReceiver.trim().isEmpty()) {
			String[] receivers = mailReceiver.split(",");
			for (String receiver : receivers) {
				if (!receiver.trim().isEmpty()) {
					receiverEmails.add(receiver.trim());
				}
			}
		}

		if (receiverEmails.isEmpty()) {
			resultMap.put("result", "fail");
			resultMap.put("message", "수신자가 없습니다.");
			return resultMap;
		}

		// 서버별 검출건수 테이블 생성 (모든 수신자에게 동일한 내용)
		StringBuilder tableContent = new StringBuilder();

		for (int i = 0; i < assetnoschList.size(); i++) {
			String targetId = assetnoschList.get(i);
			// ap_no는 assetnosch와 함께 전달받아야 하지만, 현재는 기본값 0 사용
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("target_id", targetId);
			paramMap.put("ap_no", "0");

			Map<String, Object> summary = dao.selectTargetSummaryLg(paramMap);

			if (summary != null) {
				String serverName = summary.get("SERVER_NAME") != null ? summary.get("SERVER_NAME").toString() : "-";
				String serviceNm = summary.get("SERVICE_NM") != null ? summary.get("SERVICE_NM").toString() : "-";
				long totalCnt = summary.get("TOTAL_CNT") != null ? Long.parseLong(summary.get("TOTAL_CNT").toString()) : 0;
				long phone = summary.get("PHONE") != null ? Long.parseLong(summary.get("PHONE").toString()) : 0;
				long email = summary.get("EMAIL") != null ? Long.parseLong(summary.get("EMAIL").toString()) : 0;
				long account = summary.get("ACCOUNT") != null ? Long.parseLong(summary.get("ACCOUNT").toString()) : 0;
				long card = summary.get("CARD") != null ? Long.parseLong(summary.get("CARD").toString()) : 0;
				long ssn = summary.get("SSN") != null ? Long.parseLong(summary.get("SSN").toString()) : 0;
				long foreigner = summary.get("FOREIGNER") != null ? Long.parseLong(summary.get("FOREIGNER").toString()) : 0;
				long driver = summary.get("DRIVER") != null ? Long.parseLong(summary.get("DRIVER").toString()) : 0;
				long passport = summary.get("PASSPORT") != null ? Long.parseLong(summary.get("PASSPORT").toString()) : 0;

				tableContent.append("<tr>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(serverName).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(serviceNm).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px; background-color: #FFFF00; font-weight: bold;\">").append(decFormat.format(totalCnt)).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(decFormat.format(phone)).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(decFormat.format(email)).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(decFormat.format(account)).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(decFormat.format(card)).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(decFormat.format(ssn)).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(decFormat.format(foreigner)).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(decFormat.format(driver)).append("</td>");
				tableContent.append("<td style=\"border: 1px solid #cccccc; padding: 8px;\">").append(decFormat.format(passport)).append("</td>");
				tableContent.append("</tr>");
			}
		}

		int successCount = 0;
		int failCount = 0;
		List<String> successList = new ArrayList<>();
		List<String> failList = new ArrayList<>();

		// 각 수신자에게 메일 발송
		for (String receiverEmail : receiverEmails) {
			String content = buildLgMailContentWithTable(receiverEmail, "-", detailCon, tableContent.toString());

			try {
				Properties prop = new Properties();
				prop.put("mail.smtp.host", send_host);
				prop.put("mail.smtp.port", send_port);
				prop.put("mail.smtp.auth", "true");
				prop.put("mail.smtp.ssl.enable", "true");

				Session session = Session.getDefaultInstance(prop, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(sendmail, passwd);
					}
				});

				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(sendmail));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

				// 참조자 추가
				if (mailCc != null && !mailCc.trim().isEmpty()) {
					String[] ccList = mailCc.split(",");
					for (String cc : ccList) {
						if (!cc.trim().isEmpty()) {
							message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc.trim()));
						}
					}
				}

				message.setSubject("[PICenter] " + mailTitle);

				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(content, "text/html; charset=UTF-8");

				// LG생건 로고 이미지
				MimeBodyPart imagePart1 = new MimeBodyPart();
				InputStream imageStream1 = new URL("http://localhost:8080/resources/assets/images/mail_title_lg.png").openStream();
				DataSource fds1 = new ByteArrayDataSource(imageStream1, "image/png");
				imagePart1.setDataHandler(new DataHandler(fds1));
				imagePart1.setHeader("Content-ID", "<lg_logo>");
				imagePart1.setDisposition(MimeBodyPart.INLINE);

				// 메일 아이콘 이미지
				MimeBodyPart imagePart2 = new MimeBodyPart();
				InputStream imageStream2 = new URL("http://localhost:8080/resources/assets/images/mail_icon.png").openStream();
				DataSource fds2 = new ByteArrayDataSource(imageStream2, "image/png");
				imagePart2.setDataHandler(new DataHandler(fds2));
				imagePart2.setHeader("Content-ID", "<mail_logo>");
				imagePart2.setDisposition(MimeBodyPart.INLINE);

				MimeMultipart multipart = new MimeMultipart("related");
				multipart.addBodyPart(messageBodyPart);
				multipart.addBodyPart(imagePart1);
				multipart.addBodyPart(imagePart2);

				message.setContent(multipart);
				Transport.send(message);

				successCount++;
				successList.add(receiverEmail);
				logger.info("LG생건 메일 발송 성공: " + receiverEmail);

			} catch (Exception e) {
				failCount++;
				failList.add(receiverEmail);
				logger.error("LG생건 메일 발송 실패: " + receiverEmail, e);
			}
		}

		resultMap.put("result", failCount == 0 ? "success" : (successCount > 0 ? "partial" : "fail"));
		resultMap.put("successCount", successCount);
		resultMap.put("failCount", failCount);
		resultMap.put("successList", successList);
		resultMap.put("failList", failList);

		return resultMap;
	}

	// LG생건 전용 메일 본문 생성 (서버별 검출건수 표 포함)
	private String buildLgMailContentWithTable(String userName, String userGroup, String detailCon, String tableContent) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body style=\"font-family: 'Noto Sans KR', sans-serif; padding: 0; margin: 0;\">");
		sb.append("<table style=\"border: 1px solid #ccc;\" border=\"0\" width=\"1400\" cellspacing=\"0\" align=\"center\">");
		sb.append("<tbody><tr><td align=\"center\">");

		// 헤더
		sb.append("<table style=\"border-bottom: 2px solid #000;\" border=\"0\" width=\"1300\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td>");
		sb.append("<img src=\"cid:lg_logo\" style=\"width: 100%;\" />");
		sb.append("</td></tr></tbody></table>");

		// 로고 아이콘
		sb.append("<table border=\"0\" width=\"1300\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td style=\"height: 90px;\" align=\"center\" valign=\"bottom\">");
		sb.append("<img src=\"cid:mail_logo\" style=\"vertical-align: bottom;\" width=\"59\" height=\"60\" />");
		sb.append("</td></tr><tr>");
		sb.append("<td style=\"font-size: 13px; color: #999; font-weight: bold; height: 22px;\" align=\"center\" valign=\"bottom\">");
		sb.append("개인정보 검출관리센터(PICenter)에서 발송되는 안내 메일입니다.</td>");
		sb.append("</tr><tr><td style=\"height: 28px;\">&nbsp;</td></tr></tbody></table>");

		// 본문
		sb.append("<table style=\"border: 1px solid #cccccc; padding-top: 30px;\" border=\"0\" width=\"1300\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td style=\"padding-left: 30px;\">&nbsp;</td>");
		sb.append("<td style=\"font-size: 13px; font-weight: bold;\">");

		// 수신자 정보
		sb.append("<table style=\"padding-top: 13px;\" border=\"0\" width=\"1250\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td align=\"left\" width=\"80\" style=\"font-size: 13px; color: #999; font-weight: 700;\">수신자</td>");
		sb.append("<td style=\"font-size: 13px; color: #222; font-weight: bold;\" align=\"left\">").append(userName).append("</td>");
		sb.append("</tr></tbody></table>");

		sb.append("<table style=\"padding: 10px 0 20px 0;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td style=\"font-size: 13px; color: #999; font-weight: bold;\" align=\"left\" width=\"80\">소속부서</td>");
		sb.append("<td style=\"font-size: 13px; color: #222; font-weight: bold;\">").append(userGroup).append("</td>");
		sb.append("</tr></tbody></table>");

		// 메일 내용
		sb.append("<pre style=\"font-family: 'Noto Sans KR', sans-serif; font-size: 13px; line-height: 1.8;\">").append(detailCon).append("</pre>");
		sb.append("</td></tr>");

		// 서버별 검출건수 테이블 (이미지 형식 - 2단 헤더)
		sb.append("<tr><td style=\"padding-left: 30px;\">&nbsp;</td>");
		sb.append("<td style=\"padding: 20px 0;\">");
		sb.append("<table style=\"width: 1200px; border-collapse: collapse; font-size: 12px; text-align: center;\">");

		// 테이블 헤더 - 2단 구조
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th rowspan=\"2\" style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">서버명</th>");
		sb.append("<th rowspan=\"2\" style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">용도</th>");
		sb.append("<th rowspan=\"2\" style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">총 검출 건수</th>");
		sb.append("<th colspan=\"4\" style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">개인정보 유형</th>");
		sb.append("<th colspan=\"4\" style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">고유식별정보</th>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<th style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">휴대폰 번호</th>");
		sb.append("<th style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">이메일</th>");
		sb.append("<th style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">계좌번호</th>");
		sb.append("<th style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">카드번호</th>");
		sb.append("<th style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">주민등록번호</th>");
		sb.append("<th style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">외국인번호</th>");
		sb.append("<th style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">운전면허번호</th>");
		sb.append("<th style=\"border: 1px solid #cccccc; padding: 8px; background-color: #f0f0f0;\">여권번호</th>");
		sb.append("</tr>");
		sb.append("</thead>");

		sb.append("<tbody>");
		sb.append(tableContent);
		sb.append("</tbody></table></td></tr>");

		// 링크
		sb.append("<tr><td style=\"padding-left: 30px; padding-top: 16px;\">&nbsp;</td>");
		sb.append("<td style=\"font-size: 13px; color: #999; font-weight: bold;\">상세정보 확인</td></tr>");
		sb.append("<tr><td style=\"padding-left: 30px;\">&nbsp;</td>");
		sb.append("<td style=\"padding: 10px 0 30px 0;\">");
		sb.append("<span style=\"background-color: #e6e6e6; font-size: 13px; font-weight: bold; padding: 5px 10px;\">");
		sb.append("개인정보검출관리센터(PICenter) 바로가기 (<a href=\"http://picenter.lghnh.com\" target=\"_blank\">http://picenter.lghnh.com</a>)");
		sb.append("</span></td></tr>");

		sb.append("</tbody></table>");

		// 푸터
		sb.append("<table border=\"0\" width=\"1400\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr><td style=\"height: 50px;\">&nbsp;</td></tr></tbody></table>");
		sb.append("<table style=\"border-top: 1px solid #ccc; width: 100%;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		sb.append("<tbody><tr><td bgcolor=\"#e6e6e6\" height=\"86\"><table style=\"width: 100%;\">");
		sb.append("<tbody><tr><td style=\"padding-left: 40px; width: 120px;\">");
		sb.append("</td>");
		sb.append("<td style=\"font-size: 13px; color: #000; font-weight: bold;\" align=\"center\">");
		sb.append("Personal Information Center</td></tr></tbody></table></td></tr></tbody></table>");

		sb.append("</td></tr></tbody></table></body>");

		return sb.toString();
	}

	// 메일 템플릿 목록 조회
	@Override
	public List<Map<String, Object>> getTemplateList() throws Exception {
		return dao.selectTemplateList();
	}

	// 메일 템플릿 상세 조회
	@Override
	public Map<String, Object> getTemplateDetail(int idx) throws Exception {
		return dao.selectTemplateDetail(idx);
	}

	// 메일 템플릿 삭제
	@Override
	public void deleteTemplate(int idx) throws Exception {
		dao.deleteTemplate(idx);
	}

	// 메일 템플릿 등록
	@Override
	public void insertTemplate(Map<String, Object> params) throws Exception {
		dao.insertSendmailTemplate(params);
	}

	// 메일 템플릿 수정
	@Override
	public void updateTemplate(Map<String, Object> params) throws Exception {
		dao.updateTemplateById(params);
	}

	// 메일 발송 이력 저장
	private void saveMailHistory(String mailTitle, String mailContent, String senderId, String senderName,
			List<String> receiverEmails, List<String> ccEmails, String sendResult, String errorMsg,
			JsonArray assetnoschJArr, JsonArray apnoschJArr) {

		try {
			// 1. 메일 이력 저장
			Map<String, Object> historyParams = new HashMap<>();
			historyParams.put("mail_title", "[PICenter] " + mailTitle);
			historyParams.put("mail_content", mailContent);
			historyParams.put("sender_id", senderId);
			historyParams.put("sender_name", senderName);
			historyParams.put("receivers", String.join(",", receiverEmails));
			historyParams.put("cc_receivers", ccEmails != null && !ccEmails.isEmpty() ? String.join(",", ccEmails) : null);
			historyParams.put("send_result", sendResult);
			historyParams.put("error_msg", errorMsg);

			dao.insertMailHistory(historyParams);

			// useGeneratedKeys로 생성된 mail_idx 가져오기 (BigInteger로 반환될 수 있음)
			Object mailIdxObj = historyParams.get("mail_idx");
			Integer mailIdx = null;
			if (mailIdxObj != null) {
				if (mailIdxObj instanceof Number) {
					mailIdx = ((Number) mailIdxObj).intValue();
				}
			}

			if (mailIdx != null && assetnoschJArr != null && assetnoschJArr.size() > 0) {
				// 2. 대상 서버 목록 저장
				List<Map<String, Object>> targetList = new ArrayList<>();
				for (int i = 0; i < assetnoschJArr.size(); i++) {
					Map<String, Object> target = new HashMap<>();
					target.put("target_id", assetnoschJArr.get(i).getAsString());
					target.put("ap_no", (apnoschJArr != null && apnoschJArr.size() > i)
							? apnoschJArr.get(i).getAsInt() : 0);
					targetList.add(target);
				}

				Map<String, Object> targetParams = new HashMap<>();
				targetParams.put("mail_idx", mailIdx);
				targetParams.put("targetList", targetList);
				dao.insertMailTargetBatch(targetParams);
			}

			logger.info("메일 이력 저장 완료 - MAIL_IDX: " + mailIdx);

		} catch (Exception e) {
			logger.error("메일 이력 저장 중 오류: " + e.getMessage(), e);
		}
	}

	// 복호화
	private String AESDecrypt(String pwd) throws Exception {

		byte[] PKey = key.getBytes();
		byte[] decrypted = null ;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(PKey, ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decodedBytes = Base64.getDecoder().decode(pwd);
			decrypted = cipher.doFinal(decodedBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(decrypted);
	}
}
