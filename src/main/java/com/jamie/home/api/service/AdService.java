package com.jamie.home.api.service;

import com.jamie.home.api.model.*;
import com.jamie.home.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public List<AD> listForUser(SEARCH search) {
        List<AD> list = adDao.getListAd(search);
        if(search.getUser_member() != null){
            AD param = new AD();
            param.setMember(search.getUser_member());
            for(int i=0; i<list.size(); i++){
                adDao.insertAdShow(list.get(i));
                list.get(i).setCommonKeywordList(adDao.getAdCommonKeyword(list.get(i)));
                list.get(i).setKeywordList(adDao.getAdKeyword(list.get(i)));
                param.setAd(list.get(i).getAd());
                if(adDao.getAdLike(param) > 0){
                    list.get(i).setLikeYn(true);
                } else {
                    list.get(i).setLikeYn(false);
                }
            }
        }
        return list;
    }

    public List<AD> listForAdmin(SEARCH search) {
        List<AD> list = adDao.getListAd(search);
        MEMBER param = null;
        for(int i=0; i<list.size(); i++){
            list.get(i).setIsOver(!adDao.getIsNotOverTodayPrice(list.get(i)));
            if(search.getMember() != null){ // 광고주 상세
                list.get(i).setShows(adDao.getListAdShowCnt(list.get(i)));
                list.get(i).setHits(adDao.getListClickMemberCnt(list.get(i)));
            } else { // 광고 리스트
                list.get(i).setShows(adDao.getListAdShowCnt(list.get(i)));
                list.get(i).setHits(adDao.getListClickMemberCnt(list.get(i)));
                //setDetailInfo(list.get(i));
                list.get(i).setLeft_price(adDao.getTodayLeftDayPrice(list.get(i)));
                if(list.get(i).getLeft_price() < 0){
                    list.get(i).setLeft_price(0);
                }
                list.get(i).setMember_info(new MEMBER(list.get(i).getMember()));
                list.get(i).setMember_info(memberDao.getMember(list.get(i).getMember_info()));
                list.get(i).getMember_info().setPoint(memberDao.getMemberPoint(list.get(i).getMember_info()));
            }
        }
        return list;
    }

    public Integer listCnt(SEARCH search) {
        return adDao.getListAdCnt(search);
    }

    public AD getForAdmin(AD ad){
        AD result = adDao.getAd(ad);
        result.setFilter_name(ad.getFilter_name());
        result.setStart_date(ad.getStart_date());
        result.setEnd_date(ad.getEnd_date());
        result.setIsOver(!adDao.getIsNotOverTodayPrice(result));
        setDetailInfo(result);
        result.setLeft_price(adDao.getTodayLeftDayPrice(result));
        if(result.getLeft_price() < 0){
            result.setLeft_price(0);
        }
        result.setMember_info(new MEMBER(result.getMember()));
        result.setMember_info(memberDao.getMember(result.getMember_info()));
        result.getMember_info().setPoint(memberDao.getMemberPoint(result.getMember_info()));
        return result;
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

    public Integer modifyState(AD ad) {
        AD ori_ad = adDao.getAd(ad);
        // 0: 임시저장(광고셋팅중), 1: 저장완료(검수중), 2: 검수완료(진행중), 3: 광고중지 (STATE_ERROR 수정), 4: 광고반려
        if(ori_ad.getState() == 0 && ad.getState() == 1){
            // 알림 TYPE 1
            INFO info = new INFO(ori_ad.getMember(),ori_ad.getTitle()+" 광고가 검수 작업에 들어갔습니다.");
            infoDao.insertInfo(info);
        } else if(ori_ad.getState() == 1 && ad.getState() == 2){
            // 알림 TYPE 2
            INFO info = new INFO(ori_ad.getMember(),ori_ad.getTitle()+" 광고가 검수 완료하였습니다.");
            infoDao.insertInfo(info);
            MEMBER member = new MEMBER();
            member.setMember(ori_ad.getMember());
            if(memberDao.getMemberPoint(member) <= 0){
                ad.setState(3);
                ad.setState_error("보유 포인트 부족");
            }
        } else if(ori_ad.getState() == 1 && ad.getState() == 4){
            // 알림 TYPE 5
            INFO info = new INFO(ori_ad.getMember(),ori_ad.getTitle()+" 광고가 반려되었습니다.");
            infoDao.insertInfo(info);
        } else if(ori_ad.getState() == 2 && ad.getState() == 3){ // 광고 중지
            // 알림 TYPE 3
            INFO info = new INFO(ori_ad.getMember(),ori_ad.getTitle()+" 광고가 중지 되었습니다.\n재개를 원하실 경우 광고 페이지에서 재설정해주세요!");
            infoDao.insertInfo(info);
        } else if(ori_ad.getState() == 3 && ad.getState() == 2){ // 광고 재개
            // 알림 TYPE 4
            INFO info = new INFO(ori_ad.getMember(),ori_ad.getTitle()+" 광고가 다시 게시되었습니다.");
            infoDao.insertInfo(info);
        }
        return adDao.updateAd(ad);
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

    public int saveAdLike(AD ad) {
        if(adDao.getAdLike(ad) > 0){
            adDao.deleteAdLike(ad);
        } else {
            adDao.insertAdLike(ad);
        }
        return 1;
    }

    public int saveAdHit(AD ad) {
        // 클릭당 비용 삭감
        AD ori_ad = adDao.getAd(ad);
        // 오늘 클릭 수 확인
        ori_ad.setFilter_name("today");
        setDetailInfo(ori_ad);
        MEMBER ad_member = memberDao.getMember(new MEMBER(ori_ad.getMember()));
        Integer member_point = memberDao.getMemberPoint(ad_member);

        if(member_point == null || member_point <= 0){ // 광고 중지 (보유 포인트 부족)
            adDao.updateAdStateAll(ori_ad);
        } else {
            if(ori_ad.getClick_price() >= member_point){ // 포인트 차감 후 광고 중지 (보유 포인트 부족)
                memberDao.insertMemberPoint(new POINT(ad_member.getMember(), 1, member_point*(-1), ori_ad.getTitle() + " 광고 클릭 비용 (보유 포인트 부족)"));
                adDao.updateAdStateAll(ori_ad);
            } else {
                if(ori_ad.getDay_price() <= (ori_ad.getClick_price() * ori_ad.getHits())){ // 하루예산 초과

                } else {
                    if(ori_ad.getDay_price() - (ori_ad.getClick_price() * ori_ad.getHits()) >= ori_ad.getClick_price()){
                        memberDao.insertMemberPoint(new POINT(ad_member.getMember(), 1, ori_ad.getClick_price()*(-1), ori_ad.getTitle() + " 광고 클릭 비용"));
                    } else {
                        memberDao.insertMemberPoint(new POINT(ad_member.getMember(), 1, (ori_ad.getDay_price() - (ori_ad.getClick_price() * ori_ad.getHits()))*(-1), ori_ad.getTitle() + " 광고 클릭 비용 (하루 예산 초과)"));
                    }
                }
            }
        }

        Integer re_member_point = memberDao.getMemberPoint(ad_member);
        if(re_member_point == null){

        } else if(re_member_point <= 0){
            // 알림 TYPE 7
            INFO info = new INFO(ad_member.getMember(),"포인트가 모두 소진되었습니다.");
            infoDao.insertInfo(info);
        } else if(re_member_point <= 1000){
            // 알림 TYPE 8
            INFO info = new INFO(ad_member.getMember(),"포인트가 1,000P 이하입니다.");
            infoDao.insertInfo(info);
        }

        return adDao.insertAdHit(ad);
    }

    public List<AD> listRandom(SEARCH search) {
        adDao.insertMemberAdValid(search); // 진행중 외 광고들 삭제 후 다시 셋팅
        List<AD> list = adDao.getAdListRandom(search);
        AD param = new AD();
        param.setMember(search.getUser_member());
        for(int i=0; i<list.size(); i++){
            adDao.insertAdShow(list.get(i));
            list.get(i).setCommonKeywordList(adDao.getAdCommonKeyword(list.get(i)));
            list.get(i).setKeywordList(adDao.getAdKeyword(list.get(i)));
            param.setAd(list.get(i).getAd());
            if(adDao.getAdLike(param) == 0){
                list.get(i).setLikeYn(false);
            } else {
                list.get(i).setLikeYn(true);
            }
        }
        return list;
    }
}
