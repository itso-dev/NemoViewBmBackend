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

    // 광고
    private String filter_name;
    private String start_date;
    private String end_date;

    public void calStart(){
        this.start = (this.page-1) * this.page_block;
    }
}
