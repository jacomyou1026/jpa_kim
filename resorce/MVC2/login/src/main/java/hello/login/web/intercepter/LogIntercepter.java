package hello.login.web.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogIntercepter implements HandlerInterceptor {

    public static final String LOG_ID = "logID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID,uuid);

        //@RequestMapping : HandlerMethod
        //정적 리소스 : ResourceHttpRequestHandler

        if (handler instanceof HandlerMethod) {

            HandlerMethod hm = (HandlerMethod) handler;// 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다

        }

        log.info("REQUEST [{}] [{}] [{}]", uuid, requestURI, handler);

        return true; // 다음 어뎁터 호출



    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]",modelAndView);
        log.info("-----------ModelAndView--------");
        log.info("modelAndView.getClass().getName()  = {}",modelAndView.getClass().getName());

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}] [{}] [{}]",uuid,requestURI,handler);

        if(ex != null){
            log.error("afterCompletion error!!",ex);
        }


    }
}

