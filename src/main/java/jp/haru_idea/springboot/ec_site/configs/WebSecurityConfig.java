package jp.haru_idea.springboot.ec_site.configs;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.authentication.RequestMatcherDelegatingAuthenticationManagerResolver;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jp.haru_idea.springboot.ec_site.services.LoginUserService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
// @EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {
    // @Autowired
    // private LoginUserService loginUserService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    // public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthorizationManager<RequestAuthorizationContext> manager) throws Exception{
        http
        //アクセス制限に関する設定
        .authorizeHttpRequests()
        // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll  //"/CSS/**"などはログインなしでもアクセス可能         
        .antMatchers("/login").permitAll()     //指定URLに全てのユーザがアクセス可能
        .antMatchers("/user/create").permitAll()
        .antMatchers("/user/profile/password/reset/**").permitAll()
        .antMatchers("/cart/**").permitAll()
        // .antMatchers("/user/index").hasRole("ADMIN")  //指定URLに指定したロールユーザのみアクセス可能
        .mvcMatchers("/product/**").permitAll()
        // .anyRequest().access(manager)
        // .antMatchers("/**/{userId}").access(manager)
        // .antMatchers("/**/{id}").access("@resourceAccessControl.checkUserId(authentication, userId)")
        .anyRequest().authenticated()          //他のURLはログイン後のみアクセス可能
        .and()
        //ログインに関する設定
        .formLogin()                           //フォーム認証を有効化
        .loginPage("/login")                   //ログイン画面のURL
        .usernameParameter("usr")              //ユーザ名のパラメーター名を設定
        .passwordParameter("passwd")           //パスワードのパラメーター名を設定
        // .loginProcessingUrl("/login")          //ユーザ名・パスワードの送信先URL
        .defaultSuccessUrl("/product/index")      //ログイン成功後のリダイレクト先URL
        // .successForwardUrl("/product/index")      //ログイン成功後のリダイレクト先URL
        // .failureForwardUrl("/login")           //ログイン失敗時のリダイレクト先URL
        .and()
        //ログアウトに関する設定
        .logout()                             
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  //ログアウトのためのURL
        .logoutSuccessUrl("/index")           //ログアウト成功後のリダイレクト先URL
        // .deleteCookies("")                    //ログアウト時に削除するクッキー名
        .invalidateHttpSession(true);         //ログアウト時のセッション破棄有無(tureは破棄)
        return http.build();
    }

    // @Bean
    // public AuthorizationManager<RequestAuthorizationContext> manager(){
    //     AuthorizationManager<HttpServletRequest> manager = RequestMatcherDelegatingAuthorizationManager.builder()
    //         .add(new AntPathRequestMatcher("/**/{userId}"), AuthorityAuthorizationManager.hasAuthority("admin"))
    //         .build();
    //     return(authentication, context) -> manager.check(authentication, context.getRequest());
    // }

    // @Bean 
    // public UserDetailsService userDetailsService() { 
    //     return loginUserService;
    // }


    // @Bean
    // public UserDetailsManager userDetailsManager(DataSource dataSource){
    //     UserDetails userDetails = User.builder()
    //         .username("test1@test")
    //         .password("$2a$08$71yWnvzHyJa7IruW94lHAell7pMhXkRssEMkVdYg6TGFXYGxBaYle")
    //         .roles()
    //         .build();
    //     JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
    //     users.createUser(userDetails);
    //     return users;
    // }


    // @Bean
    // public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
    //     UserDetails userDetails = User.builder()
    //         .username("test1@test")
    //         .password("$2a$08$71yWnvzHyJa7IruW94lHAell7pMhXkRssEMkVdYg6TGFXYGxBaYle")
    //         .roles("")
    //         .build();
    //     return new InMemoryUserDetailsManager(userDetails);
    // }

    
    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
    //     return auth.getAuthenticationManager();
    // }
    
    
    // @Bean
    // public CustomUserDetailsService customUserDetailService(){
    //     return new CustomUserDetailsService();
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }

}
