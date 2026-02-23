package jp.haru_idea.springboot.ec_site.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
            .antMatchers("/backoffice/home").hasAnyRole("ADMIN","SUPPORT","SYSTEM","OWNER","CONTENT")
            .antMatchers("/user/create/**","/user/save", "/user/profile/password/reset/**").permitAll()
            .antMatchers("/product/shopping/index").permitAll()
            .antMatchers("/user/admin/search/customer").hasAnyRole("ADMIN","SUPPORT")  //指定URLに指定したロールユーザのみアクセス可能
            .antMatchers("/user/admin/index/**","/user/admin/search/member").hasAnyRole("ADMIN","SYSTEM","OWNER")
            .antMatchers("/user/admin/result/**").hasAnyRole("ADMIN","SUPPORT","SYSTEM","OWNER")
            .antMatchers("/user/admin/create","/user/admin/save","/user/admin/edit","/user/admin/withdraw").hasRole("ADMIN")
            .antMatchers("/user/admin/**").hasAnyRole("ADMIN","SYSTEM")
            // .antMatchers("/product/shopping/**").authenticated() //URLに認証を要求
            .antMatchers("/product/shopping/**").hasRole("ENDUSER")
            .antMatchers("/product/index","/product/edit/**","/product/update/**").hasAnyRole("ADMIN","SYSTEM","OWNER","CONTENT")
            .antMatchers("/product/**").hasAnyRole("ADMIN","SYSTEM","OWNER")
            .antMatchers("/user/**","/cart/**","/payment/**","/order-history/**").hasRole("ENDUSER")

            // .antMatchers("/user/**","/cart/**","/payment/**").authenticated()
            // .anyRequest().access(manager)
            // .anyRequest().authenticated()          //他のURLはログイン後のみアクセス可能

        .and()
        //ログインに関する設定
        .formLogin()                           //フォーム認証を有効化
            .loginPage("/login")                   //ログイン画面のURL
            .usernameParameter("usr")              //ユーザ名のパラメーター名を設定
            .passwordParameter("passwd")           //パスワードのパラメーター名を設定
            .defaultSuccessUrl("/home")      //ログイン成功後のリダイレクト先URL
            // .loginProcessingUrl("/login")          //ユーザ名・パスワードの送信先URL
            // .failureForwardUrl("/login")           //ログイン失敗時のリダイレクト先URL
        .and()
        //ログアウトに関する設定
        .logout()
            .logoutUrl("/user/logout")             //POSTでログアウト
            // .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))  //Getでログアウト
            .logoutSuccessHandler((request, response, authentication) -> {
                var authorities = authentication.getAuthorities();                
                boolean isEndUser = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ENDUSER"));
                if (isEndUser) {
                    response.sendRedirect("/home");       // エンドユーザログアウト後
                }else{
                    response.sendRedirect("/backoffice/home"); // 管理者ログアウト後
                }
            })
            // .logoutSuccessUrl("/home")           //ログアウト成功後のリダイレクト先URL
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
