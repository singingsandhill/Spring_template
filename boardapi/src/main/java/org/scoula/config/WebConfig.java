package org.scoula.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

@Slf4j
@Configuration
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    final String LOCATION = "c:/upload";
    final long MAX_FILE_SIZE = 10L * 1024 * 1024;
    final long MAX_REQUEST_SIZE = 20L * 1024 * 1024;
    final int FILE_SIZE_THRESHOLD = 5 * 1024 * 1024;

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ServletConfig.class };
    }

    // 스프링의 FrontController인 DispatcherServlet이 담당할 URL 매핑 패턴, / : 모든 요청에 대해 매핑
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    // POST body 문자 인코딩 필터 설정 - UTF-8 설정
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        return new Filter[] { characterEncodingFilter };
    }
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
        // MultipartConfigElement 객체를 생성하여 파일 업로드 설정을 지정합니다.
        // LOCATION: 업로드된 파일이 저장될 디렉토리 경로
        // MAX_FILE_SIZE: 업로드 가능한 최대 파일 크기 (바이트 단위)
        // MAX_REQUEST_SIZE: 한 요청당 최대 파일 업로드 크기 (바이트 단위)
        // FILE_SIZE_THRESHOLD: 업로드된 파일이 임시 디렉토리에 저장되기 전에 메모리에 유지되는 최대 크기 (바이트 단위)
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                LOCATION,
                MAX_FILE_SIZE,
                MAX_REQUEST_SIZE,
                FILE_SIZE_THRESHOLD);

        // registration 객체에 생성된 MultipartConfigElement 객체를 설정하여
        // 파일 업로드 설정을 적용합니다.
        registration.setMultipartConfig(multipartConfig);
    }
}