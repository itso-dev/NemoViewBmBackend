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
    private String content;
    private Boolean chk;
    private Date regdate;
    private Date upddate;

    public INFO () {};

    public INFO(Integer member, String content) {
        this.member = member;
        this.content = content;
    }
}
