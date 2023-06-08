package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class DASH {
    private Integer member_new;
    private Integer member_tot;
    private Integer point_new;
    private Integer point_tot;
    private List<CATEGORY> categoryRank;
    private List<CATEGORY> serviceCategoryRank;
    private List<KEYWORD> commonKeywordRank;
    private List<KEYWORD> keywordRank;
}
