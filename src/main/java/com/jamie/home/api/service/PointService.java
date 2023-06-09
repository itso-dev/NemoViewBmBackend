package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamie.home.api.model.*;
import com.jamie.home.util.CodeUtils;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PointService extends BasicService{
    public List<POINT> list(SEARCH search) {
        return pointDao.getListPoint(search);
    }

    public List<POINT> listForAdmin(SEARCH search) {
        List<POINT> list = pointDao.getListPoint(search);
        for(int i=0; i<list.size(); i++){
            setDetailInfoForAdmin(list.get(i));
        }
        return list;
    }

    private void setDetailInfoForAdmin(POINT point){
        point.setMember_info(new MEMBER(point.getMember()));
        point.setMember_info(memberDao.getMember(point.getMember_info()));
        point.getMember_info().setPoint(memberDao.getMemberPoint(point.getMember_info()));
    }

    public Integer listCnt(SEARCH search) {
        return pointDao.getListPointCnt(search);
    }

    public Integer save(POINT point) {
        point.setAccumulate(memberDao.getMemberPoint(new MEMBER(point.getMember())) + point.getPoint());
        return pointDao.insertPoint(point);
    }

    public Integer modify(POINT point) {
        return pointDao.updatePoint(point);
    }

    public Map<String, Integer> listCntForAdmin(SEARCH search) {
        Map<String, Integer> result = new HashMap<>();
        search.setType(0); // 총 갯수
        result.put("pointTotCnt", pointDao.getListPointCnt(search));
        search.setType(2); // 포인트 지급
        result.put("pointTot", pointDao.getPointTot(search));
        result.put("pointCnt", pointDao.getPointCnt(search));
        search.setType(3); // 포인트 환불 신청
        result.put("refundCnt", pointDao.getPointCnt(search));
        search.setType(4); // 포인트 환불 완료
        result.put("refundCompleteTot", pointDao.getPointTot(search));
        result.put("refundCompleteCnt", pointDao.getPointCnt(search));
        return result;
    }

    public int modifyAll(SEARCH search) {
        for(int i=0; i<search.getChkPointList().size(); i++){
            search.getChkPointList().get(i).setType(4); // 환불 완료
            pointDao.updatePoint(search.getChkPointList().get(i));
        }
        return 1;
    }
}
