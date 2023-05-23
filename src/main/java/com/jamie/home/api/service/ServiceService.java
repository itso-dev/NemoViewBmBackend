package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.ROLE;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.model.SERVICE;
import com.jamie.home.util.CodeUtils;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServiceService extends BasicService{
    public List<SERVICE> list(SEARCH search) {
        return serviceDao.getListService(search);
    }

    public Integer listCnt(SEARCH search) {
        return serviceDao.getListServiceCnt(search);
    }

    public SERVICE get(SERVICE service){
        return serviceDao.getService(service);
    }

    public Integer save(SERVICE service) {
        return serviceDao.insertService(service);
    }

    public Integer modify(SERVICE service) {
        return serviceDao.updateService(service);
    }
}
