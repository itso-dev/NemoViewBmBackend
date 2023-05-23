package com.jamie.home.api.service;

import com.jamie.home.api.model.MEMBER;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
@Transactional
public class MailService extends BasicService{
    public void sendMail(MEMBER member) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(message, true, "UTF-8");
        // 수신 대상
        msgHelper.setTo(member.getEmail());
        msgHelper.setFrom("sender@gmail.com");

        msgHelper.setSubject("[네모뷰 광고] 회원가입 인증번호");
        String htmlContent = "인증번호["+member.getCode()+"]를 입력해주시기 바랍니다.<br>";
        msgHelper.setText(htmlContent, true);
        javaMailSender.send(message);
    }
}
