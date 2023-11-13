package jp.haru_idea.springboot.ec_site.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.LoginUserDetails;
import jp.haru_idea.springboot.ec_site.models.Role;
import jp.haru_idea.springboot.ec_site.models.RoleUser;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.repositories.RoleRepository;
import jp.haru_idea.springboot.ec_site.repositories.RoleUserRepository;
import jp.haru_idea.springboot.ec_site.repositories.UserRepository;

@Service
public class LoginUserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private RoleUserService roleUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByMail(username);
        if(user != null){
            //ロールが1つの時
            // RoleUser roleUser = roleUserRepository.findByUserId(user.getId());
            // System.out.println("******" + roleUser.getRole().getClass().getSimpleName()); //Role
            // System.out.println("///////" + roleUser.getRole().getName()); 
            System.out.println("++++++" + getGrantedAuthorities() + ": " + getGrantedAuthorities().getClass().getSimpleName());
            System.out.println("------" + AuthorityUtils.createAuthorityList(roleUserService.getByUserId(user.getId())));

            // return new org.springframework.security.core.userdetails.User(username, users.getPassword(), AuthorityUtils.createAuthorityList(new String[] {roleUsers.getRole().getName()}));

            // エラーにならない
            return new LoginUserDetails(user, AuthorityUtils.createAuthorityList(roleUserService.getByUserId(user.getId())));

            // System.out.println("++++++" + getGrantedAuthorities());
            // return new LoginUserDetails(user, getGrantedAuthorities());

            // String query = "SELECT a FROM role_users a WHERE EXISTS "
            //             + "(SELECT 1 FROM users b WHERE b MEMBER OF a.users AND b.id = :id)";
            // List<Role> roles = em.createQuery(jpql, Role.class).setParameter("id", users.getId()).getResultList();
            

        // List<User> users = userRepository.findByMail(username);
        // System.out.println("+------" + users.get(0) );
        //     System.out.println("------" + users.get(0) );
        //     return new LoginUserDetails(users.get(0), getGrantedAuthorities());
        //     // return new User(mail, user.getByPassword(),
        //     // Collections.emptySet());
        } else {
            throw new UsernameNotFoundException("User is not found");
        }
    }
    private Collection<GrantedAuthority> getGrantedAuthorities() {
        return AuthorityUtils.createAuthorityList(new String[] {"USER"});
    }


}
