package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.RoleUser;
import jp.haru_idea.springboot.ec_site.repositories.RoleUserRepository;

@Service
public class RoleUserService {
    @Autowired
    private RoleUserRepository roleUserRepository;

    public Collection<RoleUser> getAll(){
        return roleUserRepository.findAll();
    }

    public RoleUser getById(int id){
        return roleUserRepository.findById(id);
    }

    public Collection<RoleUser> getAllbyId(int id){
        return roleUserRepository.findAllById(id);
    }

    public String[] getByUserId(int userId){
        List<RoleUser> roleUsers = roleUserRepository.findByUserId(userId); 
        String[] roles = new String[roleUsers.size()];
        for(int i = 0; i < roleUsers.size(); i++ ){
            roles[i] = roleUsers.get(i).getRole().getName();
        }
        return roles;
    }
    // public RoleUser getByUserId(int userId){
    //     return roleUserRepository.findByUserId(userId);
    // }

}