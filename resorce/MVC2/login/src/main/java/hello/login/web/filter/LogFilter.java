package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;


@Slf4j
public class LogFilter implements Filter {


    @Override
    //필터 초기 메서드, 서블릿 컨테이너가 생성될 떄 호출
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    //고객 요청이 올 때마다 해당 메서드 호출,
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI= httpRequest.getRequestURI();
        String requestURL = String.valueOf(httpRequest.getRequestURL());


        String uuid= UUID.randomUUID().toString();
        try {
            log.info("Requset [{}] [{}] [{}]",uuid,requestURI,requestURL);
            chain.doFilter(request, response); //다음 필터 출력, 없으면 서블릿
        }catch (Exception e){
            throw  e;
        }finally{
            log.info("response [{}] [{}] -finally",uuid,requestURI);
        }


    }

    @Override
    //필터 종료 메서드 , 서블릿 컨테이너가 종료될 때 호출
    public void destroy() {
        log.info("log filter destory");
    }
}
