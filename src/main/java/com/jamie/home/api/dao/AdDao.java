package com.jamie.home.api.dao;

import com.jamie.home.api.model.AD;
import com.jamie.home.api.model.KEYWORD;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AdDao {
    List<AD> getListAd(SEARCH search);
    Integer getListAdCnt(SEARCH search);
    AD getAd(AD ad);
    Integer insertAd(AD ad);
    Integer updateAd(AD ad);
    Integer deleteAd(AD ad);
    List<KEYWORD> getAdCommonKeyword(AD ad);
    Integer insertAdCommonKeyword(AD ad);
    Integer updateAdCommonKeywordMandatory(KEYWORD keyword);
    Integer deleteAdCommonKeyword(KEYWORD keyword);
    List<KEYWORD> getAdKeyword(AD ad);
    Integer insertAdKeyword(AD ad);
    Integer updateAdKeywordMandatory(KEYWORD keyword);
    Integer deleteAdKeyword(KEYWORD keyword);
    Integer insertAdHit(AD ad);
    Integer getAdLike(AD ad);
    Integer insertAdLike(AD ad);
    Integer deleteAdLike(AD ad);
    Integer getListAdShowCnt(AD ad);
    Integer getListClickMemberCnt(AD ad);
    List<KEYWORD> getClickMemberKeyword(AD ad);
    void updateAdStateAll(AD ad);
    List<AD> getAdListRandom(SEARCH search);
    void insertMemberAdValid(SEARCH search);

    void insertAdShow(AD ad);

    Boolean getIsNotOverTodayPrice(AD ad);

    Integer getTodayLeftDayPrice(AD ad);
    Integer getCntMemberMatchKeyword(AD ad);
}
