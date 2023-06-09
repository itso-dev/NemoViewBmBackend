package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class POINT {
    private Integer member_point;
    private Integer member;
    private Integer type;
    private Integer point;
    private Integer accumulate;
    private String content;
    private String bank;
    private String account;
    private Date regdate;
    private Date upddate;

    private MEMBER member_info;

    public POINT() {};

    public POINT(Integer member, Integer type, Integer point, Integer accumulate, String content) {
        this.member = member;
        this.type = type;
        this.point = point;
        this.accumulate = accumulate;
        this.content = content;
    }
}
