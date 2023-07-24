package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class SEARCH {
    // 리스트
    private Integer page;
    private Integer page_block;
    private Integer start;
    private String orderType;
    private String searchType;
    private String searchKeyword;

    // 회원
    private Integer member;
    private String email;
    private Boolean chk;

    // 광고
    private Integer ad;
    private Integer type;
    private Integer category;
    private String filter_name;
    private String start_date;
    private String end_date;
    private Integer state;
    private List<KEYWORD> commonKeywordList; // 공통
    private List<KEYWORD> commonKeywordList_gender; // 공통
    private List<KEYWORD> commonKeywordList_age; // 공통
    private List<KEYWORD> keywordList; // 필수
    private long commonKeywordList_mandatory;
    private long commonKeywordList_gender_mandatory;
    private long commonKeywordList_age_mandatory;
    private long keywordList_mandatory;

    // 사용자
    private Integer user_member;
    private Integer member_ad;
    private Boolean like;

    // 관리자
    private Boolean showYn;
    private List<POINT> chkPointList;

    public void calStart(){
        this.start = (this.page-1) * this.page_block;
    }
}
