package com.jamie.home.api.service;

import com.jamie.home.api.model.AD;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.model.SERVICE;
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

    public Integer save(SERVICE service) throws Exception {
        // 이미지 저장
        service.setImage(
                FileUtils.saveFiles(
                        service.getImage_new(),
                        uploadDir
                )
        );
        return serviceDao.insertService(service);
    }

    public Integer modify(SERVICE service) throws Exception {
        SERVICE ori_service = serviceDao.getService(service);
        // 이미지 수정
        if(service.getImage_new() != null){
            service.setImage(
                    FileUtils.modiOneFiles(
                            ori_service.getImage(),
                            service.getImage_new(),
                            uploadDir
                    )
            );
        }

        return serviceDao.updateService(service);
    }

    public Integer remove(SERVICE service){
        return serviceDao.deleteService(service);
    }
}
