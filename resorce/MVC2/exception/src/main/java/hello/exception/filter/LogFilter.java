package hello.exception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
//서블릿 제공
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST [{}][{}][{}]", uuid,
                    request.getDispatcherType(), requestURI);
            chain.doFilter(request, response); //다음 필터 OR 서블릿 호출
        } catch (Exception e) {
            log.info("e.getMessage()) = {}", e.getMessage());
            throw e; //예외를 잡아 WAS까지 올라감
        } finally {
            log.info("RESPONSE [{}][{}][{}]", uuid,
                    request.getDispatcherType(), requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}