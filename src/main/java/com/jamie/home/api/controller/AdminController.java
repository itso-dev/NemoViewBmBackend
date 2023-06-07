package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.AdService;
import com.jamie.home.api.service.MemberService;
import com.jamie.home.api.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdService adService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value="/service/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();

            List<SERVICE> list = serviceService.list(search);

            if(list != null){
                Integer cnt = serviceService.listCnt(search);
                VoList<SERVICE> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_SERVICE_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_SERVICE_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_SERVICE_FAIL", null);
        }
    }

    @RequestMapping(value="/service/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
        try {
            SERVICE service = new SERVICE();
            service.setService(key);
            SERVICE result = serviceService.get(service);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_SERVICE_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_SERVICE_NULL", false);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_SERVICE_FAIL", false);
        }
    }

    @RequestMapping(value="/service/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute SERVICE service) {
        try {
            int result = serviceService.save(service);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_SERVICE_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_SERVICE_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_SERVICE_FAIL", false);
        }
    }

    @RequestMapping(value={"/service/{key}"}, method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modify(@PathVariable("key") int key, @Validated @ModelAttribute SERVICE service) {
        try {
            service.setService(key);
            int result = serviceService.modify(service);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_SERVICE_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_SERVICE_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_SERVICE_FAIL", false);
        }
    }

    @RequestMapping(value={"/service/{key}"}, method= RequestMethod.DELETE)
    public ResponseOverlays remove(@PathVariable("key") int key, @Validated @RequestBody SERVICE service) {
        try {
            service.setService(key);
            int result = serviceService.remove(service);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_SERVICE_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_SERVICE_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_SERVICE_FAIL", false);
        }
    }

    @RequestMapping(value="/member/list", method= RequestMethod.POST)
    public ResponseOverlays listMember(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();

            List<MEMBER> list = memberService.listForAdmin(search);

            if(list != null){
                Integer cnt = memberService.listCnt(search);
                VoList<MEMBER> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/member/{key}", method= RequestMethod.GET)
    public ResponseOverlays getMember(@PathVariable("key") int key) {
        try {
            MEMBER member = new MEMBER();
            member.setMember(key);
            MEMBER result = memberService.getForAdmin(member);
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

    @RequestMapping(value="/member/{key}/point", method= RequestMethod.PUT)
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

    @RequestMapping(value="/ad/list", method= RequestMethod.POST)
    public ResponseOverlays listAd(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();
            List<AD> list = adService.listForAdmin(search);

            if(list != null){
                Integer cnt = adService.listCnt(search);
                VoList<AD> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_AD_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_AD_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_AD_FAIL", null);
        }
    }

    @RequestMapping(value="/ad/{key}", method= RequestMethod.GET)
    public ResponseOverlays getAd(@PathVariable("key") int key) {
        try {
            AD ad = new AD();
            ad.setAd(key);
            AD result = adService.get(ad);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_AD_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_AD_NULL", false);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_AD_FAIL", false);
        }
    }
}