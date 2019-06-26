package com.bms.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
* @author YeChunBo
* @time 2017��9��8�� 
*
* ��˵��: ip ��Ȩ�ֻ࣬���������ļ��ж����˵�ip �ſ��Է��ʽӿ�
*/

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Bean   //�����ǵ�������ע��Ϊbean
    public HandlerInterceptor getMyInterceptor(){
        return new URLInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns ����������ع���, ����������� /url �����ȫ������
        // excludePathPatterns �û��ų�����
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
