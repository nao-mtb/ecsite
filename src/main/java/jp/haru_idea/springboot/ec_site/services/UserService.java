package jp.haru_idea.springboot.ec_site.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.RoleType;
import jp.haru_idea.springboot.ec_site.models.RoleUser;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleUserService roleUserService;

    // @Autowired
    // BCryptPasswordEncoder passwordEncoder;

    // @Autowired
    // PasswordEncoder passwordEncoder;

    public Collection<User> getAll(){
        return userRepository.findAll();
    }

    public Collection<User> getUsersByRoleType(RoleType roleType){
        Collection<RoleUser> roleUsers = roleUserService.getRoleType(roleType);
        Collection<User> users = new ArrayList<User>();
        for(RoleUser roleUser : roleUsers){
            users.add(roleUser.getUser());
        }
        return users;
    }

    public Collection<User> getUsersByNotRoleType(RoleType roleType){
        Collection<RoleUser> roleUsers = roleUserService.getNotRoleType(roleType);
        Collection<User> users = new ArrayList<User>();
        for(RoleUser roleUser : roleUsers){
            if (!users.contains(roleUser.getUser())){
                users.add(roleUser.getUser());                
            }
        }
        return users;
    }

    public Collection<User> getInternalUsers(RoleType roleType){
        Collection<User> allUsers = userRepository.findAll();
        Collection<User> notEndUsers = new ArrayList<User>();
        for(User user : allUsers){
            if(!(user.getRoleUsers().stream().map(roleUser -> roleUser.getRole().getRoleType()).collect(Collectors.toList()).contains(roleType))){
                notEndUsers.add(user);
            }
        }
        return notEndUsers;
    }
    // @Bean
    // public PasswordEncoder passwordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }
    
    public void save(User user){
        userRepository.save(user);
    }

    // public void save(User user, int passwordFlg){
    //     if(passwordFlg == 1){
    //         user.setPassword(passwordEncoder.encode(user.getPassword()));            
    //     }
    //     userRepository.save(user);
    // }

    public void delete(int id){
        userRepository.deleteById(id);
    }   

    public void activateAccount(User user){
        user.setDeleteFlag(0);
        save(user);
    }

    public void deactivateAccount(User user){
        user.setDeleteFlag(1);
        save(user);
    }

    public User getById(int id){
        return userRepository.findById(id);
    }

    public User getByMail(String mail){
        return userRepository.findByMail(mail);
    }

    public boolean isUserExists(String mail){
        return getByMail(mail) != null;
    }

    public Collection<User> searchEndUsers(int roleId, String lastName, String firstName){
        return userRepository.findUsersByRoleAndName(roleId, lastName, firstName);
    }

    public Collection<User> searchInternalUsers(int roleId, String lastName){
        return userRepository.findUsersByRoleAndLastName(roleId, lastName);
    }


    public boolean isEndUser(User user){
        return user.getRoleUsers().stream().anyMatch(roleUser -> roleUser.getRole().getRoleType().equals(RoleType.ROLE_ENDUSER));
    }
}
