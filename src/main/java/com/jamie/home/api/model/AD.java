package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class AD {
    private Integer ad;
    private Integer member;
    private String title;
    private Integer type;
    private Integer category;
    private String brand;
    private String name;
    private Integer price;
    private Integer discount;
    private String link;
    private String keywords;
    private String images;
    private Boolean image_support;
    private Integer click_price;
    private Integer day_price;
    private Integer shows;
    private Integer hits;
    private Integer state;
    private String state_error;
    private Date regdate;
    private Date upddate;

    private ArrayList<MultipartFile> images_new;
    private String images_del;
    private List<KEYWORD> commonKeywordList; // 공통
    private List<KEYWORD> commonKeywordList_del; // 공통
    private List<KEYWORD> keywordList; // 필수
    private List<KEYWORD> keywordList_del; // 필수

    private String filter_name;
    private String start_date;
    private String end_date;

    private Boolean likeYn;
    private Boolean isOver;
    private Integer left_price;
    private MEMBER member_info;
}
