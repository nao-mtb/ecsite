package jp.haru_idea.springboot.ec_site.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    // @Autowired
    // private LoginUserService loginUserService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
        //アクセス制限にする設定
        .authorizeHttpRequests(auth -> auth
            // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll  //"/CSS/**"などはログインなしでもアクセス可能
            .requestMatchers("/login").permitAll()     //指定URLに全てのユーザがアクセス可能
            .requestMatchers("/backoffice/home").hasAnyRole("ADMIN","SUPPORT","SYSTEM","OWNER","CONTENT")
            .requestMatchers("/user/create/**","/user/save", "/user/profile/password/reset/**").permitAll()
            .requestMatchers("/product/shopping/index").permitAll()
            .requestMatchers("/user/admin/search/customer").hasAnyRole("ADMIN","SUPPORT")  //指定URLに指定したロールユーザのみアクセス可能
            .requestMatchers("/user/admin/index/**","/user/admin/search/member").hasAnyRole("ADMIN","SYSTEM","OWNER")
            .requestMatchers("/user/admin/result/**").hasAnyRole("ADMIN","SUPPORT","SYSTEM","OWNER")
            .requestMatchers("/user/admin/create","/user/admin/save","/user/admin/edit","/user/admin/withdraw").hasRole("ADMIN")
            .requestMatchers("/user/admin/**").hasAnyRole("ADMIN","SYSTEM")
            // .requestMatchers("/product/shopping/**").authenticated() //URLに認証を要求
            .requestMatchers("/product/shopping/**").hasRole("ENDUSER")
            .requestMatchers("/product/index","/product/edit/**","/product/update/**").hasAnyRole("ADMIN","SYSTEM","OWNER","CONTENT")
            .requestMatchers("/product/**").hasAnyRole("ADMIN","SYSTEM","OWNER")
            .requestMatchers("/user/**","/cart/**","/payment/**","/order-history/**").hasRole("ENDUSER")
        )
            // .requestMatchers("/user/**","/cart/**","/payment/**").authenticated()
            // .anyRequest().access(manager)
            // .anyRequest().authenticated()          //他のURLはログイン後のみアクセス可能

        //ログインに関する設定
        .formLogin(form -> form                       //フォーム認証を有効化
            .loginPage("/login")                   //ログイン画面のURL
            .usernameParameter("usr")              //ユーザ名のパラメーター名を設定
            .passwordParameter("passwd")           //パスワードのパラメーター名を設定
            .defaultSuccessUrl("/home")      //ログイン成功後のリダイレクト先URL
            // .loginProcessingUrl("/login")          //ユーザ名・パスワードの送信先URL
            // .failureForwardUrl("/login")           //ログイン失敗時のリダイレクト先URL
        )
        //ログアウトに関する設定
        .logout(logout -> logout
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
            .invalidateHttpSession(true)         //ログアウト時のセッション破棄有無(tureは破棄)
        );
        return http.build();
    }

}
