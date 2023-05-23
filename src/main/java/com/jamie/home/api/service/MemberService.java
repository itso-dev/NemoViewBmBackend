package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.ROLE;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.CodeUtils;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService extends BasicService{
    public List<MEMBER> list(SEARCH search) {
        return memberDao.getListMember(search);
    }

    public Integer listCnt(SEARCH search) {
        return memberDao.getListMemberCnt(search);
    }

    public MEMBER get(MEMBER member){
        return memberDao.getMember(member);
    }

    public Integer save(MEMBER member) throws JsonProcessingException {
        // 비밀번호 암호화
        member.setPassword(encoder.encode(member.getPassword()));
        member.setRole(ROLE.ROLE_USER);

        Integer result = memberDao.insertMember(member);
        return result;
    }

    public Integer modify(MEMBER member) throws Exception {
        MEMBER ori_member = memberDao.getMember(member);

        if(member.getPassword() != null){
            member.setPassword(encoder.encode(member.getPassword()));
        }

        // 사업자등록증 수정
        if(member.getTax_file_new() != null){
            member.setTax_file(
                    FileUtils.modiOneFiles(
                            ori_member.getTax_file(),
                            member.getTax_file_new(),
                            uploadDir
                    )
            );
        }

        return memberDao.updateMember(member);
    }

    public MEMBER getByParam(MEMBER member) {
        return memberDao.getByParam(member);
    }

    public MEMBER checkDuple(MEMBER member) {
        MEMBER result = memberDao.getByParam(member);

        if(result == null){ // 인증코드 생성
            member.setCode(CodeUtils.getCodeValue());
            return member;
        }
        return result;
    }
}
