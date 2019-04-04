package com.sunjinke.log4j2.desensitization.fliter;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * 创建时间 2019年四月04日 星期四 11:18
 * 作者: zhangjunping
 * 描述：这边是日志记录这次请求的追踪信息 以便查找信息
 */
@WebFilter
@Log4j2
public class LogFilter implements Filter {

    @Value("${log.remoteAddr}")
    private String remoteAddr;

    @Value("${log.reqId}")
    private String reqId;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        TODO 这边看需求 可以从 header带来，很多微服务系统中，相互调用采用 加到请求头中将此值传递下去
//        配合 其他 微服务路由追踪插件来实现 追踪
        String reqIdValue = UUID.randomUUID().toString();
        String remoteAddrValue = request.getRemoteAddr();
        ThreadContext.put(remoteAddr,remoteAddrValue);
        ThreadContext.put(reqId,reqIdValue);
        log.info("-------请求开始,此次业务代码:{}，请求源IP:{}------",reqIdValue,remoteAddrValue);
        chain.doFilter(request,response);
        log.info("-------请求结束,此次业务代码:{}，请求源IP:{}------",reqIdValue,remoteAddrValue);
        ThreadContext.clearMap();


    }

    @Override
    public void destroy() {
    }
}
