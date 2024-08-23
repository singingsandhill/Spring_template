package org.scoula.config;

import org.scoula.security.config.SecurityConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    // 파일 업로드 관련 상수
    final String LOCATION = "c:/upload"; // 파일 업로드 경로
    final long MAX_FILE_SIZE = 10L * 1024 * 1024; // 최대 파일 크기: 10MB
    final long MAX_REQUEST_SIZE = 20L * 1024 * 1024; // 최대 요청 크기: 20MB
    final int FILE_SIZE_THRESHOLD = 5 * 1024 * 1024; // 메모리에서 저장될 최대 파일 크기: 5MB

    // 루트 애플리케이션 컨텍스트를 설정하는 설정 클래스들을 반환합니다.
    // 이 메서드는 Spring이 애플리케이션을 시작할 때 어떤 설정 클래스를 로드할지를 결정합니다.
    // 여기서는 RootConfig.class와 SecurityConfig.class를 루트 설정 클래스로 사용하도록 지정하고 있습니다.
    @Override
    public Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class, SecurityConfig.class };
    }

    // 서블릿 애플리케이션 컨텍스트를 설정하는 설정 클래스를 반환합니다.
    // 여기서는 ServletConfig.class를 서블릿 설정 클래스로 지정하고 있습니다.
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ServletConfig.class };
    }

    // 스프링의 DispatcherServlet이 담당할 URL 매핑 패턴을 설정합니다.
    // "/"로 설정하여 모든 요청을 DispatcherServlet이 처리하도록 지정합니다.
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    // 서블릿 필터를 설정합니다.
    // 여기서는 CharacterEncodingFilter를 설정하여 모든 요청의 인코딩을 UTF-8로 강제 설정합니다.
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8"); // UTF-8 인코딩 설정
        characterEncodingFilter.setForceEncoding(true); // 강제로 인코딩 설정을 적용

        return new Filter[] { characterEncodingFilter }; // 필터 배열 반환
    }

    // 서블릿 등록을 커스터마이징하는 메서드입니다.
    // 여기서 파일 업로드와 관련된 설정을 적용합니다.
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true"); // 핸들러가 없을 경우 예외를 발생하도록 설정

        // MultipartConfigElement 객체를 생성하여 파일 업로드 설정을 지정합니다.
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                LOCATION, // 업로드된 파일이 저장될 경로
                MAX_FILE_SIZE, // 업로드 가능한 최대 파일 크기
                MAX_REQUEST_SIZE, // 한 요청당 최대 파일 업로드 크기
                FILE_SIZE_THRESHOLD // 임시 디렉토리에 저장되기 전에 메모리에 유지되는 최대 크기
        );

        // registration 객체에 생성된 MultipartConfigElement 객체를 설정하여
        // 파일 업로드 설정을 적용합니다.
        registration.setMultipartConfig(multipartConfig);
    }
}
