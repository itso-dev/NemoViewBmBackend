package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class POINT {
    private Integer member;
    private Integer type;
    private Integer point;
    private String content;
    private Date regdate;
    private Date upddate;

    public POINT() {};

    public POINT(Integer member, Integer type, Integer point, String content) {
        this.member = member;
        this.type = type;
        this.point = point;
        this.content = content;
    }
}
