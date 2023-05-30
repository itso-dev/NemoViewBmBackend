package com.jamie.home.api.service;

import com.jamie.home.api.dao.AdDao;
import com.jamie.home.api.dao.InfoDao;
import com.jamie.home.api.dao.MemberDao;
import com.jamie.home.api.dao.ServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BasicService {
    private static final Logger logger = LoggerFactory.getLogger(BasicService.class);
    @Autowired
    PasswordEncoder encoder;
    @Value("${file.upload.dir}")
    String uploadDir;
    @Autowired
    MemberDao memberDao;
    @Autowired
    ServiceDao serviceDao;
    @Autowired
    AdDao adDao;
    @Autowired
    InfoDao infoDao;
    @Autowired
    JavaMailSender javaMailSender;
}
