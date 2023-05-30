package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class SERVICE {
    private Integer service;
    private String title;
    private String content;
    private String link;
    private Date regdate;
    private Date upddate;
}
