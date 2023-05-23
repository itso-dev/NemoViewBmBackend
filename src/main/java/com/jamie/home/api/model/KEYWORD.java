package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KEYWORD {
    private Integer keyword;
    private Integer common_keyword;
    private Integer ad_common_keyword;
    private Integer ad_keyword;
    private Boolean mandatory;
    private Integer ad;
    private Integer member;
    private Integer review;
    private Integer category;
    private Integer classification;
    private String name;
    private Integer cnt;
}
