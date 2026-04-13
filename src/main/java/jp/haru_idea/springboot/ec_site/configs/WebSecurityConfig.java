package jp.haru_idea.springboot.ec_site.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    //common setting
    private void confCommonLogin(FormLoginConfigurer<HttpSecurity> form){
        form
            .usernameParameter("usr")              //ユーザ名のパラメーター名を設定
            .passwordParameter("passwd");           //パスワードのパラメーター名を設定
    }

    @Bean
    @Order(1)
    public SecurityFilterChain endUserSecurityFilterChain(HttpSecurity http) throws Exception{
        http
        .securityMatcher("/user/**", "/payment/**", "/order-history/**", "/cart/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/user/login").permitAll()
            .requestMatchers("/user/create/**","/user/save", "/user/profile/password/reset/**").permitAll()
            .requestMatchers("/user/**","/cart/**","/payment/**","/order-history/**").hasRole("ENDUSER")
        )
        .formLogin(form -> {
            form.loginPage("/user/login")                   //ログイン画面のURL
                .defaultSuccessUrl("/user/home");
            confCommonLogin(form);
        })
        .logout(logout -> logout
            .logoutUrl("/user/logout")             //POSTでログアウト
            .logoutSuccessUrl("/home")           //ログアウト成功後のリダイレクト先URL
            // .deleteCookies("")                    //ログアウト時に削除するクッキー名
            .invalidateHttpSession(true)         //ログアウト時のセッション破棄有無(tureは破棄)
        );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain backofficeSecurityFilterChain(HttpSecurity http) throws Exception{
        http
        .securityMatcher("/backoffice/**", "/product/backoffice/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/backoffice/login", "/backoffice/logout", "/backoffice/home").permitAll()
            .requestMatchers("/backoffice/search/customer").hasAnyRole("ADMIN","SUPPORT")  //指定URLに指定したロールユーザのみアクセス可能
            .requestMatchers("/backoffice/index/**","/backoffice/search/member").hasAnyRole("ADMIN","SYSTEM","OWNER")
            .requestMatchers("/backoffice/result/**").hasAnyRole("ADMIN","SUPPORT","SYSTEM","OWNER")
            .requestMatchers("/backoffice/create","/backoffice/save","/backoffice/edit","/backoffice/withdraw").hasRole("ADMIN")
            .requestMatchers("/backoffice/**").hasAnyRole("ADMIN","SYSTEM")
            .requestMatchers("/product/backoffice/edit/**","/product/backoffice/update/**").hasAnyRole("ADMIN","SYSTEM","OWNER","CONTENT")
            .requestMatchers("/product/backoffice/**").hasAnyRole("ADMIN","SYSTEM","OWNER")
        )
            // .anyRequest().access(manager)
            // .anyRequest().authenticated()          //他のURLはログイン後のみアクセス可能

        .formLogin(form -> {
            form.loginPage("/backoffice/login")
                .defaultSuccessUrl("/backoffice/home");
            confCommonLogin(form);
        })
        .logout(logout -> logout
            .logoutUrl("/backoffice/logout")
            .logoutSuccessUrl("/backoffice/home")
            .invalidateHttpSession(true)
        );
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception{
        http
        .securityMatcher("/product/**", "/home", "/password/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/product/shopping/index", "/product/shopping/detail/**").permitAll()
            .requestMatchers("/password/reset/**").permitAll()
            .requestMatchers("/home").permitAll()
        )
        .formLogin(form -> form.disable())
        .logout(logout -> logout.disable());
        return http.build();
    }
}
