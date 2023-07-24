package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class INFO {
    private Integer member_info;
    private Integer member;
    private Integer type;
    private Integer key;
    private String content;
    private Boolean chk;
    private Date regdate;
    private Date upddate;

    public INFO () {};

    public INFO(Integer member, Integer type, Integer key, String content) {
        // type 1: 광고 상태 변경, 2: 포인트관련
        this.member = member;
        this.type = type;
        this.key = key;
        this.content = content;
    }
}
