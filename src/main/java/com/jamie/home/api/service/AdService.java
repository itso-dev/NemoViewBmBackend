package com.jamie.home.api.service;

import com.jamie.home.api.model.KEYWORD;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.model.AD;
import com.jamie.home.api.service.BasicService;
import com.jamie.home.util.FileUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdService extends BasicService {
    public List<AD> list(SEARCH search) {
        return adDao.getListAd(search);
    }

    public Integer listCnt(SEARCH search) {
        return adDao.getListAdCnt(search);
    }

    public AD get(AD ad){
        return adDao.getAd(ad);
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
        ad.setImages(
                FileUtils.modiFiles(
                        ori_ad.getImages(),
                        ad.getImages_del(),
                        ad.getImages_new(),
                        uploadDir
                )
        );

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
        List<KEYWORD> commonKeywords = ad.getCommonKeywordList().stream().filter(k -> k.getMandatory()).collect(Collectors.toList());
        commonKeywords.forEach(keyword -> adDao.updateAdCommonKeyword(keyword));
        // 필수 키워드
        List<KEYWORD> mandatoryKeyowrds = ad.getKeywordList().stream().filter(k -> k.getMandatory()).collect(Collectors.toList());
        mandatoryKeyowrds.forEach(keyword -> adDao.updateAdKeyword(keyword));
        return 1;
    }
}
