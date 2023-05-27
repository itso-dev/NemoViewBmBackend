package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class MEMBER {
    private Integer member;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String company;
    private String position;
    private Integer company_size;
    private Integer inflow;
    private String tax_name;
    private String tax_phone;
    private String tax_email;
    private String tax_file;
    private Boolean info_mobile;
    private Boolean info_email;
    @Enumerated(EnumType.STRING)
    private ROLE role;
    private Date regdate;
    private Date upddate;

    private String code;
    private Integer point;

    private ArrayList<MultipartFile> tax_file_new;
}
