package jp.haru_idea.springboot.ec_site.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginUserDetails implements UserDetails{
    private User user;
    private Collection<GrantedAuthority> authorities;

    public LoginUserDetails(User user, Collection<GrantedAuthority> authorities){
        this.user = user;
        this.authorities = authorities;
    }
    
    public LoginUserDetails(){

    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(){
        return authorities;
    }

    @Override
    public String getUsername() {
        return user.getMail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
