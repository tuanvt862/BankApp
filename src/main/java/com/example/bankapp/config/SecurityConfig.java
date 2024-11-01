package com.example.bankapp.config;


import com.example.bankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    AccountService accountService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cấu hình HttpSecurity mới với xử lý lỗi 403
        http
                .csrf(csrf -> csrf.disable())  // Tắt CSRF nếu không cần
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Chỉ cho phép ADMIN truy cập
                        .requestMatchers("/register", "/forgotPassword").permitAll()  // Mọi người có thể truy cập trang đăng ký
                        .anyRequest().authenticated()  // Tất cả các yêu cầu khác phải được xác thực
                )
                // Sử dụng AccessDeniedHandler để xử lý lỗi 403
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())  // Xử lý khi gặp lỗi 403 Forbidden
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Trang đăng nhập tùy chỉnh
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)  // Đăng nhập thành công sẽ điều hướng đến /dashboard
                        .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    // Tạo AccessDeniedHandler để xử lý lỗi 403
    @Bean
    public AccessDeniedHandlerImpl accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/dashboard");  // Điều hướng đến trang 403 khi bị từ chối quyền truy cập
        return accessDeniedHandler;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
    }
}


//@Configuration
//@EnableWebSecurity
//
//public class SecurityConfig {
//
//    @Autowired
//    AccountService accountService;
//
//    @Bean
//    public static PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/register").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/login")
//                        .defaultSuccessUrl("/dashboard", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/login?logout")
//                        .permitAll()
//                )
//                .headers(header -> header
//                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
//
//                );
//        return http.build();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
//    }
//}



