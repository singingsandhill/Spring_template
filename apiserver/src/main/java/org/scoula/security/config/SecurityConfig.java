package org.scoula.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.mybatis.spring.annotation.MapperScan;
import org.scoula.security.filter.AuthenticationErrorFilter;
import org.scoula.security.filter.JwtAuthenticationFilter;
import org.scoula.security.filter.JwtUsernamePasswordAuthenticationFilter;
import org.scoula.security.handler.CustomAccessDeniedHandler;
import org.scoula.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration      // Spring의 설정 클래스를 나타내며, SecurityConfig 클래스가 Spring 설정으로 인식되도록 합니다.
@EnableWebSecurity  // Spring Security의 웹 보안 기능을 활성화하는 어노테이션으로, 이 클래스가 Spring Security의 보안 설정을 담당하도록 지정합니다.
@Log4j              // Log4j 로깅을 위한 어노테이션으로, 이 클래스 내에서 로깅을 쉽게 사용할 수 있게 해줍니다.
@MapperScan(basePackages = {"org.scoula.security.account.mapper"})  // MyBatis의 Mapper 인터페이스를 자동으로 스캔하고 Spring 빈으로 등록. // "org.scoula.security.account.mapper" 패키지 내의 모든 Mapper 인터페이스를 스캔합니다.
@ComponentScan(basePackages = {"org.scoula.security"})      // Spring의 컴포넌트 스캔을 위해 사용되며, "org.scoula.security" 패키지 내의 모든 컴포넌트를 스캔하고 Spring 컨텍스트에 등록합니다.
@RequiredArgsConstructor// Lombok의 어노테이션으로, 클래스의 생성자를 자동으로 생성해줍니다.   // 생성자 주입을 위해 final로 선언된 필드가 있는 경우 이 어노테이션을 사용합니다.

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 사용자 세부 정보 관리를 위한 서비스로, 사용자 인증에 필요한 정보를 제공합니다.
    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter;
    private final AuthenticationErrorFilter authenticationErrorFilter;

    // 패스워드 인코더 빈(Bean)을 정의합니다.
    // BCryptPasswordEncoder를 사용하여 패스워드를 안전하게 인코딩합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean   // AuthenticationManager 빈 등록
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean   // cross origin 접근 허용
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // CORS 설정을 적용할 URL 패턴 소스를 생성
        CorsConfiguration config = new CorsConfiguration(); // CORS 설정을 정의할 CorsConfiguration 객체 생성
        config.setAllowCredentials(true);       // 자격 증명(쿠키, HTTP 인증 등)을 허용
        config.addAllowedOriginPattern("*");    // 모든 출처(origin)에서 오는 요청을 허용 (와일드카드 패턴 사용)
        config.addAllowedHeader("*");           // 모든 요청 헤더를 허용
        config.addAllowedMethod("*");           // 모든 HTTP 메소드(GET, POST, PUT, DELETE 등)를 허용
        source.registerCorsConfiguration("/**", config);    // 모든 URL 패턴에 대해 위의 CORS 설정을 적용

        return new CorsFilter(source);          // 설정된 CORS 소스를 사용해 CorsFilter 생성 및 반환
    }

    // 접근 제한 무시 경로 설정 – resource
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/*", "/api/member/**");
    }

    // 문자셋 필터 설정 메서드로, UTF-8 인코딩을 강제로 설정하는 필터를 생성합니다.
    public CharacterEncodingFilter encodingFilter() {
        // CharacterEncodingFilter 객체를 생성하여 모든 요청과 응답에 UTF-8 인코딩을 적용합니다.
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8"); // 요청 및 응답의 문자셋을 UTF-8로 설정합니다.
        encodingFilter.setForceEncoding(true); // 인코딩 설정을 강제 적용합니다.
        return encodingFilter;
    }

    // HTTP 보안 설정을 구성하는 메서드입니다.
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 문자셋 필터를 CSRF 필터보다 앞에 추가하여 모든 요청에 대해 UTF-8 인코딩을 적용합니다.
        http.addFilterBefore(encodingFilter(), CsrfFilter.class)
                // 인증 에러 필터
                .addFilterBefore(authenticationErrorFilter, UsernamePasswordAuthenticationFilter.class)
                // Jwt 인증 필터
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 로그인 인증 필터
                .addFilterBefore(jwtUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 예외 처리 설정
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        http.httpBasic().disable() // 기본 HTTP 인증 비활성화
                .csrf().disable() // CSRF 비활성화
                .formLogin().disable() // formLogin 비활성화 관련 필터 해제
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 생성 모드 설정

        http
                .authorizeRequests() // 경로별 접근 권한 설정
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/api/security/all").permitAll() // 모두 허용
                .antMatchers("/api/security/member").access("hasRole('ROLE_MEMBER')") // ROLE_MEMBER 이상 접근 허용
                .antMatchers("/api/security/admin").access("hasRole('ROLE_ADMIN')") // ROLE_ADMIN 이상 접근 허용
                .anyRequest().authenticated(); // 나머지는 로그인 된 경우 모두 허용
    }

    // 인증 관리자 설정 메서드로, 사용자 인증 정보를 구성합니다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        // 로그 메시지를 출력하여 이 메서드가 호출된 것을 확인합니다.
        log.info("configure .........................................");
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
