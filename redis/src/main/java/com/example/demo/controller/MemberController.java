package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.WebResponse;
import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;

@RestController
@RequestMapping("/api")
public class MemberController {
    
    @Autowired
    private MemberService memberService;

    //Db
    @PostMapping("/members")
    public WebResponse<Member> addMember(@RequestBody Member request){
        Member response = memberService.addMember(request);
        return WebResponse.<Member>builder().data(response).build();
    }

    @GetMapping("/members/{memberId}")
    public WebResponse<Member> getMember(@PathVariable String memberId){
        Member response = memberService.getMember(memberId);
        return WebResponse.<Member>builder().data(response).build();
    }

    @GetMapping("/members")
    public WebResponse<List<Member>> getAllMember(){
        List<Member> response = memberService.getAllMember();
        return WebResponse.<List<Member>>builder().data(response).build();
    }

    @PatchMapping("/members/{memberId}")
    public WebResponse<Member> updateMemebr(@RequestBody Member member, @PathVariable String memberId){
        Member response = memberService.updateMember(member, memberId);
        return WebResponse.<Member>builder().data(response).build();
    }
    
    @DeleteMapping("/members/{memberId}")
    public WebResponse<Object> deleteMember(@PathVariable String memberId){
        memberService.deleteMember(memberId);
        return WebResponse.builder().data(memberId).build();
    }

    //Redis
    @GetMapping("members/redis/{memberId}")
    public WebResponse<Member> getMemberFromRedis(@PathVariable String memberId) {
        Member response = memberService.getMemberFromRedis(memberId);
        return WebResponse.<Member>builder().data(response).build();
    }

    @GetMapping("/members/redis")
    public WebResponse<List<Member>> getAllMemberFromRedis(){
        List<Member> response = memberService.getAllMemberFromRedis();
        return WebResponse.<List<Member>>builder().data(response).build();
    }
}
