package com.jamie.home.api.service;

import com.jamie.home.api.controller.AdController;
import com.jamie.home.api.model.KEYWORD;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.model.AD;
import com.jamie.home.api.service.BasicService;
import com.jamie.home.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdService extends BasicService {
    private static final Logger logger = LoggerFactory.getLogger(AdService.class);

    public List<AD> list(SEARCH search) {
        List<AD> list = adDao.getListAd(search);
        for(int i=0; i<list.size(); i++){
            list.get(i).setFilter_name(search.getFilter_name());
            list.get(i).setStart_date(search.getStart_date());
            list.get(i).setEnd_date(search.getEnd_date());
            setDetailInfo(list.get(i));
        }
        return list;
    }

    public Integer listCnt(SEARCH search) {
        return adDao.getListAdCnt(search);
    }

    public AD get(AD ad){
        AD result = adDao.getAd(ad);
        result.setFilter_name(ad.getFilter_name());
        result.setStart_date(ad.getStart_date());
        result.setEnd_date(ad.getEnd_date());
        setDetailInfo(result);
        return result;
    }

    private void setDetailInfo(AD ad){
        ad.setCommonKeywordList(adDao.getAdCommonKeyword(ad));
        ad.setKeywordList(adDao.getAdKeyword(ad));
        ad.setShows(adDao.getListAdShowCnt(ad));
        ad.setHits(adDao.getListClickMemberCnt(ad));
    }

    public Integer save(AD ad) throws Exception {
        // 이미지 저장
        ad.setImages(
                FileUtils.saveFiles(
                        ad.getImages_new(),
                        uploadDir
                )
        );
        return adDao.insertAd(ad);
    }

    public Integer modify(AD ad) throws Exception {
        AD ori_ad = adDao.getAd(ad);
        // 이미지 수정
        if(ad.getImages_new() != null){
            ad.setImages(
                    FileUtils.modiOneFiles(
                            ori_ad.getImages(),
                            ad.getImages_new(),
                            uploadDir
                    )
            );
        }

        Integer result = adDao.updateAd(ad);

        // 공통키워드 수정
        // 1. 삭제
        if(ad.getCommonKeywordList_del() != null){
            for(int i=0; i<ad.getCommonKeywordList_del().size(); i++){
                ad.getCommonKeywordList_del().get(i).setAd(ad.getAd());
                adDao.deleteAdCommonKeyword(ad.getCommonKeywordList_del().get(i));
            }
        }
        if(ad.getCommonKeywordList() != null){
            // 2. 조회
            List<KEYWORD> adCommonKeyword = adDao.getAdCommonKeyword(ad);
            List<Integer> adCommonKeywordIdx = adCommonKeyword.stream().map(k -> k.getCommon_keyword()).collect(Collectors.toList());
            // 3. 겹치지 않는 키워드 입력
            List<KEYWORD> insertAdCommonKeyword
                    = ad.getCommonKeywordList().stream().filter(item -> !adCommonKeywordIdx.contains(item.getCommon_keyword())).collect(Collectors.toList());
            if(insertAdCommonKeyword.size() != 0){
                ad.setCommonKeywordList(insertAdCommonKeyword);
                adDao.insertAdCommonKeyword(ad);
            }
        }

        // 필수키워드 수정
        // 1. 삭제
        if(ad.getKeywordList_del() != null){
            for(int i=0; i<ad.getKeywordList_del().size(); i++){
                ad.getKeywordList_del().get(i).setAd(ad.getAd());
                adDao.deleteAdKeyword(ad.getKeywordList_del().get(i));
            }
        }
        if(ad.getKeywordList() != null) {
            // 2. 조회
            List<KEYWORD> adKeyword = adDao.getAdKeyword(ad);
            List<Integer> adKeywordIdx = adKeyword.stream().map(k -> k.getKeyword()).collect(Collectors.toList());
            // 3. 겹치지 않는 키워드 입력
            List<KEYWORD> insertAdKeyword
                    = ad.getKeywordList().stream().filter(item -> !adKeywordIdx.contains(item.getKeyword())).collect(Collectors.toList());
            if(insertAdKeyword.size() != 0){
                ad.setKeywordList(insertAdKeyword);
                adDao.insertAdKeyword(ad);
            }
        }

        return result;
    }

    public int modifyKeywordMandatory(AD ad) {
        // 공통 키워드
        ad.getCommonKeywordList().forEach(keyword -> adDao.updateAdCommonKeywordMandatory(keyword));
        // 필수 키워드
        ad.getKeywordList().forEach(keyword -> adDao.updateAdKeywordMandatory(keyword));
        return 1;
    }

    public int remove(AD ad) {
        return adDao.deleteAd(ad);
    }

    public List<KEYWORD> getClickMemberKeyword(AD ad) {
        return adDao.getClickMemberKeyword(ad);
    }
}
