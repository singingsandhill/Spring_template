package org.scoula.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

// Spring의 설정 클래스를 나타내며, SecurityConfig 클래스가 Spring 설정으로 인식되도록 합니다.
@Configuration

// Spring Security의 웹 보안 기능을 활성화하는 어노테이션으로, 이 클래스가 Spring Security의 보안 설정을 담당하도록 지정합니다.
@EnableWebSecurity

// Log4j 로깅을 위한 어노테이션으로, 이 클래스 내에서 로깅을 쉽게 사용할 수 있게 해줍니다.
@Log4j

// MyBatis의 Mapper 인터페이스를 자동으로 스캔하고 Spring 빈으로 등록하기 위해 사용됩니다.
// "org.scoula.security.account.mapper" 패키지 내의 모든 Mapper 인터페이스를 스캔합니다.
@MapperScan(basePackages = {"org.scoula.security.account.mapper"})

// Spring의 컴포넌트 스캔을 위해 사용되며, "org.scoula.security" 패키지 내의 모든 컴포넌트를 스캔하고 Spring 컨텍스트에 등록합니다.
@ComponentScan(basePackages = {"org.scoula.security"})

// Lombok의 어노테이션으로, 클래스의 생성자를 자동으로 생성해줍니다.
// 생성자 주입을 위해 final로 선언된 필드가 있는 경우 이 어노테이션을 사용합니다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 사용자 세부 정보 관리를 위한 서비스로, 사용자 인증에 필요한 정보를 제공합니다.
    private final UserDetailsService userDetailsService;

    // 패스워드 인코더 빈(Bean)을 정의합니다.
    // BCryptPasswordEncoder를 사용하여 패스워드를 안전하게 인코딩합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
        http.addFilterBefore(encodingFilter(), CsrfFilter.class);

        // 경로별 접근 권한 설정을 정의합니다.
        http.authorizeRequests()
                .antMatchers("/security/all").permitAll() // "/security/all" 경로는 모든 사용자에게 접근 허용
                .antMatchers("/security/admin").access("hasRole('ROLE_ADMIN')") // "/security/admin" 경로는 ADMIN 역할이 있는 사용자만 접근 가능
                .antMatchers("/security/member").access("hasRole('ROLE_MEMBER')"); // "/security/member" 경로는 MEMBER 역할이 있는 사용자만 접근 가능

        // 폼 기반 로그인 설정을 구성합니다.
        http.formLogin()
                .loginPage("/security/login") // 사용자 정의 로그인 페이지의 URL을 "/security/login"으로 설정
                .loginProcessingUrl("/security/login") // 로그인 처리 URL을 "/security/login"으로 설정
                .defaultSuccessUrl("/"); // 로그인 성공 시 기본 리다이렉트 URL을 루트("/")로 설정

        // 로그아웃 설정을 정의합니다.
        http.logout()
                .logoutUrl("/security/logout") // 로그아웃 호출 URL을 "/security/logout"으로 설정
                .invalidateHttpSession(true) // 세션을 무효화하여 로그아웃 시 세션 데이터를 제거합니다.
                .deleteCookies("remember-me", "JSESSIONID") // 로그아웃 시 "remember-me" 및 "JSESSIONID" 쿠키를 삭제합니다.
                .logoutSuccessUrl("/security/logout"); // 로그아웃 성공 후 리다이렉트할 URL을 "/security/logout"으로 설정합니다.
    }

    // 인증 관리자 설정 메서드로, 사용자 인증 정보를 구성합니다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        // 로그 메시지를 출력하여 이 메서드가 호출된 것을 확인합니다.
        log.info("configure .........................................");

        // 주석 처리된 부분은 인메모리(in-memory) 인증 설정 예시입니다.
        // 실제 운영 환경에서는 이 설정을 사용하지 않고, 데이터베이스나 외부 인증 시스템을 사용해야 합니다.
        // 주석 처리된 부분:
        // auth.inMemoryAuthentication()
        //         .withUser("admin")
        //         .password("$2a$10$eVhXnPjFX7i30wtSISGFfuPZowtCMc0UVllrJNO3ePGA2.97akxiW")
        //         .roles("ADMIN", "MEMBER");
        //
        // auth.inMemoryAuthentication()
        //         .withUser("member")
        //         .password("$2a$10$eVhXnPjFX7i30wtSISGFfuPZowtCMc0UVllrJNO3ePGA2.97akxiW")
        //         .roles("MEMBER");

        // UserDetailsService를 사용하여 사용자 인증 정보를 로드하고, 패스워드 인코더를 사용하여 패스워드를 검증합니다.
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
