package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Role;
import jp.haru_idea.springboot.ec_site.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getById(int id){
        return roleRepository.findById(id);
    }   
    public Role getByName(String name){
        return roleRepository.findByName(name);
    }   

    public Collection<Role> getExcludedName(String name){
        return roleRepository.findByNameNotIn(List.of(name));
    }
}
