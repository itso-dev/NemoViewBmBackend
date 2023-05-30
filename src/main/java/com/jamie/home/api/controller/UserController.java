package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.AdService;
import com.jamie.home.api.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user/*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AdService adService;

    @RequestMapping(value="/ad/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            List<AD> list = adService.list(search);

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

    @RequestMapping(value={"/ad/{key}/like"}, method= RequestMethod.POST)
    public ResponseOverlays saveAdLike(@PathVariable("key") int key, @Validated @RequestBody AD ad) {
        try {
            ad.setAd(key);
            int result = adService.saveAdLike(ad);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_LIKE_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_LIKE_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_LIKE_FAIL", false);
        }
    }

    @RequestMapping(value={"/ad/{key}/hit"}, method= RequestMethod.POST)
    public ResponseOverlays saveAdHit(@PathVariable("key") int key, @Validated @RequestBody AD ad) {
        try {
            ad.setAd(key);
            int result = adService.saveAdHit(ad);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_HIT_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_HIT_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_HIT_FAIL", false);
        }
    }
}