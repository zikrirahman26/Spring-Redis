package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ValidatorService validatorService;

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEYPREFIX = "MBR";

    //Db
    public Member addMember(Member request){
        validatorService.validation(request);
        
        Member member = new Member();
        member.setName(request.getName());
        member.setJob(request.getJob());
        member.setTittle(request.getTittle());
        memberRepository.save(member);
        addMemberToRedis(member);
        return member;
    }

    public Member getMember(String memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "member " + memberId + " not found"));
        return member;
    }

    public List<Member> getAllMember(){
        List<Member> member = memberRepository.findAll();
        return member;
    }

    public Member updateMember(Member member, String memberId){
        Member members = getMember(memberId);
        if (members != null) {
            members.setName(member.getName());
            members.setJob(member.getJob());
            members.setTittle(member.getTittle());
            memberRepository.save(members);
            addMemberToRedis(members);
            return members;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not found");
    }

    public boolean deleteMember(String memberId){
        Member member = getMember(memberId);
        if (member != null) {
            memberRepository.deleteById(memberId);
            deleteMemberFromRedis(memberId);
            return true;
        }
        return false;
    }

    //Redis
    @SuppressWarnings("unchecked")
    public void addMemberToRedis(Member member) {
        String redisKey = KEYPREFIX + member.getId();
        redisTemplate.opsForHash().put(redisKey, "data", member);
    }
    
    @SuppressWarnings("unchecked")
    public Member getMemberFromRedis(String memberId) {
        String redisKey = KEYPREFIX + memberId;
        return (Member) redisTemplate.opsForHash().get(redisKey, "data");
    }

    @SuppressWarnings("unchecked")
    public List<Member> getAllMemberFromRedis() {
        List<Member> members = new ArrayList<>();
        Set<Object> redisKeys = redisTemplate.keys(KEYPREFIX + "*");

        for (Object redisKey : redisKeys) {
            Member member = (Member) redisTemplate.opsForHash().get(redisKey, "data");
            members.add(member);
        }

        return members;
    }

    @SuppressWarnings({ "unused", "unchecked" })
    private void deleteMemberFromRedis(String memberId) {
        String redisKey = KEYPREFIX + memberId;
        redisTemplate.delete(redisKey);
    }
}