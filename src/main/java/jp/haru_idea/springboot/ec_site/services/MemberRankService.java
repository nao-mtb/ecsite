package jp.haru_idea.springboot.ec_site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.MemberRank;
import jp.haru_idea.springboot.ec_site.repositories.MemberRankRepository;

@Service
public class MemberRankService {
    @Autowired
    private MemberRankRepository memberRankRepository;

    public MemberRank getById(int id){
        return memberRankRepository.findById(id);
    }
}
