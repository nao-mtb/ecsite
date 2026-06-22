package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jp.haru_idea.springboot.ec_site.models.Role;
import jp.haru_idea.springboot.ec_site.models.RoleType;
import jp.haru_idea.springboot.ec_site.models.RoleUser;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.repositories.RoleUserRepository;

@Service
public class RoleUserService {
    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private RoleService roleService;

    public Collection<RoleUser> getAll(){
        return roleUserRepository.findAll();
    }

    public RoleUser getById(int id){
        return roleUserRepository.findById(id);
    }

    public Collection<RoleUser> getAllById(int id){
        return roleUserRepository.findAllById(id);
    }

    public String[] getByUserId(int userId){
        List<RoleUser> roleUsers = roleUserRepository.findByUserId(userId); 
        String[] roles = new String[roleUsers.size()];
        for(int i = 0; i < roleUsers.size(); i++ ){
            roles[i] = roleUsers.get(i).getRole().getRoleType().getRoleName();
        }
        return roles;
    }

    public boolean hasSpecificRole(int userId, RoleType roleType){
        String roles[] = getByUserId(userId);
        return Arrays.asList(roles).contains(roleType.toString());
    }
    
    // public RoleUser getByUserId(int userId){
    //     return roleUserRepository.findByUserId(userId);
    // }

    public void deleteByUserId(int userId){
        roleUserRepository.deleteByUserId(userId);
    }

    public void addRoleUser(User user, RoleType roleType){
        RoleUser roleUser = new RoleUser();
        roleUser.setRole(roleService.getByRoleType(roleType));
        roleUser.setUser(user);
        roleUserRepository.save(roleUser);
    }

    @Transactional
    public void editRoleUser(User user, List<RoleType> roleTypes){
        roleUserRepository.deleteByUserId(user.getId());
        if (!CollectionUtils.isEmpty(roleTypes)){
            for (RoleType roleType : roleTypes){
                addRoleUser(user, roleType);
            }
        }
    }

    @Transactional
    public void deleteAllRole(User user){
        roleUserRepository.deleteByUserId(user.getId());
    }

    public Collection<RoleUser> getRoleType(RoleType roleType){
        Role role = roleService.getByRoleType(roleType);
        return roleUserRepository.findByRole(role);
    }

    public Collection<RoleUser> getNotRoleType(RoleType roleType){
        Role role = roleService.getByRoleType(roleType);
        return roleUserRepository.findByRoleNotIn(List.of(role));
    }

}