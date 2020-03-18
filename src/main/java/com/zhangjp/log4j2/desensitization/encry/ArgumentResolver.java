package com.zhangjp.log4j2.desensitization.encry;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 创建时间 2019年九月12日 星期四 17:10
 * 作者: zhangjp
 * 描述：TODO
 */
public class ArgumentResolver implements HandlerMethodArgumentResolver {

    public ArgumentResolver() {}

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonSerialize.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        System.out.println("parameter = " + parameter);
        return null;
    }
}
