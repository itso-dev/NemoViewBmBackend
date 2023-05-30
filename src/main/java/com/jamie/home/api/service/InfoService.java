package com.jamie.home.api.service;

import com.jamie.home.api.model.INFO;
import com.jamie.home.api.model.SEARCH;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InfoService extends BasicService{
    public List<INFO> list(SEARCH search) {
        return infoDao.getListInfo(search);
    }

    public Integer listCnt(SEARCH search) {
        return infoDao.getListInfoCnt(search);
    }

    public INFO get(INFO info){
        return infoDao.getInfo(info);
    }

    public Integer save(INFO info) {
        return infoDao.insertInfo(info);
    }

    public Integer modify(INFO info) {
        return infoDao.updateInfo(info);
    }
}
