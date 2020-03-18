package com.zhangjp.log4j2.desensitization.config;

import com.zhangjp.log4j2.desensitization.encry.ArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 创建时间 2019年九月12日 星期四 17:14
 * 作者: zhangjp
 */
@Configuration
public class ArgumentHadlerConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new ArgumentResolver());
    }
}
