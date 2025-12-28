package com.example.carespawbe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private CustomOAuth2SuccessHandler oAuth2SuccessHandler;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    //  0) Webhook chain chạy ĐẦU TIÊN và chỉ match /webhook/**
    @Bean
    @Order(0)
    public SecurityFilterChain webhookFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/carespaw/webhook/**")
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    // 1) API chain: JWT only, STATELESS => KHÔNG còn JSESSIONID cho API
    @Bean
    @Order(1)
    public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/carespaw/**")
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())
                .oauth2Login(o -> o.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/carespaw/auth/**", "/public/**").permitAll()
                                .requestMatchers("/carespaw/forum", "/carespaw/forum/**").permitAll()
                                .requestMatchers("/carespaw/shop/register").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/carespaw/shop/*").hasAnyRole("USER","SHOP_OWNER","ADMIN")
                        // nhớ thêm /api vào đây
                                .requestMatchers("/carespaw/payment/**", "/carespaw/sse/**").permitAll()
                                .requestMatchers("/carespaw/admin/**").hasRole("ADMIN")
                        .requestMatchers("/carespaw/shop/**").hasAnyRole("ADMIN", "SHOP_OWNER")
                                .requestMatchers("/carespaw/products/**").hasAnyRole("ADMIN", "SHOP_OWNER","USER")
                                .requestMatchers("/carespaw/feedbacks/**").hasAnyRole("USER", "SHOP_OWNER")
//                        .requestMatchers("/shopManager/**").hasAnyRole("SHOP_OWNER")
                        .requestMatchers("/carespaw/expert/**").hasAnyRole("ADMIN", "EXPERT")
                        .requestMatchers("/carespaw/user/**").hasAnyRole("ADMIN", "USER", "SHOP_OWNER", "EXPERT")
                        .requestMatchers("/carespaw/cart/**").hasAnyRole("USER","SHOP_OWNER","ADMIN")
                        .requestMatchers("/carespaw/location/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ 2) Web/OAuth2 chain: dùng cho login Google (có thể tạo session)
    @Bean
    @Order(2)
    public SecurityFilterChain webChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/public/**", "/login/**", "/oauth2/**").permitAll()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // nên ghi rõ, đừng *
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        config.addAllowedHeader("*");
        config.setExposedHeaders(List.of("Authorization"));

        // JWT dùng Authorization header thì KHÔNG cần credentials
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
