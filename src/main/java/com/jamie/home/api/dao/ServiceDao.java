package com.jamie.home.api.dao;

import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.model.SERVICE;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ServiceDao {
    List<SERVICE> getListService(SEARCH search);
    Integer getListServiceCnt(SEARCH search);
    SERVICE getService(SERVICE service);
    Integer insertService(SERVICE service);
    Integer updateService(SERVICE service);
    Integer deleteService(SERVICE service);
}
