package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.AdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/ad/*")
public class AdController {

    private static final Logger logger = LoggerFactory.getLogger(AdController.class);

    @Autowired
    private AdService adService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
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

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
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

    @RequestMapping(value="/{key}/{filter_name}/{start_date}/{end_date}", method= RequestMethod.GET)
    public ResponseOverlays getDetail(@PathVariable("key") int key,
                                      @PathVariable("filter_name") String filter_name,
                                      @PathVariable("start_date") String start_date,
                                      @PathVariable("end_date") String end_date) {
        try {
            AD ad = new AD();
            ad.setAd(key);
            ad.setFilter_name(filter_name);
            ad.setStart_date(start_date);
            ad.setEnd_date(end_date);
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

    @RequestMapping(value="/{key}/click/member/keyword", method= RequestMethod.GET)
    public ResponseOverlays getClickMemberKeyword(@PathVariable("key") int key) {
        try {
            AD ad = new AD();
            ad.setAd(key);
            List<KEYWORD> result = adService.getClickMemberKeyword(ad);
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

    @RequestMapping(value="/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute AD ad) {
        try {
            int result = adService.save(ad);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_NOT_SAVE", false);
            } else {
                AD return_ad = new AD();
                return_ad.setAd(ad.getAd());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_AD_SUCCESS", return_ad);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_FAIL", false);
        }
    }

    @RequestMapping(value={"/{key}","/{key}/keyword","/{key}/price"}, method= RequestMethod.PUT)
    public ResponseOverlays modify(@PathVariable("key") int key, @Validated @RequestBody AD ad) {
        try {
            ad.setAd(key);
            int result = adService.modify(ad);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_AD_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}/state", method= RequestMethod.PUT)
    public ResponseOverlays modifyState(@PathVariable("key") int key, @Validated @RequestBody AD ad) {
        try {
            ad.setAd(key);
            int result = adService.modifyState(ad);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_AD_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_FAIL", false);
        }
    }

    @RequestMapping(value={"/{key}/keyword/mandatory"}, method= RequestMethod.PUT)
    public ResponseOverlays modifyKeywordMandatory(@PathVariable("key") int key, @Validated @RequestBody AD ad) {
        try {
            ad.setAd(key);
            int result = adService.modifyKeywordMandatory(ad);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_AD_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modifyWithFiles(@PathVariable("key") int key, @Validated @ModelAttribute AD ad) {
        try {
            ad.setAd(key);
            int result = adService.modify(ad);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_AD_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_AD_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays remove(@PathVariable("key") int key) {
        try {
            AD ad = new AD();
            ad.setAd(key);
            int result = adService.remove(ad);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_AD_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "DELETE_AD_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_AD_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}/cnt/match", method= RequestMethod.GET)
    public ResponseOverlays getCntMember(@PathVariable("key") int key) {
        try {
            AD ad = new AD();
            ad.setAd(key);
            Integer result = adService.getCntMemberMatchKeyword(ad);
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

    @RequestMapping(value="/member/cnt/match", method= RequestMethod.POST)
    public ResponseOverlays getCntMemberChange(@Validated @RequestBody SEARCH search) {
        try {
            Integer result = adService.getCntMemberChange(search);
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