package com.jamie.home.api.controller;

import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.POINT;
import com.jamie.home.api.model.ResponseOverlays;
import com.jamie.home.api.model.TOKEN;
import com.jamie.home.api.service.MailService;
import com.jamie.home.api.service.MemberService;
import com.jamie.home.jwt.JwtFilter;
import com.jamie.home.jwt.TokenProvider;
import com.jamie.home.util.CodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/member/*")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MailService mailService;


    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
        try {
            MEMBER member = new MEMBER();
            member.setMember(key);
            MEMBER result = memberService.get(member);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", false);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", false);
        }
    }

    @RequestMapping(value="/save", method= RequestMethod.POST)
    public ResponseOverlays save(@Validated @RequestBody MEMBER member) {
        try {
            int result = memberService.save(member);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_MEMBER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_FAIL", false);
        }
    }

    @RequestMapping(value={"/{key}","/{key}/password","/{key}/info"}, method= RequestMethod.PUT)
    public ResponseOverlays modify(@PathVariable("key") int key, @Validated @RequestBody MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.modify(member);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_MEMBER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}/tax", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modifyWithFiles(@PathVariable("key") int key, @Validated @ModelAttribute MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.modify(member);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_MEMBER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_FAIL", false);
        }
    }

    @RequestMapping(value="/login", method= RequestMethod.POST)
    public ResponseOverlays login(@Value("${jwt.token-validity-in-seconds}") Double expirySec, @Validated @RequestBody MEMBER member) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = null;
            TOKEN token = null;
/*
            if(member.getRemember() != null && member.getRemember().booleanValue()){
                jwt = tokenProvider.createLongToken(authentication);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            } else {
                jwt = tokenProvider.createToken(authentication);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            }
*/
            jwt = tokenProvider.createToken(authentication);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            MEMBER result = memberService.getByParam(member);
            if(jwt != null){
                token = new TOKEN(result, jwt, (expirySec/3600.0));
                return new ResponseOverlays(HttpServletResponse.SC_OK, "LOGIN_MEMBER_SUCCESS", token);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/duple", method= RequestMethod.POST)
    public ResponseOverlays duple(@Validated @RequestBody MEMBER member) {
        try {
            MEMBER result = memberService.checkDuple(member);
            if(result != null){
                if(result.getCode() != null){ // 신규
                    mailService.sendMail(result);
                }
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", false);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}/point", method= RequestMethod.PUT)
    public ResponseOverlays modifyPoint(@PathVariable("key") int key, @Validated @RequestBody POINT point) {
        try {
            point.setMember(key);
            int result = memberService.modifyPoint(point);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_MEMBER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_FAIL", false);
        }
    }

    @RequestMapping(value="/find/password", method= RequestMethod.POST)
    public ResponseOverlays find(@Validated @RequestBody MEMBER member) {
        try {
            MEMBER result = memberService.getByParam(member);
            if(result != null){
                String newPassword = CodeUtils.getCodeValue();
                result.setPassword(newPassword);
                memberService.modify(result);
                mailService.sendMailPassword(result, newPassword);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", true);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", false);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", false);
        }
    }
}