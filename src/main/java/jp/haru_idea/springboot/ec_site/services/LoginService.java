package jp.haru_idea.springboot.ec_site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.repositories.UserRepository;
import jp.haru_idea.springboot.ec_site.securities.LoginUserDetails;

@Service
public class LoginService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleUserService roleUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByMail(username);
        if(user != null){
            return new LoginUserDetails(user, AuthorityUtils.createAuthorityList(roleUserService.getByUserId(user.getId())));
        } else {
            throw new UsernameNotFoundException("User is not found");
        }
    }
}
