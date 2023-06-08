package com.jamie.home.api.dao;

import com.jamie.home.api.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AdminDao {

    Integer getMemberCnt(SEARCH search);

    Integer getMemberPoint(SEARCH search);

    List<CATEGORY> getCategoryRank(SEARCH search);

    List<KEYWORD> getCommonKeywordRank();

    List<KEYWORD> getKeywordRank();
}
