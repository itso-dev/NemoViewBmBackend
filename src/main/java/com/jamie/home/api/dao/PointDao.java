package com.jamie.home.api.dao;

import com.jamie.home.api.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PointDao {
    List<POINT> getListPoint(SEARCH search);
    Integer getListPointCnt(SEARCH search);
    POINT getPoint(POINT point);
    Integer insertPoint(POINT point);
    Integer updatePoint(POINT point);
    Integer deletePoint(POINT point);

    Integer getPointTot(SEARCH search);
    Integer getPointCnt(SEARCH search);
}
