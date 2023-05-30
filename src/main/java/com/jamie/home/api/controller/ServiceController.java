package com.jamie.home.api.controller;

import com.jamie.home.api.model.ResponseOverlays;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.model.SERVICE;
import com.jamie.home.api.model.VoList;
import com.jamie.home.api.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/service/*")
public class ServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private ServiceService serviceService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
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

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
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

    @RequestMapping(value="/save", method= RequestMethod.POST)
    public ResponseOverlays save(@Validated @RequestBody SERVICE service) {
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

    @RequestMapping(value={"/{key}"}, method= RequestMethod.PUT)
    public ResponseOverlays modify(@PathVariable("key") int key, @Validated @RequestBody SERVICE service) {
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
}