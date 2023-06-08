package com.jamie.home.api.service;

import com.jamie.home.api.dao.*;
import com.jamie.home.api.model.DASH;
import com.jamie.home.api.model.SEARCH;
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
    AdminDao adminDao;
    @Autowired
    JavaMailSender javaMailSender;

    public DASH getDashInfo(SEARCH search) {
        DASH dash = new DASH();
        dash.setMember_tot(adminDao.getMemberCnt(new SEARCH()));
        dash.setMember_new(adminDao.getMemberCnt(search));
        dash.setPoint_tot(adminDao.getMemberPoint(new SEARCH()));
        dash.setPoint_new(adminDao.getMemberPoint(search));
        search.setType(1); // 제품
        dash.setCategoryRank(adminDao.getCategoryRank(search));
        search.setType(2); // 제품
        dash.setServiceCategoryRank(adminDao.getCategoryRank(search));
        dash.setCommonKeywordRank(adminDao.getCommonKeywordRank());
        dash.setKeywordRank(adminDao.getKeywordRank());
        return dash;
    }
}
