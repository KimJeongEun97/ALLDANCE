package com.alldance01.alldance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    //세션 인터셉터 클래스 자동 등록
    @Autowired
    private SessionIntercepter intercepter;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(intercepter)//인터셉터 등록
                .addPathPatterns("/**")//범위 지정(모든 페이지)
                .excludePathPatterns("/", "/css/**", "/js/**", "/images/**", "/mp4/**")
                .excludePathPatterns("/dbimgupload/**","/dbvideoupload/**","/lcimgupload/**","/lcvideoupload/**")
                .excludePathPatterns("/joinForm", "/loginForm", "/idCheck","/emailCheck", "/mail", "/mailConfirm")
                .excludePathPatterns("/findId", "/findIdComplete", "/findPwCertification", "/findPwChange")
                .excludePathPatterns("/danceBoardList","/introduction","/adInfo")
                .excludePathPatterns("/loginProc", "/joinProc", "/error/**", "/findIdProc","/findPwChangeProc");
    }
}
